package com.daff.favoriteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daff.favoriteapp.adapter.FavoriteAdapter;
import com.daff.favoriteapp.model.FavoriteResult;
import com.daff.favoriteapp.utils.LoadFavoriteCallback;
import com.daff.favoriteapp.utils.MappingHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements LoadFavoriteCallback, View.OnClickListener {

    RecyclerView listFavorite;
    FavoriteAdapter adapter;
    ArrayList<FavoriteResult> listItem;
    FloatingActionButton restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listFavorite = findViewById(R.id.list_favorite);
        restart = findViewById(R.id.restart);
        restart.setOnClickListener(this);

        adapter = new FavoriteAdapter(this);
        listFavorite.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listFavorite.setHasFixedSize(true);
        listFavorite.setItemAnimator(new DefaultItemAnimator());
        listFavorite.setAdapter(adapter);

        if (savedInstanceState == null) {
            new LoadMoviesAsync(this, this).execute();
        }else {
            ArrayList<FavoriteResult> favoritesItem = savedInstanceState.getParcelableArrayList("state");
            if (favoritesItem != null) {
                adapter.setMovieResult(favoritesItem);
                adapter.notifyDataSetChanged();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        listFavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void postExecute(Cursor movies) {
        listItem = MappingHelper.mapCursorToArrayList(movies);
        if (listItem.size() > 0) {
            adapter.setMovieResult(listItem);
            adapter.notifyDataSetChanged();
        } else if (listItem == null) {
            Snackbar.make(listFavorite, "Nothing data to show", Snackbar.LENGTH_LONG).show();
        }  else {
            adapter.setMovieResult(new ArrayList<FavoriteResult>());
            Snackbar.make(listFavorite, "Nothing data", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void proExecute() {

    }

    @Override
    public void onClick(View view) {
        new LoadMoviesAsync(this, this).execute();
    }

    public static class LoadMoviesAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakMoviesHelper;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        public LoadMoviesAsync(Context moviesHelper, LoadFavoriteCallback loadMoviesCallback) {
            weakMoviesHelper = new WeakReference<>(moviesHelper);
            weakCallback = new WeakReference<>(loadMoviesCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().proExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakMoviesHelper.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor resultsItemMovies) {
            super.onPostExecute(resultsItemMovies);
            weakCallback.get().postExecute(resultsItemMovies);
        }
    }


}