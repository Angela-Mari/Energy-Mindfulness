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
    JournalOpenHelper helper;
    ActivityResultLauncher<Intent> launcher;
    String myDate;
    TextView title;
    TextView journalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //wire up TextViews
        journalTextView = findViewById(R.id.journalEntry);

        //get items passes in from Journal View
        Intent intent = getIntent();
        if (intent != null) { // good practice
            Log.d(TAG, "activity result OK");
            String fullEntryText = intent.getStringExtra("fullEntryText");
            journalTextView.setText(fullEntryText);
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