package com.example.solartask.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Insert
    void insertAll(Category... categories);

    @Update
    int updateCategory(Category category);
}
