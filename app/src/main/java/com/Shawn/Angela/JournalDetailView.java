package com.Shawn.Angela;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JournalDetailView extends AppCompatActivity {

    static final String TAG = "JournalDetailView";
    List<Journal> journalEntries;
    JournalOpenHelper helper;
    ActivityResultLauncher<Intent> launcher;
    String myDate;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_detail_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}