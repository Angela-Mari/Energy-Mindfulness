package com.Shawn.Angela;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JournalDetailView extends AppCompatActivity {

    static final String TAG = "JournalDetailView";

    TextView dateTextView;
    TextView batteryView;
    TextView moodView;
    TextView titleTextView;
    TextView journalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //wire up TextViews
        dateTextView = findViewById(R.id.date);
        batteryView = findViewById(R.id.batteryLevel);
        titleTextView = findViewById(R.id.title);
        moodView = findViewById(R.id.mood);
        journalTextView = findViewById(R.id.journalEntry);

        //get items passes in from Journal View
        Intent intent = getIntent();
        if (intent != null) { // good practice
            Log.d(TAG, "activity result OK");
            String fullEntryText = intent.getStringExtra("fullEntryText");
            journalTextView.setText(fullEntryText);
            String entryTitle = intent.getStringExtra("entryTitle");
            titleTextView.setText(entryTitle);
//            String entryDate = intent.getStringExtra("entryDate");
//            dateTextView.setText(entryDate);
            String entryDateTime = intent.getStringExtra("entryDateTime");
            dateTextView.setText(entryDateTime);
            String entryMood = intent.getStringExtra("entryMood");
            moodView.setText(entryMood);
            int batteryEntry = intent.getIntExtra("batteryEntry", 0);
            batteryView.setText(String.valueOf(batteryEntry));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // make sure to pass back date
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(JournalDetailView.this, JournalView.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}