package com.Shawn.Angela;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class JournalView extends AppCompatActivity {

    static final String TAG = "JournalView";
    List<Journal> journalEntries;
    JournalOpenHelper helper;
    ActivityResultLauncher<Intent> launcher;
    String myDate;
    String dateTitle;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view);

        //menu back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "onCreate: in journal view");
        //title
        title = findViewById(R.id.recTitle);

        //journal entries
        journalEntries = new ArrayList<>();

        //get date
        Intent intent = getIntent();
        if (intent != null) { // good practice
            Log.d(TAG, "activity result OK");
            int year = intent.getIntExtra("year", 0);
            int month = intent.getIntExtra("month", 0);
            int day = intent.getIntExtra("day", 0);

            myDate = year + "." + month + "." + day;
            Log.d(TAG, "onCreate: myDate " + myDate);
            dateTitle = day + "/" + month + "/" + year;
            title.setText(dateTitle);
        }

        // get journals for that day
        helper = new JournalOpenHelper(this);


        if (myDate != null){
           journalEntries = helper.getSelectJournalsByDate(myDate);
           title.setText(journalEntries.size() + " " + (journalEntries.size() == 1? "Journal Entry for " : "Journal Entries for " ) + dateTitle);
            Log.d(TAG, "onCreate: we got journals " + journalEntries);
        }

        // set up Recycler View
        RecyclerView recyclerView = findViewById(R.id.myRecycler);

        //Recycler view is more performant because it recycles views when they go off the screen
        //1. set layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //2. set up an adapter

        //2.1 set up custom adapter by subclassing the RecyclerView.Adapter
        //2.2 set up custom view manager
        //2.3 wire it all up
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(JournalView.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //nested class
    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        @NonNull
        @Override
        public CustomAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //inflate xml layout for child view holder
            //1. standard layout from android
            //View view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, parent, false);
            //2. or a custom layout
            View view = LayoutInflater.from(JournalView.this).inflate(R.layout.journal_card, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
            Journal j = journalEntries.get(position);
            holder.updateView(j);
        }

        @Override
        public int getItemCount() {
            return journalEntries.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView myTitle;
            TextView dateTime;
            TextView myBatt;
            TextView myMood;
            TextView myPreview;
            ImageView image1;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                myTitle = itemView.findViewById(R.id.myTitle1);
                dateTime = itemView.findViewById(R.id.dateTimeView);
                myBatt = itemView.findViewById(R.id.myBatteryEnergy);
                myMood = itemView.findViewById(R.id.moodCardView);
                myPreview = itemView.findViewById(R.id.textPreview);
                image1 = itemView.findViewById(R.id.imageView1);

                itemView.setOnClickListener(this);
            }

            public void updateView(Journal j){

                myTitle.setText(j.getTitle());
                dateTime.setText(j.getTime());
                myBatt.setText("Battery: " + String.valueOf(j.getBatteryPercentage()) + "%");

                String[] splitStr = j.getMood().split("\\s+");
                myMood.setText("Mood: " + splitStr[1]);
                int splitNum = 100;
                boolean dotdot = true;
                if (j.getJournalEntry().length() < 100){
                    splitNum = j.getJournalEntry().length();
                }
                myPreview.setText(j.getJournalEntry().substring(0, splitNum) + (dotdot? "... " : ""));

                if (j.getBatteryPercentage() > 75){
                    image1.setImageResource(R.drawable.happy_battery_2);
                }
                if (j.getBatteryPercentage() <= 75 && j.getBatteryPercentage() > 25){
                    image1.setImageResource(R.drawable.meh_battery);
                }
                if (j.getBatteryPercentage() <= 25){
                    image1.setImageResource(R.drawable.sad_battery);
                }

            }

            //3. set up click listener

            //3.1 learn about alert
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:");
            }

        }
    }
}