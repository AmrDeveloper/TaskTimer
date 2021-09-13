package com.amrdeveloper.tasktimer;

import java.util.ArrayList;
import java.util.List;

public class ObserverManager implements Observable {

    private final List<Observer> observerList;

    @FunctionalInterface
    public interface OnStateChangeListener {
        void onChange(boolean isEmpty);
    }

    private OnStateChangeListener mOnStateChangeListener;

    public ObserverManager() {
        this.observerList = new ArrayList<>();
    }

    @Override
    public void addObserver(Observer observer) {
        if (observerList.isEmpty() && mOnStateChangeListener != null) {
            mOnStateChangeListener.onChange(false);
        }
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observerList.remove(observer);
        if (observerList.isEmpty() && mOnStateChangeListener != null) {
            mOnStateChangeListener.onChange(true);
        }
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }

    public void setOnChangeListener(OnStateChangeListener onChangeListener) {
        mOnStateChangeListener = onChangeListener;
    }
}
