package com.example.taskmanager;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks = new ArrayList<>();

    // Constructor
    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    // Called by MainActivity to update the adapter data
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    // ✅ Provide a method to retrieve a Task at a specific position
    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, dueDate;

        public TaskViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            dueDate = itemView.findViewById(R.id.txtDueDate);

            // On click, open TaskDetailActivity with the corresponding taskId
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    Task clickedTask = tasks.get(pos);
                    Intent intent = new Intent(itemView.getContext(), TaskDetailActivity.class);
                    intent.putExtra("taskId", clickedTask.id);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.title.setText(task.title);
        holder.dueDate.setText(task.dueDate);

        // Fade-in animation for each item (you can switch to slide_in_bottom if desired)
        Animation animation = AnimationUtils.loadAnimation(
                holder.itemView.getContext(),
                android.R.anim.fade_in
        );
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
