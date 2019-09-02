package com.amrdeveloper.tasktimer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel{

    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public void insert(Task task){
        repository.insert(task);
    }

    public void update(Task task){
        repository.update(task);
    }

    public void updateTasks(Task...task){
        repository.updateTasks(task);
    }

    public void delete(Task task){
        repository.delete(task);
    }

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }
}
