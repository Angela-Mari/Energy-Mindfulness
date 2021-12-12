package com.Shawn.Angela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class JournalOpenHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "journaldatabase.db";
    static final int DATABASE_VERSION = 1;

    static final String ENTRIES_TABLE = "tableEntries";
    static final String ID = "_id";
    static final String TITLE = "title";
    static final String DATE = "date";
    static final String TIME = "time";
    static final String JOURNAL_ENTRY = "journalEntry";
    static final String MOOD = "mood";
    static final String BATTERY_PERCENTAGE = "batteryPercentage";

    public JournalOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // we get the database reference here
        // structured query language
        String sqlCreate = "CREATE TABLE " + ENTRIES_TABLE +
                "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DATE + " DATE NOT NULL, " +
                TIME + " TEXT, " +
                JOURNAL_ENTRY + " TEXT, " +
                MOOD + " TEXT, " +
                BATTERY_PERCENTAGE + " INTEGER)";
        Log.d(MainActivity.TAG, "onCreate: " + sqlCreate);
        // execute the statement
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertJournal(Journal journalEntry){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, journalEntry.getTitle());
        contentValues.put(DATE, journalEntry.getDate());
        contentValues.put(TIME, journalEntry.getTime());
        contentValues.put(JOURNAL_ENTRY, journalEntry.getJournalEntry());
        contentValues.put(MOOD, journalEntry.getMood());
        contentValues.put(BATTERY_PERCENTAGE, (int) journalEntry.getBatteryPercentage());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(ENTRIES_TABLE, null, contentValues);
        db.close(); // don't forget to close!
    }

    // helper method for returning cursor
    public Cursor getSelectedCursor(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ENTRIES_TABLE, new String[]{ID, TITLE, DATE, TIME, JOURNAL_ENTRY, MOOD, BATTERY_PERCENTAGE}, null, null, null, null, null);
        return cursor;
    }

    public List<Journal> getSelectAllJournals(){
        List<Journal> journalEntries = new ArrayList<>();
        Cursor cursor = getSelectedCursor();
        // cursor starts one before the first one
        while (cursor.moveToNext()){ // when false there are no more records
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String journal = cursor.getString(4);
            String mood = cursor.getString(5);
            int battery = cursor.getInt(6);
            Journal journalEntry = new Journal(id, title, date, time, journal, mood, battery);
            journalEntries.add(journalEntry);
        }
        return journalEntries;
    }

    public Cursor getDateCursor(String date){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(ENTRIES_TABLE, new String[]{ID, TITLE, DATE, TIME, JOURNAL_ENTRY, MOOD, BATTERY_PERCENTAGE}, "DATE =?", new String[]{""+date}, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public List<Journal> getSelectJournalsByDate(String searchDate){
        List<Journal> journalEntries = new ArrayList<>();
        Cursor cursor = getDateCursor(searchDate);
        // cursor starts one before the first one
        while (cursor.moveToNext()){ // when false there are no more records
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String date = cursor.getString(2);
            String time = cursor.getString(3);
            String journal = cursor.getString(4);
            String mood = cursor.getString(5);
            int battery = cursor.getInt(6);
            Journal journalEntry = new Journal(id, title, date, time, journal, mood, battery);
            journalEntries.add(journalEntry);
        }
        return journalEntries;
    }

    public void deleteALLJournals(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ENTRIES_TABLE, null, null);
        db.close();
    }



}
