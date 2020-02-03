package com.daff.cataloguemovieapi.view.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.db.FavoriteHelper;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;

public class FavoriteDetail extends AppCompatActivity implements View.OnClickListener {
    private ImageView backdrop, poster;
    private TextView title, popularity, dateRelease, overview;
    private Button btnUnfavorite;
    private ResultsItem item;
    private FavoriteHelper favoriteHelper;
    final static String url_backdrop = "https://image.tmdb.org/t/p/w500";
    final static String url_poster = "https://image.tmdb.org/t/p/w154";
    public static int idMovies;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);

        backdrop = findViewById(R.id.backdrop_favorite);
        poster = findViewById(R.id.poster_favorite);
        title = findViewById(R.id.title_favorite);
        popularity = findViewById(R.id.genre_favorite);
        dateRelease = findViewById(R.id.date_favorite);
        overview = findViewById(R.id.desc_favorite);

        btnUnfavorite = findViewById(R.id.button_favorite_detail);
        btnUnfavorite.setOnClickListener(this);

        favoriteHelper = FavoriteHelper.getInstance(this);
        favoriteHelper.open();

        item = getIntent().getParcelableExtra("favorite");
        position = getIntent().getIntExtra("position", 0);
        getSupportActionBar().setTitle(item.getOriginalTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieFavoriteData();

        idMovies = item.getId();
    }

    void movieFavoriteData() {

        title.setText(item.getOriginalTitle());
        popularity.setText(String.format(getResources().getString(R.string.vote) + " %s", item.getVoteAverage()));
        dateRelease.setText(String.format(getResources().getString(R.string.release_date) + " %s", item.getReleaseDate()));
        overview.setText(String.format(getResources().getString(R.string.overview) + "\n\n %s", item.getOverview()));

        Glide.with(this).load(url_backdrop + item.getBackdropPath()).override(800, 600).into(backdrop);
        Glide.with(this).load(url_poster + item.getPosterPath()).override(512, 512).into(poster);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_favorite_detail) {
            Intent intent = new Intent();
            getContentResolver().delete(getIntent().getData(), null, null);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            finish();
            Toast.makeText(this, getString(R.string.delete_data), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }
}
