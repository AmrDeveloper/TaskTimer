package com.amrdeveloper.tasktimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
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
            mTasksAdapter.submitList(taskList);
        });

        setupItemTouchHelper();
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

    private void setupItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int itemPosition = viewHolder.getAdapterPosition();
                Task task = mTasksAdapter.getTaskAt(itemPosition);

                //Remove this task from ObserverManager to stop schedule it
                mObserverManager.removeObserver(task);

                //Remove this task from Database
                mTaskViewModel.delete(task);
            }
        }).attachToRecyclerView(mTasksList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAllMenu:
                mTaskViewModel.deleteAll();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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

                    mTasksAdapter.notifyRunningItems();
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<Task> currentList = mTasksAdapter.getCurrentList();
        mTaskViewModel.updateTasks(currentList.toArray(new Task[currentList.size()]));
    }
}
