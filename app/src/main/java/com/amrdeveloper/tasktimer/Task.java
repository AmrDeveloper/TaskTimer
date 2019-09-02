package com.amrdeveloper.tasktimer;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks_table")
public class Task implements Observer {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private long timeInSec;
    private boolean isRunning;

    public Task(String title, long timeInSec, boolean isRunning) {
        this.title = title;
        this.timeInSec = timeInSec;
        this.isRunning = isRunning;
    }

    public int getTaskId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimeInSec() {
        return timeInSec;
    }

    public void setTimeInSec(long timeInSec) {
        this.timeInSec = timeInSec;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void observe() {
        this.timeInSec++;
    }
}
