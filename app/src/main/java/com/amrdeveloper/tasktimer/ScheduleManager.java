package com.amrdeveloper.tasktimer;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleManager {

    private static final int DELAY = 1000;
    private static final int PERIOD = 1000;

    private volatile boolean isTimerRunning;
    private Timer timer;

    public void runTimer(TimerTask timerTask) {
        if (!isTimerRunning) {
            timer = new Timer();
            isTimerRunning = true;
        }
        timer.scheduleAtFixedRate(timerTask, DELAY, PERIOD);
    }

    public void stopTimer() {
        if(isTimerRunning){
            isTimerRunning = false;
            timer.cancel();
        }
    }

    public Timer getCurrentTimer(){
        return timer;
    }

    public boolean getTimerState() {
        return isTimerRunning;
    }
}
