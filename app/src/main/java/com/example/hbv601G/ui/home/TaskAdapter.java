package com.example.hbv601G.ui.home;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.data.DummyGognVinnsla;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<DummyGognVinnsla.Task> taskList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DummyGognVinnsla.Task task);
    }

    public TaskAdapter(List<DummyGognVinnsla.Task> taskList) {
        this.taskList = taskList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
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
        DummyGognVinnsla.Task task = taskList.get(position);
        holder.taskTitle.setText(task.taskName);
        holder.dueDate.setText("Due: " + task.dueDate);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(task);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, dueDate;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            dueDate = itemView.findViewById(R.id.taskDueDate);
        }
    }
}
