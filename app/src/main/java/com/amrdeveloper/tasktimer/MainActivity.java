package com.amrdeveloper.tasktimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTasksList;
    private TaskAdapter mTasksAdapter;

    private ScheduleManager mScheduleManager;
    private ObserverManager mObserverManager;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayoutViews();
        setupRecyclerView();

        mScheduleManager = new ScheduleManager();
        mObserverManager = new ObserverManager();
        mObserverManager.setOnChangeListener(onStateChangeListener);
    }

    private void initLayoutViews(){
        mTasksList = findViewById(R.id.tasksList);
    }

    private void setupRecyclerView(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Developer Compiler",60,false));
        tasks.add(new Task("Developer OS",3600,false));
        tasks.add(new Task("Developer Web app",1000,false));
        tasks.add(new Task("Developer Mob App",500,false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTasksList.setLayoutManager(layoutManager);

        mTasksList.setHasFixedSize(true);

        mTasksAdapter = new TaskAdapter(tasks);
        mTasksAdapter.setOnTaskClickListener(onTaskClickListener);
        mTasksList.setAdapter(mTasksAdapter);
    }

    private TaskAdapter.OnTaskClickListener onTaskClickListener = task -> {
        if(task.isRunning()){
            mObserverManager.removeObserver(task);
            task.setRunning(false);
            Log.d(TAG,"Remove from Observer Manager");
        }else{
            mObserverManager.addObserver(task);
            task.setRunning(true);
            Log.d(TAG,"Add to Observer Manager");
        }
    };

    private ObserverManager.onStateChangeListener onStateChangeListener = isEmpty -> {
        if(isEmpty){
            Log.d(TAG,"Stop Timer");
            mScheduleManager.stopTimer();

        }else{
            Log.d(TAG,"Run Timer");
            mScheduleManager.runTimer(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG,"Notify Observers");
                    mObserverManager.notifyObservers();
                    mTasksAdapter.notifyRunningItems();
                }
            });
        }
    };
}
