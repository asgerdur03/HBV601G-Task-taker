package com.example.hbv601G.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hbv601G.R;
import com.example.hbv601G.services.TaskService;
import com.example.hbv601G.networking.NetworkingService;
import com.google.gson.JsonObject;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailFragment extends Fragment {

    private EditText titleInput, noteInput, dueDateInput;
    private Button saveButton, archiveButton, deleteButton;
    private long taskId;
    private boolean isArchived;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleInput = view.findViewById(R.id.detailTaskTitle);
        noteInput = view.findViewById(R.id.detailTaskNote);
        dueDateInput = view.findViewById(R.id.detailTaskDueDate);
        saveButton = view.findViewById(R.id.buttonSaveEdit);
        archiveButton = view.findViewById(R.id.buttonArchive);
        deleteButton = view.findViewById(R.id.buttonDelete);

        taskId = getArguments() != null ? getArguments().getInt("taskId", -1) : -1;
        if (taskId == -1) {
            Toast.makeText(getContext(), "taskId vantar", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskService taskService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);

        taskService.getTaskById(taskId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject task = response.body();
                    if (task.has("taskName")) {
                        titleInput.setText(task.get("taskName").getAsString());
                    }

                    if (task.has("taskNote")) {
                        noteInput.setText(task.get("taskNote").getAsString());
                    }

                    if (task.has("dueDate")) {
                        dueDateInput.setText(task.get("dueDate").getAsString());
                    }
                    if (task.has("archived") && !task.get("archived").isJsonNull()) {
                        isArchived = task.get("archived").getAsBoolean();
                        archiveButton.setText(isArchived ? "Unarchive" : "Archive");
                    } else {
                        isArchived = false;
                        archiveButton.setText("Archive");
                    }

                 } else {
                Toast.makeText(getContext(), "Ekki tókst að sækja task", Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Villa við að sækja verkefni", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(v -> {
            JsonObject update = new JsonObject();
            update.addProperty("taskName", titleInput.getText().toString());
            update.addProperty("taskNote", noteInput.getText().toString());
            update.addProperty("dueDate", dueDateInput.getText().toString());

            taskService.updateTask(taskId, update).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Verkefni uppfært!", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(requireView());
                        navController.popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Villa við uppfærslu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Netvilla: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        archiveButton.setOnClickListener(v -> {
            JsonObject update = new JsonObject();
            update.addProperty("archived", !isArchived);

            taskService.updateTask(taskId, update).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        isArchived = !isArchived;
                        archiveButton.setText(isArchived ? "Unarchive" : "Archive");
                        Toast.makeText(getContext(), "Tókst að uppfæra archive status", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Gat ekki breytt archive", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(getContext(), "Villa við tengingu", Toast.LENGTH_SHORT).show();
                }
            });
        });


        deleteButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Delete task – kóði ekki útfærður enn", Toast.LENGTH_SHORT).show();
        });
    }
}
