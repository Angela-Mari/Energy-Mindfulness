package com.Shawn.Angela;

public class Journal {
    private int id;
    private String title;
    private String date;
    private String time;
    private String journalEntry;
    private String mood;
    private int batteryPercentage;

    public Journal(String title, String date, String time, String journalEntry, String mood, int batteryPercentage) {
        this.id = -1;
        this.title = title;
        this.date = date;
        this.time = time;
        this.journalEntry = journalEntry;
        this.mood = mood;
        this.batteryPercentage = batteryPercentage;
    }

    public Journal(int id, String title, String date, String time, String journalEntry, String mood, int batteryPercentage) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time = time;
        this.journalEntry = journalEntry;
        this.mood = mood;
        this.batteryPercentage = batteryPercentage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getJournalEntry() {
        return journalEntry;
    }

    public String getMood() {
        return mood;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", journalEntry='" + journalEntry + '\'' +
                ", mood='" + mood + '\'' +
                ", batteryPercentage=" + batteryPercentage +
                '}';
    }
}


