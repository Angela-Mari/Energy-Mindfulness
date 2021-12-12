package com.Shawn.Angela;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateJournalActivity extends AppCompatActivity {

    static final String TAG = "CreateJournalActivity";

    EditText editTitleView;
    TextView dateAndTime;
    TextView currentBattery;


    Spinner moodSpinner;
    EditText journalEntryEditText;
    Button saveEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_journal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Log.d(TAG, "onCreate: in CreateJournalActivity");
        // Wire up the XML to this Activity
        editTitleView = findViewById(R.id.editTitleView);

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        dateAndTime = findViewById(R.id.dateAndTime);
        dateAndTime.setText(date);

        currentBattery = findViewById(R.id.currentBattery);
        moodSpinner = findViewById(R.id.spinner);

        // Spinner Mood Options
        journalEntryEditText = findViewById(R.id.journalEntryEditView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mood_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        moodSpinner.setAdapter(adapter);

        // get Battery Pct
        // get battery percentage
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);


        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

        Log.d(TAG, "onCreate: level " + level);
        currentBattery.setText("Current battery at " + String.valueOf(level));

        // Save Journal Entry
        saveEntry = findViewById(R.id.saveEntryButton);
        saveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: click save entry");
                Intent intent = new Intent(CreateJournalActivity.this, MainActivity.class);
                Log.d(TAG, "onClick: create title: " + editTitleView.getText());
                intent.putExtra("title", editTitleView.getText().toString());

                DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                String date = df.format(Calendar.getInstance().getTime());
                Log.d(TAG, "onClick: date " + date);
                intent.putExtra("date", date);
                df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                String time = df.format(Calendar.getInstance().getTime());
                intent.putExtra("time", time);
                intent.putExtra("mood", moodSpinner.getSelectedItem().toString());
                intent.putExtra("journalEntry", journalEntryEditText.getText().toString());
                intent.putExtra("battery", level);

                //save extras
                CreateJournalActivity.this.setResult(Activity.RESULT_OK, intent);
                //pop activity
                CreateJournalActivity.this.finish();
                return;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(CreateJournalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Helper Spinner Class
    class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener  {

        private String current;
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
            Log.d(TAG, "item selected: " +  parent.getItemAtPosition(pos));
            current = (String) parent.getItemAtPosition(pos);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }

        public String getCurrentItem(){
            return current;
        }
    }

}