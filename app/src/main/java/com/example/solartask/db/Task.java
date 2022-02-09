package com.example.solartask.db;

import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity
public class Task {
    public Task(String description, Integer importance, boolean done, Date dueDate, String category) {
        this.description = description;
        this.done = done;
        this.importance = importance;
        this.dueDate = dueDate;
        this.category = category;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public Integer importance;

    public String description;

    public boolean isRecurring;

    @TypeConverters(Converters.class)
    public Date dueDate;

    public boolean done;

    public String category;

    public String getCategory() {
        return category;
    }

    public String toString(){
        return description + " " + dueDate;
    }

    public void complete() {
        this.done = true;
    }

    public Integer getImportance() {
        return importance;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }
}