package com.example.solartask.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Insert
    void insertAll(Task... tasks);

    @Update
    int updateTask(Task task);

    @Delete
    void deleteTasks(Task... tasks);
}
