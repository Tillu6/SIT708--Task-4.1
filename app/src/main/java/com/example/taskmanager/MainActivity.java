package com.example.taskmanager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter adapter;
    private ImageView backgroundImageView;
    private Button btnAddTask;
    private TaskDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backgroundImageView = findViewById(R.id.backgroundImageView);
        btnAddTask = findViewById(R.id.btnAddTask);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        // Set up a static gradient background
        int[] colors = {
                Color.parseColor("#2E3192"),
                Color.parseColor("#1BFFFF"),
                Color.parseColor("#4A00E0"),
                Color.parseColor("#FA8BFF")
        };
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.BL_TR,
                colors
        );
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        backgroundImageView.setImageDrawable(gradientDrawable);

        // Initialize DB and Adapter
        db = TaskDatabase.getInstance(this);
        adapter = new TaskAdapter(db.taskDao().getAllTasks());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Full-width Add Task Button click opens the Add/Edit Task activity
        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });

        // Attach swipe-to-delete functionality
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; // We are not handling onMove
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Get position and corresponding task from adapter
                int position = viewHolder.getAdapterPosition();
                Task task = adapter.getTaskAt(position);

                // Show confirmation dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this task?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.taskDao().delete(task);
                            refreshTaskList();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            adapter.notifyItemChanged(position); // Undo swipe
                        })
                        .setCancelable(false)
                        .show();
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }

    // Method to refresh the RecyclerView with the latest tasks
    private void refreshTaskList() {
        List<Task> updatedTasks = db.taskDao().getAllTasks();
        adapter.setTasks(updatedTasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTaskList();
    }
}
