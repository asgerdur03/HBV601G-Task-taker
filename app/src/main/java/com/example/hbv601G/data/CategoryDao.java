package com.example.hbv601G.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.hbv601G.entities.Category;

import java.util.List;


@Dao
public interface CategoryDao {

    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Insert
    void insert(Category category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categories);




}
