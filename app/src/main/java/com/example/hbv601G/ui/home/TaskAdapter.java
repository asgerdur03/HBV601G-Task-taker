package com.example.hbv601G.ui.home;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;




import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hbv601G.R;
import com.example.hbv601G.entities.Task;
import com.example.hbv601G.services.TaskService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnClickListener onClickListener;
    private TaskService taskService;


    public interface OnClickListener{
        void onDeleteClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnClickListener listener, TaskService taskService) {
        this.taskList = taskList;
        this.onClickListener = listener;
    this.taskService = taskService;
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
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("taskId", (int) task.getId());


            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_home_to_taskDetailFragment, bundle);
        });


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

        if (task.getCategory() != null) {
            holder.category.setText(task.getCategory().getCategoryName());

            try {
                int parsedColor = Color.parseColor(task.getCategory().getCategoryColor());
                holder.cardView.setCardBackgroundColor(parsedColor);
            } catch (IllegalArgumentException e) {
                holder.cardView.setCardBackgroundColor(Color.LTGRAY);
            }

        } else {
            holder.category.setText("No Category");
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
            taskService.toggleFavorite(task.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        task.setFavorite(!task.getFavorite());

                        notifyItemChanged(holder.getBindingAdapterPosition());
                        Log.d("Favorite", "Toggle succesful");
                    }
                    else {
                        Log.d("Favorite", "Toggle Failed: " + response.code());
                    }

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Toggle", "Error: " + t.getMessage());

                }
            });

        });

        holder.archivedClick.setOnClickListener(v->{
            taskService.toggleArchive(task.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()){
                        task.setArchived(!task.getArchived());

                        notifyItemChanged(holder.getBindingAdapterPosition());
                        Log.d("Archive", "Toggle succesful");
                    }
                    else {
                        Log.d("Archive", "Toggle Failed: "+ response.code());
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Toggle", "Error: " + t.getMessage());

                }
            });

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
