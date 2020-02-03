package com.daff.cataloguemovieapi.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.notification.DailyReceiver;
import com.daff.cataloguemovieapi.notification.MovieReleaseReceiver;
import com.daff.cataloguemovieapi.notification.NotificationPreference;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    Switch dailyReminder, upcomingReminder;
    TextView changeLang;
    DailyReceiver dailyReceiver;
    MovieReleaseReceiver ReleaseReceiver;
    NotificationPreference notificationPreference;
    SharedPreferences spReleaseReminder, spDailyReminder;
    SharedPreferences.Editor edtReleaseReminder, edtDailyReminder;

    String TYPE_DAILY = "reminderDaily";
    String TYPE_RELEASE = "reminderRelease";
    String DAILY_REMINDER = "dailyReminder";
    String RELEASE_REMINDER = "releaseReminder";
    String KEY_RELEASE = "Release";
    String KEY_DAILY_REMINDER = "Daily";

    String timeDaily = "07:44";
    String timeRelease = "07:44";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setTitle(getString(R.string.setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dailyReminder = findViewById(R.id.daily_reminder);
        upcomingReminder = findViewById(R.id.upcoming_reminder);
        changeLang = findViewById(R.id.change_lang);

        changeLang.setOnClickListener(this);

        dailyReceiver = new DailyReceiver();
        ReleaseReceiver = new MovieReleaseReceiver();
        notificationPreference = new NotificationPreference(this);

        setPreference();

        upcomingReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            edtReleaseReminder = spReleaseReminder.edit();
            if (isChecked) {
                edtReleaseReminder.putBoolean(KEY_RELEASE, true);
                edtReleaseReminder.apply();
                releaseOn();
            } else {
                edtReleaseReminder.putBoolean(KEY_RELEASE, false);
                edtReleaseReminder.apply();
                releaseOff();
            }
        });

        dailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            edtDailyReminder = spDailyReminder.edit();
            if (isChecked) {
                edtDailyReminder.putBoolean(KEY_DAILY_REMINDER, true);
                edtDailyReminder.apply();
                dailyOn();
            } else {
                edtDailyReminder.putBoolean(KEY_DAILY_REMINDER, false);
                edtDailyReminder.apply();
                dailyOff();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent setting = new Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(setting);
    }

    private void releaseOn() {
        String message = "Upcoming Release";
        notificationPreference.setTimeRelease(timeRelease);
        notificationPreference.setReleaseMessage(message);
        ReleaseReceiver.setAlarm(SettingActivity.this, TYPE_RELEASE, timeRelease, message);
    }

    private void releaseOff() {
        ReleaseReceiver.CancelNotif(SettingActivity.this);
    }
    private void dailyOn() {
        String message = "Daily Reminder";
        notificationPreference.setTimeDaily(timeDaily);
        notificationPreference.setDailyMessage(message);
        dailyReceiver.setAlarm(SettingActivity.this, TYPE_DAILY, timeDaily, message);
    }

    private void dailyOff() {
        dailyReceiver.CancelNotif(SettingActivity.this);
    }
    private void setPreference() {
        spReleaseReminder = getSharedPreferences(RELEASE_REMINDER, MODE_PRIVATE);
        boolean checkUpcomingReminder = spReleaseReminder.getBoolean(KEY_RELEASE, false);
        upcomingReminder.setChecked(checkUpcomingReminder);
        spDailyReminder = getSharedPreferences(DAILY_REMINDER, MODE_PRIVATE);
        boolean checkDailyReminder = spDailyReminder.getBoolean(KEY_DAILY_REMINDER, false);
        dailyReminder.setChecked(checkDailyReminder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
