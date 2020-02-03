package com.daff.favoriteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daff.favoriteapp.model.Favorite;
import com.daff.favoriteapp.model.FavoriteResult;

public class DetailActivity extends AppCompatActivity {
    ImageView backdrop, poster;
    TextView title, popularity, dateRelease, overview;
    FavoriteResult item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backdrop = findViewById(R.id.backdrop_detail);
        poster = findViewById(R.id.poster_detail);
        title = findViewById(R.id.title_detail);
        popularity = findViewById(R.id.genre_detail);
        dateRelease = findViewById(R.id.date_detail);
        overview = findViewById(R.id.desc_detail);
        item = getIntent().getParcelableExtra("item_favorite");

        getSupportActionBar().setTitle(item.getOriginalTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setData(item);
    }

    void setData(FavoriteResult item){
        title.setText(String.format("Title : %s", item.getOriginalTitle()));
        overview.setText(String.format("Overview \n\n %s", item.getOverview()));
        popularity.setText(String.format("Vote : %s",item.getVoteAverage()));
        dateRelease.setText(String.format("Release Date %s", item.getReleaseDate()));

        String imgUrl = "https://image.tmdb.org/t/p/w185/";
        Glide.with(getApplicationContext())
                .load(imgUrl + item.getBackdropPath())
                .override(600, 200)
                .into(backdrop);

        Glide.with(getApplicationContext())
                .load(imgUrl + item.getPosterPath())
                .override(600, 200)
                .into(poster);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
