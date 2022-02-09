package com.example.solartask.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity
public class Habit {
    public Habit(String name, boolean done) {
        this.name = name;
        this.done = done;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public boolean done;

    public void complete() {
        this.done = true;
    }
}