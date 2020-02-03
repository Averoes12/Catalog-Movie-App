package com.daff.cataloguemovieapi.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import static com.daff.cataloguemovieapi.notification.Utils.KEY_DAILY_REMINDER;
import static com.daff.cataloguemovieapi.notification.Utils.KEY_MESSAGE_DAILY;
import static com.daff.cataloguemovieapi.notification.Utils.KEY_MESSAGE_RELEASE;
import static com.daff.cataloguemovieapi.notification.Utils.PREF_NAME;

public class NotificationPreference {

    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    private final static String PREFERENCE = "reminderPreferences";
    private final static String KEY_DAILY = "DailyReminder";
    private final static String KEY_MESSAGE_Release = "messageRelease";
    private final static String KEY_MESSAGE_DAILY = "messageDaily";


    @SuppressLint("CommitPrefEdits")
    public NotificationPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void setTimeRelease(String time){
        editor.putString(KEY_DAILY,time);
        editor.commit();
    }
    public void setReleaseMessage (String message){
        editor.putString(KEY_MESSAGE_Release,message);
    }
    public void setTimeDaily(String time){
        editor.putString(KEY_DAILY,time);
        editor.commit();
    }
    public void setDailyMessage(String message){
        editor.putString(KEY_MESSAGE_DAILY,message);
    }
}

