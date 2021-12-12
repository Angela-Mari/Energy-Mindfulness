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
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivtyTAG";

    static final String SHARED_PREFERENCES_FNAME = "sharedpreffname";
    static final String NAME_KEY = "name";

    private NotificationManagerCompat notificationManager;
    Button createJournalEntry;
    TextView batteryText;
    JournalOpenHelper helper;
    ActivityResultLauncher<Intent> launcher;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //shared pref
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FNAME, 0);
        String name = sharedPreferences.getString(NAME_KEY, ""); // second arg is default value
        nameEditText = findViewById(R.id.nameView);
        nameEditText.setText(name);

        //SQLite
        helper = new JournalOpenHelper(this);

        // Demo entries Run only once, uninstall to clear
        //Friday
//        helper.insertJournal(new Journal("Lots of problems today!!", "2021.12.10", "Fri, 10 Dec 2021 9:18", "This is for all you lovers out there. Stop it. Yeah Mom, we know, you’ve told us this story a million times. You felt sorry for him so you decided to go with him to The Fish Under The Sea Dance. Oh, uh, hey you, get your damn hands off her. Do you really think I oughta swear? He’s your brother, Mom.", "Mad 😡", 61));
//
//        //Saturday
//        helper.insertJournal(new Journal("Overslept today...", "2021.12.11", "Sat, 11 Dec 2021 08:18", "I over slept, look I need your help. I have to ask Lorraine out but I don’t know how to do it. I have to ask Lorraine out but I don’t know how to do it. George. George. Weight has nothing to do with it. I know, and all I could say is I’m sorry. I can’t believe you loaned me a car, without telling me it had a blindspot. I could’ve been killed.", "Tired 😴", 78));
//        helper.insertJournal(new Journal("Weather", "2021.12.11", "Sat, 11 Dec 2021 12:18", "When could weathermen predict the weather, let alone the future. Yeah, alright, bye-bye. What? Perfect, just perfect. Can I go now, Mr. Strickland? Over there, on my hope chest. I’ve never seen purple underwear before, Calvin.", "Happy 😊", 55));
//        helper.insertJournal(new Journal("Emo Hours", "2021.12.11", "Sat, 11 Dec 2021 01:18", "Oh, then I wanna give her a call, I don’t want her to worry about you. You okay, is everything alright? Can’t be. This is nuts. Aw, c’mon. I’m too loud. I can’t believe it. I’m never gonna get a chance to play in front of anybody. Great Scott. Let me see that photograph again of your brother. Just as I thought, this proves my theory, look at your brother.", "Sad 😭", 22));
//
//
//        //Sunday
//        helper.insertJournal(new Journal("Bad morning :(", "2021.12.12", "Sat, 12 Dec 2021 9:18", "Pretty Mediocre photographic fakery, they cut off your brother’s hair. Save the clock tower, save the clock tower. Mayor Wilson is sponsoring an initiative to replace that clock. Thirty years ago, lightning struck that clock tower and the clock hasn’t run since. We at the Hill Valley Preservation Society think it should be preserved exactly the way it is as part of our history and heritage. Biff. I will. Oh.", "Mad 😡", 61));
//        helper.insertJournal(new Journal("Finally got a break", "2021.12.12", "Sat, 12 Dec 2021 12:18", "Science Fiction Theater. Uh, stories, science fiction stories, about visitors coming down to Earth from another planet. Thank god I still got my hair. What on Earth is that thing I’m wearing? Aw yeah, everything is great. Who are you calling spook, pecker-wood.", "Happy 😊", 55));
//        helper.insertJournal(new Journal("Good end of the day", "2021.12.12", "Sat, 11 Dec 2021 16:18", "When could weathermen predict the weather, let alone the future. Yeah, alright, bye-bye. What? Perfect, just perfect. Can I go now, Mr. Strickland? Over there, on my hope chest. I’ve never seen purple underwear before, Calvin.", "Hopeful 🤔", 89));

        // notifications
        notificationManager = NotificationManagerCompat.from(this);

        // get battery percentage
        batteryText = findViewById(R.id.batteryLife);

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        batteryText.setText("Current Battery Percentage: " + String.valueOf(level) + "%");

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                            int battery = data.getIntExtra("battery", -1);

                            Journal newJournal = new Journal(title, date, time, journalEntry, mood, battery);
                            helper.insertJournal(newJournal);
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

                    intent.putExtra("year", year);
                    intent.putExtra("month", month +1);
                    intent.putExtra("day", dayOfMonth);
                    launcher.launch(intent);
                }
            });
        }
    }





    @Override
    protected void onStop () {
        super.onStop();
        String name = nameEditText.getText().toString();

        // persistent data storage: save data between executions of the app
        // few approaches to do this
        // 1. shared preferences: can store int, double, string, stringset, etc.
        // 2. read/write to a file
        // 3. database like SQLite
        // 4. Room persistent library (abstraction layer on top of SQLite)

        // for 1. we store key value pairs
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FNAME, 0);
        // 0 Context.MODE_PRIVATE
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME_KEY, name);
        editor.apply(); // or commit()
        // save the edits!! very important!!

        startService( new Intent( this, NotificationService.class ));
    }

}