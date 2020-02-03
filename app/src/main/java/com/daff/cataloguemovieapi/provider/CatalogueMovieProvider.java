package com.daff.cataloguemovieapi.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daff.cataloguemovieapi.db.FavoriteHelper;

import static com.daff.cataloguemovieapi.db.DatabaseContract.AUTHORITY;
import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.daff.cataloguemovieapi.db.DatabaseContract.TABLE_MOVIES;

public class CatalogueMovieProvider extends ContentProvider {

    private static final int FAVORITE = 11;
    private static final int FAVORITE_ID = 22;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIES, FAVORITE);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIES + "/#", FAVORITE_ID);
       }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                cursor = favoriteHelper.queryProvider();
                break;
            case FAVORITE_ID:
                cursor = favoriteHelper.queryProviderById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long add;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE:
                add = favoriteHelper.insertProvider(values);
                break;
            default:
                add = 0;

        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + add);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        favoriteHelper.open();
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVORITE_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
