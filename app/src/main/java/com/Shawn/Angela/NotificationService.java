package com.Shawn.Angela;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Source Tutorials:
 *
 * How to use the Broadcast Receiver Service
 * https://youtu.be/KAKn6XdMaxM
 *
 * How to use notification channels and builders
 * https://youtu.be/CZ575BuLBo4
 *
 * How to send a notification with the app closed, Service Demo
 * https://www.tutorialspoint.com/send-a-notification-when-the-android-app-is-closed
 *
 */
public class NotificationService extends Service {


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);


                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level * 100 / (float)scale;


                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), BaseApp.CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_baseline_battery_full_1)
                        .setContentTitle("What is your emotional battery at? Take a sec to reflect.")
                        .setContentText("Battery at " + String.valueOf(batteryPct))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_STATUS);

                if (batteryPct == 10 || batteryPct ==20 || batteryPct ==30 || batteryPct==40 || batteryPct==50 || batteryPct ==60 || batteryPct==70){
                    builder.build();
                    notificationManager.notify(1, builder.build());
                }

            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        return START_STICKY;
    }


    @Override
    public void onCreate() {
        //.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        //Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

}