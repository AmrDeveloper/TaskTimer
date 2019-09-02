package com.amrdeveloper.tasktimer;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager {

    private volatile boolean shouldTimerRunning;
    private List<Observer> observerList;

    @FunctionalInterface
    public interface onStateChangeListener{
        void onChange(boolean isEmpty);
    }

    private onStateChangeListener mOnStateChangeListener;

    public ObserverManager() {
        this.observerList = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        if (observerList.size() == 0) {
            shouldTimerRunning = true;
            if(mOnStateChangeListener != null){
                mOnStateChangeListener.onChange(false);
            }
        }
        observerList.add(observer);
    }

    public void removeObserver(Observer observer) {
        observerList.remove(observer);
        if (observerList.size() == 0) {
            shouldTimerRunning = false;
            if(mOnStateChangeListener != null){
                mOnStateChangeListener.onChange(true);
            }
        }
    }

    public void notifyObservers() {
        for(Observer observer : observerList){
            observer.observe();
        }
    }

    public boolean getRunState() {
        return shouldTimerRunning;
    }

    public List<Observer> getObserverList(){
        return observerList;
    }

    public void setOnChangeListener(onStateChangeListener onChangeListener){
        mOnStateChangeListener = onChangeListener;
    }
}
