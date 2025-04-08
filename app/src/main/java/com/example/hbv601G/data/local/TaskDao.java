package com.example.hbv601G.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hbv601G.entities.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertAll(List<Task> tasks);

    @Query("SELECT * FROM tasks WHERE userId = :userId")
    List<Task> getTasksForUser(int userId);

    @Query("DELETE FROM tasks WHERE userId = :userId")
    void deleteTasksForUser(int userId);
}
