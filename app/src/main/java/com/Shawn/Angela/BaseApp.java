package com.Shawn.Angela;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class BaseApp extends Application {
    public static final String CHANNEL_1_ID = "battery notifications";
    public static final String CHANNEL_2_ID = "Journaling Reminder";


    @Override
    public void onCreate(){
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID, "Battery Notification", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("These notifications are for battery life");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID, "Journaling Reminder", NotificationManager.IMPORTANCE_LOW);
            channel2.setDescription("These notifications are for Journaling");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }



    }
}
