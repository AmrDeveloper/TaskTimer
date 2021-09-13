package com.amrdeveloper.tasktimer;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Task.class, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao tasksDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    TaskDatabase.class,
                    "task_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new FirstInsertAsyncTask(instance).execute();
        }
    };

    private static class FirstInsertAsyncTask extends AsyncTask<Void, Void, Void> {

        private final TaskDao taskDao;

        private FirstInsertAsyncTask(TaskDatabase taskDao) {
            this.taskDao = taskDao.tasksDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.insert(new Task("Compiler Design", 0, false));
            taskDao.insert(new Task("Web Design", 0, false));
            taskDao.insert(new Task("App Design", 0, false));
            return null;
        }
    }
}
