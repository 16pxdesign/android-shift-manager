package com.ruszala.fueltrack.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.global.CurrentShift;

import java.util.Date;

/**
 * Service do manage notification that show real time running after start new shift
 */
public class TimingService extends Service {


    private final String channelId = "com.ruszala.fueltrack";
    private final int notificationId = 222;
    private CountDownTimer counter;
    private final IBinder binder = new LocalBinder();
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private boolean isRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("mylog", "TimingService onCreate: ");

        //setup notification bar
        Intent intent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, "fueltrack", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null, null);
        if (notificationManager != null)
            notificationManager.createNotificationChannel(channel);
        builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Shift time")
                .setChannelId(channelId)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        //add action button to notification
        Intent snoozeIntent = new Intent(this, Receiver.class);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);
        builder.addAction(R.drawable.quantum_ic_arrow_back_grey600_24, "End Shift", snoozePendingIntent);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("mylog", "TimingService onStartCommand: ");

        //init timer
        if (CurrentShift.getInstance().isShiftRunning()) {
            showNotification();
            startTimer(CurrentShift.getInstance().getShift().getStartDate());
        } else {
            stopByNotification();
        }

        return START_STICKY;
    }

    //method update notification bar by new string / time
    private void change(String string) {
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelId);
        builder.setContentText(string);

        notificationManager.notify(notificationId, builder.build());
    }

    //init and fire timer
    public void startTimer(final Date start) {
        stop();
        isRunning = true;
        final long startMils = start.getTime();
        counter = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                long nowMils = new Date().getTime();
                long difference = (nowMils - startMils) - (1000 * 60 * 60);

                String dateString = DateFormat.format("HH:mm:ss", new Date(difference)).toString();
                change("You working: " + dateString);

            }

            public void onFinish() {
                startTimer(CurrentShift.getInstance().getShift().getStartDate());
            }
        };
        counter.start();
    }

    //show notification bar
    private void showNotification() {
        notificationManager.notify(notificationId, builder.build());
    }

    //remove notification bar
    public void stopByNotification() {
        stop();
        notificationManager.cancel(notificationId);
    }

    //stop service
    public void stop() {
        Log.d("mylog", "TimingService stop: ");
        if (counter != null) {
            counter.cancel();
        }
        isRunning = false;
    }

    public void onDestroy() {
        Log.d("mylog", "TimingService onDestroy: ");
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //Custom binder
    public class LocalBinder extends Binder {
        public TimingService getService() {
            return TimingService.this;
        }
    }


}