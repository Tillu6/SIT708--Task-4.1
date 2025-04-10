package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TaskDetailActivity extends AppCompatActivity {

    private Task task;
    private TaskDatabase db;
    private TextView titleView, descriptionView, dueDateView, priorityView;
    private Button editBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Enable back button on the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Task Details");
        }

        // Bind views from your layout (make sure your XML has these IDs)
        titleView = findViewById(R.id.textTitle);
        descriptionView = findViewById(R.id.textDescription);
        dueDateView = findViewById(R.id.textDueDate);
        priorityView = findViewById(R.id.textPriority);
        editBtn = findViewById(R.id.btnEdit);
        deleteBtn = findViewById(R.id.btnDelete);

        db = TaskDatabase.getInstance(this);

        // Retrieve the taskId from the intent extras
        int taskId = getIntent().getIntExtra("taskId", -1);
        if (taskId != -1) {
            task = db.taskDao().getTaskById(taskId);
            if (task != null) {
                titleView.setText(task.title);
                descriptionView.setText(task.description);
                dueDateView.setText(task.dueDate);
                String priorityText;
                switch (task.priority) {
                    case 1:
                        priorityText = "Low";
                        break;
                    case 2:
                        priorityText = "Medium";
                        break;
                    case 3:
                        priorityText = "High";
                        break;
                    default:
                        priorityText = "Unknown";
                        break;
                }
                priorityView.setText("Priority: " + priorityText);
            }
        }

        // Set up the edit button: launch AddEditTaskActivity to edit the current task.
        editBtn.setOnClickListener(v -> {
            if (task != null) {
                // Replace any previous reference to EditTaskActivity with your AddEditTaskActivity.
                Intent intent = new Intent(TaskDetailActivity.this, AddEditTaskActivity.class);
                intent.putExtra("taskId", task.id);
                startActivity(intent);
                finish();
            }
        });

        // Set up the delete button: delete the current task from the database.
        deleteBtn.setOnClickListener(v -> {
            if (task != null) {
                db.taskDao().delete(task);
                Toast.makeText(TaskDetailActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    // Handle action bar back/up button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Go back to the previous screen
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
