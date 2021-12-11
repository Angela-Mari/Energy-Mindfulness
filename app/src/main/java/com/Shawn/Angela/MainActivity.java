package com.Shawn.Angela;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat notificationManager;

    TextView batteryText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationManager = NotificationManagerCompat.from(this);

        setContentView(R.layout.activity_main);
        batteryText = findViewById(R.id.batteryLife);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        batteryText.setText("Current Battery Percentage: " + String.valueOf(batteryPct));

        //sendNotification(batteryPct);

        CalendarView calendarView = findViewById(R.id.calendarView);
        if (calendarView != null) {
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
                    String msg = "Selected date is " + (month + 1) + "/" + dayOfMonth + "/" + year;
                    //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, JournalView.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // we need to get a menu inflater to
        // inflate main_menu.xml
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.return_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // figure out which menuItem this is
        int itemId = item.getItemId();
        switch(itemId){
            case R.id.menuBack:
                finish();
                return true; // this event has been consumed/handled
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop () {
        super.onStop();
        startService( new Intent( this, NotificationService.class ));
    }

//    public void sendNotification(float batteryCharge){
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BaseApp.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_baseline_battery_full_1)
//                .setContentTitle("What is your Emotional Battery Status")
//                .setContentText("Remember to take a break from your phone to charge your battery. Your phone's battery is at " + String.valueOf(batteryCharge))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_STATUS);
//
//        builder.build();
//
//        notificationManager.notify(1, builder.build());
//
//    }



}