package com.daff.cataloguemovieapi.helper;

import android.database.Cursor;

import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.POSTER;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.REL_DATE;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.VOTE;

public class MappingHelper {
    public static ArrayList<ResultsItem> mapCursorToArrayList(Cursor movie) {
        ArrayList<ResultsItem> list = new ArrayList<>();

        while (movie.moveToNext()) {
            int id = movie.getInt(movie.getColumnIndexOrThrow(_ID));
            String title = movie.getString(movie.getColumnIndexOrThrow(TITLE));
            String description = movie.getString(movie.getColumnIndexOrThrow(OVERVIEW));
            String date = movie.getString(movie.getColumnIndexOrThrow(REL_DATE));
            double vote = movie.getDouble(movie.getColumnIndexOrThrow(VOTE));
            String poster = movie.getString(movie.getColumnIndexOrThrow(POSTER));
            String backdrop = movie.getString(movie.getColumnIndexOrThrow(BACKDROP));
            list.add(new ResultsItem(id,description, title, poster, backdrop, date, vote));
        }
        return list;
    }

}
