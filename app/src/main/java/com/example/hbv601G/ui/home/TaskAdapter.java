package com.example.hbv601G.ui.home;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.entities.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnClickListener onClickListener;


    public interface OnClickListener{
        void onDeleteClick(Task task);
        void onArchiveClick(Task task);
        void onFavoriteClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnClickListener listener) {
        this.taskList = taskList;
        this.onClickListener = listener;
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
                task.getStatus() != null ? task.getStatus().toString() : "No status"
        );
        holder.priority.setText(
                task.getPriority() != null ? task.getPriority().toString() : "No priority"
        );
        holder.favorite.setText(
                task.getFavorite() ? "❤" : "not favorite"
        );

        holder.category.setText(
                task.getCategory().getCategoryName()
        );

        try {
            int parsedColor = Color.parseColor(task.getCategory().getCategoryColor());
            holder.cardView.setCardBackgroundColor(parsedColor);
        }catch (IllegalArgumentException e){
            holder.cardView.setCardBackgroundColor(Color.LTGRAY);
        }

        holder.archived.setText(
                task.getArchived() ? "Archived" : "Not Archived"
        );

        holder.deleteTask.setOnClickListener(v->{
            if (onClickListener != null) {
                onClickListener.onDeleteClick(task);
            }
        });

        holder.favoriteClick.setOnClickListener(v->{
            if (onClickListener != null) {
                onClickListener.onFavoriteClick(task);
            }
        });

        holder.archivedClick.setOnClickListener(v->{
            if (onClickListener != null) {
                onClickListener.onArchiveClick(task);
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }



    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, dueDate, taskNote, status, priority, category, archived, favorite;
        Button archivedClick, favoriteClick;

        ImageButton deleteTask;

        CardView cardView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            // todo: make the buttons pretty
            taskTitle = itemView.findViewById(R.id.taskTitle);
            dueDate = itemView.findViewById(R.id.taskDueDate);
            taskNote = itemView.findViewById(R.id.taskNote);
            status= itemView.findViewById(R.id.status);
            priority= itemView.findViewById(R.id.priority);
            category = itemView.findViewById(R.id.category);
            archived = itemView.findViewById(R.id.archived);
            favorite = itemView.findViewById(R.id.favorite);

            archivedClick = itemView.findViewById(R.id.archivedClick);
            favoriteClick = itemView.findViewById(R.id.favoriteClick);

            deleteTask = itemView.findViewById(R.id.deleteButtonTask);

            cardView = itemView.findViewById(R.id.taskCardRoot);

        }
    }
    public void updateTasks(List<Task> newTasks){
        taskList = newTasks;
        notifyDataSetChanged();
    }


}
