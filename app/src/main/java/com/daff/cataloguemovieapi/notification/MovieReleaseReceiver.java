package com.daff.cataloguemovieapi.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.daff.cataloguemovieapi.R;
import com.daff.cataloguemovieapi.model.movie.ResponseMovie;
import com.daff.cataloguemovieapi.model.movie.ResultsItem;
import com.daff.cataloguemovieapi.networking.APIService;
import com.daff.cataloguemovieapi.networking.ConfigRetrofit;
import com.daff.cataloguemovieapi.view.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.daff.cataloguemovieapi.BuildConfig.API_KEY;

public class MovieReleaseReceiver extends BroadcastReceiver {

    int NOTIFICATION_ID = 3;

    @Override
    public void onReceive(final Context context, Intent intent) {
        getReleaseMovie(context);
    }

    private void getReleaseMovie(final Context context) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        final String now = dateFormat.format(Calendar.getInstance().getTime());
        APIService service = ConfigRetrofit.getClient().create(APIService.class);
        service.getReleaseToday(API_KEY, now, now)
                .enqueue(new Callback<ResponseMovie>() {
                    @Override
                    public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                        List<ResultsItem> results = Objects.requireNonNull(response.body()).getResults();
                        int notifId = 0;

                        for (int i = 0; i < results.size(); i++) {
                            showNotif(context, results.get(i), notifId);
                            notifId++;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                        Log.d("getMovies", "onFailure: " + t.toString());
                    }
                });
    }

    private void showNotif(Context context, ResultsItem item, int notifId) {
        String CHANNEL_ID = "channel_02";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(item.getTitle())
                .setContentText(item.getOverview())
                .setSound(alarmSound)
                .setSmallIcon(R.drawable.icon)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }
        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notifId, builder.build());
        }
    }

    public void CancelNotif(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        Objects.requireNonNull(alarmManager).cancel(pendingIntent);
    }

    public void setAlarm(Context context, String type, String time, String message) {
        CancelNotif(context);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MovieReleaseReceiver.class);
        intent.putExtra("message", message);
        intent.putExtra("type", type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, 0);
        Objects.requireNonNull(alarmManager).setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }
}
