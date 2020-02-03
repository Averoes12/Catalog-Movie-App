package com.daff.cataloguemovieapi.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;

import java.lang.annotation.Target;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.Result;

import static com.daff.cataloguemovieapi.db.DatabaseContract.FavoriteColumns.CONTENT_URI;

public class StackRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context context;
    int appWidgetId;

    private Cursor list;

    public StackRemoteViewFactory(Context appContext, Intent intent) {
        context = appContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {


    }

    @Override
    public void onDataSetChanged() {
        if (list != null){
            list.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        list = context.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );

        Binder.restoreCallingIdentity(identityToken);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ResultsItem item = getItem(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w185/" + item.getPosterPath())
                    .into(512, 512)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        Bundle bundle = new Bundle();
        bundle.putInt(ImageBannerWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return remoteViews;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private ResultsItem getItem(int position) {
        if (!list.moveToPosition(position)) {
            throw new IllegalArgumentException("position invalid");
        }
        return new ResultsItem(list);
    }
}
