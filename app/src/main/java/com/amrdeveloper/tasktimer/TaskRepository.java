package com.amrdeveloper.tasktimer;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.tasksDao();
        allTasks = taskDao.getTasks();
    }

    public void insert(Task task){
        new InsertLoadAsyncTask(taskDao).execute(task);
    }

    public void update(Task task){
        new UpdateAsyncTask(taskDao).execute(task);
    }

    public void updateTasks(Task...tasks){
        new UpdateTasksAsyncTask(taskDao).execute(tasks);
    }


    public void delete(Task task){
        new DeleteAsyncTask(taskDao).execute(task);
    }

    public void deleteAll(){
       new DeleteAllAsyncTask(taskDao).execute();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    private static class InsertLoadAsyncTask extends AsyncTask<Task,Void,Void>{

        private TaskDao taskDao;

        private InsertLoadAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Task,Void,Void>{

        private TaskDao taskDao;

        private UpdateAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class UpdateTasksAsyncTask extends AsyncTask<Task,Void,Void>{

        private TaskDao taskDao;

        private UpdateTasksAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.updateTasks(tasks);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{

        private TaskDao taskDao;

        private DeleteAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{

        private TaskDao taskDao;

        private DeleteAllAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void...v) {
            taskDao.deleteAll();
            return null;
        }
    }
}
