package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String title;

    public String description;

    @NonNull
    public String dueDate;

    public int priority; // 1 = Low, 2 = Medium, 3 = High

    // No-arg constructor (required by Room)
    public Task() {}

    // Additional constructor; annotated with @Ignore so Room ignores this constructor
    @Ignore
    public Task(@NonNull String title, String description, @NonNull String dueDate, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
    }
}
