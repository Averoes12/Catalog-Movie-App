package com.daff.cataloguemovieapi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.util.ArrayList;

import static android.os.Build.ID;
import static android.provider.BaseColumns._ID;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.TITLE;

public class FavoriteHelper {

    private static final String DATABASE_TABLE = DatabaseContract.TABLE_MOVIES;
//    private static final String DATABASE_TABLE_TV = DatabaseContract.TABLE_TVSHOW;

    private FavoriteDatabaseHelper databaseHelper;
    private static FavoriteHelper instance;
    private static SQLiteDatabase database;

    public FavoriteHelper(Context context) {

        databaseHelper = new FavoriteDatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (instance == null) {
                    instance = new FavoriteHelper(context);

                }
            }
        }
        return instance;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }


    public Cursor queryProviderById(String id) {
        return database.query(DATABASE_TABLE, null,
                ID + " = ?",
                new String[]{id},
                null,
                null,
                _ID + " ASC",
                null);

    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);

    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public Cursor cekTitle(String title) {
        Cursor cursor = database.query(DATABASE_TABLE,
                null,
                TITLE + "=?",
                new String[]{title},
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
