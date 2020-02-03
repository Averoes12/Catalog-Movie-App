package com.daff.cataloguemovieapi.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.daff.cataloguemovieapi.LoadMovieCallback;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.adapter.FavoriteAdapter;
import com.daff.cataloguemovieapi.db.FavoriteHelper;
import com.daff.cataloguemovieapi.helper.MappingHelper;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class FavoriteActivity extends AppCompatActivity implements LoadMovieCallback {

    RecyclerView listFavorite;
    ProgressBar loading;
    FavoriteAdapter adapter;
    FavoriteHelper favoriteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorite_movie);

        listFavorite = findViewById(R.id.list_favorite);
        loading = findViewById(R.id.loading_favorite);
        loading.setVisibility(View.VISIBLE);
        adapter = new FavoriteAdapter(this);
        favoriteHelper = FavoriteHelper.getInstance(this);
        favoriteHelper.open();

        getSupportActionBar().setTitle(getString(R.string.favorite));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            new LoadNoteAsync(this, this).execute();
        } else {
            ArrayList<ResultsItem> favoritesItem = savedInstanceState.getParcelableArrayList("state");
            if (favoritesItem != null) {
                favoritesItem.clear();
                adapter.setListFavorite(favoritesItem);
            }
        }
        showList();
    }

    void showList() {
        loading.setVisibility(View.GONE);
        listFavorite.setLayoutManager(new LinearLayoutManager(this));
        listFavorite.setHasFixedSize(true);
        listFavorite.setAdapter(adapter);
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(this).runOnUiThread(() ->
                loading.setVisibility(View.VISIBLE));
    }

    @Override
    public void postExecute(Cursor favorite) {
        loading.setVisibility(View.GONE);
        ArrayList<ResultsItem> favoritesItem = MappingHelper.mapCursorToArrayList(favorite);
        if (favoritesItem.size() > 0) {
            adapter.setListFavorite(favoritesItem);
        } else {
            adapter.setListFavorite(new ArrayList<>());
            Snackbar.make(listFavorite, getString(R.string.nothing_favorite), Snackbar.LENGTH_SHORT).show();
        }
    }

    private static class LoadNoteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakHelper;
        private final WeakReference<LoadMovieCallback> weakCallback;

        LoadNoteAsync(Context context, LoadMovieCallback callback) {
            weakHelper = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakHelper.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor resultsItems) {
            super.onPostExecute(resultsItems);
            weakCallback.get().postExecute(resultsItems);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                adapter.deleteItem(position);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("state", adapter.getListFavorite());
    }

    @Override
    protected void onResume() {
        super.onResume();
        listFavorite.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
