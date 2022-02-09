package com.example.solartask.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Task.class, Category.class, Habit.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public static Database INSTANCE;

    public static Database getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Database.class, "appDB").allowMainThreadQueries().build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract TaskDao taskDao();

    public abstract CategoryDao categoryDao();

    public abstract HabitDao habitDao();
}
