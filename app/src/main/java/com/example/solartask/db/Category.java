package com.example.solartask.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    public Category(String name) {
        this.name = name;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
}