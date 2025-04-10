package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editDueDate;
    private TaskDatabase db;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Set up back arrow on toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add/Edit Task");
        }

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDueDate = findViewById(R.id.editDueDate);
        Button saveBtn = findViewById(R.id.btnSave);

        db = TaskDatabase.getInstance(this);

        // Editing existing task
        if (getIntent().hasExtra("taskId")) {
            taskId = getIntent().getIntExtra("taskId", -1);
            Task task = db.taskDao().getTaskById(taskId);

            if (task != null) {
                editTitle.setText(task.title);
                editDescription.setText(task.description);
                editDueDate.setText(task.dueDate);
            }
        }

        // Save Button logic
        saveBtn.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String dueDate = editDueDate.getText().toString().trim();

            if (title.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(this, "Title and Due Date are required", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task();
            task.title = title;
            task.description = description;
            task.dueDate = dueDate;

            if (taskId != -1) {
                task.id = taskId;
                db.taskDao().update(task);
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
            } else {
                db.taskDao().insert(task);
                Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    // Handle the back arrow click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
