package com.daff.favoriteapp.utils;

import android.database.Cursor;

public interface LoadFavoriteCallback {
    void postExecute(Cursor movies);
    void proExecute();
}
