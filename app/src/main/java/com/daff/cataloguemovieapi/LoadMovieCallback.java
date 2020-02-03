package com.daff.cataloguemovieapi;

import android.database.Cursor;

import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public interface LoadMovieCallback {
    void preExecute();
    void postExecute(Cursor favorite);
}

