package com.amrdeveloper.tasktimer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getTasks();

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Update
    void updateTasks(Task...tasks);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM tasks_table")
    void deleteAll();
}
