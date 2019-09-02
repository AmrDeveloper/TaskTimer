package com.amrdeveloper.tasktimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mTasksList;
    private FloatingActionButton mNewTaskFab;

    private TaskAdapter mTasksAdapter;
    private TaskViewModel mTaskViewModel;

    private ScheduleManager mScheduleManager;
    private ObserverManager mObserverManager;

    private static final String TAG = "MainActivity";
    private static final int ADD_TASK_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayoutViews();
        setupRecyclerView();

        mScheduleManager = new ScheduleManager();
        mObserverManager = new ObserverManager();
        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        mObserverManager.setOnChangeListener(onStateChangeListener);
        mTaskViewModel.getAllTasks().observe(this, taskList -> {
            mTasksAdapter.updateAdapterData(taskList);
        });
    }

    private void initLayoutViews(){
        mTasksList = findViewById(R.id.tasksList);
        mNewTaskFab = findViewById(R.id.newTaskFab);

        mNewTaskFab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,AddTaskActivity.class);
            startActivityForResult(intent,ADD_TASK_REQUEST);
        });
    }

    private void setupRecyclerView(){
        mTasksAdapter = new TaskAdapter();
        mTasksAdapter.setOnTaskClickListener(onTaskClickListener);

        mTasksList.setLayoutManager(new LinearLayoutManager(this));
        mTasksList.setHasFixedSize(true);
        mTasksList.setKeepScreenOn(true);
        mTasksList.setItemAnimator(null);
        mTasksList.setAdapter(mTasksAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
           String title = data.getStringExtra(Constant.EXTRA_TITLE);

           Task task = new Task(title,0,false);

           mTaskViewModel.insert(task);
        }
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
            mScheduleManager.stopTaskSchedule();
        }else{
            Log.d(TAG,"Run Timer");
            mScheduleManager.startTaskSchedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG,"Notify Observers");
                    mObserverManager.notifyObservers();
                    //TODO : can improve notify performance by re implement it
                    //mTaskViewModel.updateTasks((Task) mObserverManager.getObserverList());
                    mTasksAdapter.notifyRunningItems();
                }
            });
        }
    };
}
