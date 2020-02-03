package com.daff.cataloguemovieapi.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.model.tvshow.ResultsItem;

public class FavoriteTvDetail extends AppCompatActivity {
    ImageView backdrop, poster;
    TextView title, popularity, dateRelease, overview;
    ResultsItem item;
    final static String url_backdrop = "https://image.tmdb.org/t/p/w500";
    final static String url_poster = "https://image.tmdb.org/t/p/w154";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_tv_detail);

        backdrop = findViewById(R.id.backdrop_favoriteTv);
        poster = findViewById(R.id.poster_favoriteTv);
        title = findViewById(R.id.title_favoriteTv);
        popularity = findViewById(R.id.genre_favoriteTv);
        dateRelease = findViewById(R.id.date_favoriteTv);
        overview = findViewById(R.id.desc_favoriteTv);

        item = getIntent().getParcelableExtra("favoriteTv");
        getSupportActionBar().setTitle(item.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movieFavoriteTvData();
    }

    private void movieFavoriteTvData() {
        title.setText(item.getName());
        popularity.setText(String.format(getResources().getString(R.string.vote) + " %s", item.getVoteAverage()));
        dateRelease.setText(String.format(getResources().getString(R.string.release_date) + " %s", item.getFirstAirDate()));
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
}
