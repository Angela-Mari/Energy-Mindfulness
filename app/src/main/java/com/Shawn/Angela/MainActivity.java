package com.Shawn.Angela;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivtyTAG";
    private NotificationManagerCompat notificationManager;
    Button createJournalEntry;
    TextView batteryText;

    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        notificationManager = NotificationManagerCompat.from(this);

        // get battery percentage
        setContentView(R.layout.activity_main);
        batteryText = findViewById(R.id.batteryLife);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        batteryText.setText("Current Battery Percentage: " + String.valueOf(batteryPct) + "%");

        launcher = launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d(TAG, "onActivityResult: ");
                        // this callback executes when MainActivity returns from
                        // starting an activity (e.g. SecondActivity) that was
                        // started for a result
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Log.d(TAG, "activity result OK");
                            Intent data = result.getData();
                            String title = data.getStringExtra("title");
                            String date = data.getStringExtra("date");
                            String time = data.getStringExtra("time");
                            String mood = data.getStringExtra("mood");
                            String journalEntry = data.getStringExtra("journalEntry");
                            double battery = data.getDoubleExtra("battery", -1);

                            Journal newJournal = new Journal(title, date, time, journalEntry, mood, battery);
                            Log.d(TAG, "onActivityResult: New Journal Entry: " + newJournal.toString());
                        }
                        else {
                            Log.d(TAG, "onActivityResult: activity result not ok");
                        }

                    }
                });

        // launch CreateJournalActivity
        createJournalEntry = findViewById(R.id.createJournalButton);
        createJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: launch create entry");
                Intent intent = new Intent(MainActivity.this, CreateJournalActivity.class);
                launcher.launch(intent);
            }
        });

        // calendar view
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