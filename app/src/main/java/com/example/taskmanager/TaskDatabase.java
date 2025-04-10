package com.example.taskmanager;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Update version to 2 and disable schema export.
@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase instance;

    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration() // Wipes and rebuilds the DB on schema changes.
                    .allowMainThreadQueries()         // Only for development/testing.
                    .build();
        }
        return instance;
    }
}
