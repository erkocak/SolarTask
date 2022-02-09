package com.example.solartask.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habit")
    List<Habit> getAllHabits();

    @Insert
    void insertAll(Habit... habits);

    @Update
    int updateHabit(Habit habit);
}
