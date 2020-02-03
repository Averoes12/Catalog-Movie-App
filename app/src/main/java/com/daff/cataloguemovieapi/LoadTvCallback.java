package com.daff.cataloguemovieapi;

import android.database.Cursor;

import com.daff.cataloguemovieapi.model.tvshow.ResultsItem;

import java.util.ArrayList;

public interface LoadTvCallback {
    void preExecute();
    void postExecute(Cursor favorite);
}

