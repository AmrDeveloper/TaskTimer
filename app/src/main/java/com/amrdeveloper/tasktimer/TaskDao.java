package com.amrdeveloper.tasktimer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao extends BaseDao<Task> {

    @Query("SELECT * FROM tasks_table")
    LiveData<List<Task>> getTasks();

    @Query("DELETE FROM tasks_table")
    void deleteAll();
}
