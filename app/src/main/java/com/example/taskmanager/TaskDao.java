package com.example.taskmanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM Task ORDER BY dueDate ASC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM Task WHERE id = :taskId")
    Task getTaskById(int taskId);
}
