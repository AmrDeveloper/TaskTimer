package com.amrdeveloper.tasktimer;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

public interface BaseDao <T> {

    @Insert
    void insert(T task);

    @Update
    void update(T task);

    @Update
    void updateTasks(T...tasks);

    @Delete
    void delete(T task);

}
