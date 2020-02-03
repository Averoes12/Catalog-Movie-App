package com.daff.favoriteapp.utils;

import android.database.Cursor;

import com.daff.favoriteapp.model.FavoriteResult;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.BACKDROP;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.OVERVIEW;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.POSTER;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.REL_DATE;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.TITLE;
import static com.daff.favoriteapp.database.DatabaseConstract.FavoriteColumns.VOTE;

public class MappingHelper {
    public static ArrayList<FavoriteResult> mapCursorToArrayList(Cursor movie) {
        ArrayList<FavoriteResult> list = new ArrayList<>();

        if (movie != null){
            while (movie.moveToNext()) {
                int id = movie.getInt(movie.getColumnIndexOrThrow(_ID));
                String title = movie.getString(movie.getColumnIndexOrThrow(TITLE));
                String description = movie.getString(movie.getColumnIndexOrThrow(OVERVIEW));
                String date = movie.getString(movie.getColumnIndexOrThrow(REL_DATE));
                double vote = movie.getDouble(movie.getColumnIndexOrThrow(VOTE));
                String poster = movie.getString(movie.getColumnIndexOrThrow(POSTER));
                String backdrop = movie.getString(movie.getColumnIndexOrThrow(BACKDROP));
                list.add(new FavoriteResult(id,description, title, poster, backdrop, date, vote));
            }

        }

        return list;
    }

}
