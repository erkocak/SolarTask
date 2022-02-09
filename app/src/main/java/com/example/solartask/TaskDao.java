package com.example.solartask;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.solartask.db.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task")
    List<com.example.solartask.db.Task> getAllTasks();

    @Insert
    void insertAll(com.example.solartask.db.Task... tasks);

    @Update
    int updateTask(com.example.solartask.db.Task task);

    @Delete
    void deleteTasks(Task... tasks);
}
