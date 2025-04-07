package com.example.hbv601G.ui.home;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.data.DummyGognVinnsla;
import com.example.hbv601G.entities.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.taskTitle.setText(task.getTaskName());

        holder.dueDate.setText(
                task.getDueDate() != null ? task.getDueDate().toString() : "No date"
        );

        holder.taskNote.setText(task.getTaskNote());
        holder.status.setText(
                task.getTaskStatus() != null ? task.getTaskStatus().toString() : "No status"
        );
        holder.priority.setText(
                task.getTaskPriority() != null ? task.getTaskPriority().toString() : "No priority"
        );

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, dueDate, taskNote, status, priority, category;
        CheckBox archived, favorite;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            dueDate = itemView.findViewById(R.id.taskDueDate);
            taskNote = itemView.findViewById(R.id.taskNote);
            status= itemView.findViewById(R.id.status);
            priority= itemView.findViewById(R.id.priority);
            category = itemView.findViewById(R.id.category);

            archived = itemView.findViewById(R.id.archived);
            favorite = itemView.findViewById(R.id.favorite);
        }
    }
    public void updateTasks(List<Task> newTasks){
        taskList = newTasks;
        notifyDataSetChanged();
    }


}
