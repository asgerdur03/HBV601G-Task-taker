package com.example.hbv601G.ui.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.NavController;

import com.example.hbv601G.R;
import com.example.hbv601G.entities.Category;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.CategoryService;
import com.example.hbv601G.services.TaskService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailFragment extends Fragment {
    // todo: reomove delete and archive button, add priority and status bar

    private EditText titleInput, noteInput, dueDateInput;
    private Button saveButton;

    private Spinner editCategorySpinner, editStatusSpinner, editPrioritySpinner;
    private long taskId;
    private boolean isArchived;

    private String oldTitle = "";
    private String oldNote = "";
    private String oldDueDate = "";

    private String oldPriority = "";
    private String oldStatus = "";
    private long oldCategoryId = -1;

    private List<Category> categoryList = new ArrayList<>();


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

        editCategorySpinner = view.findViewById(R.id.editCategorySpinner);
        editStatusSpinner = view.findViewById(R.id.editStatusSpinner);
        editPrioritySpinner = view.findViewById(R.id.editPrioritySpinner);

        taskId = getArguments() != null ? getArguments().getInt("taskId", -1) : -1;
        if (taskId == -1) {
            Toast.makeText(getContext(), "taskId vantar", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskService taskService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);
        CategoryService categoryService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(CategoryService.class);

        taskService.getTaskById(taskId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("TASK_DETAIL", "API response body: " + response.body().toString());

                    JsonObject root = response.body();
                    if (root.has("task") && root.get("task").isJsonObject()) {
                        JsonObject task = root.getAsJsonObject("task");

                        if (task.has("taskName") && !task.get("taskName").isJsonNull()) {
                            oldTitle = task.get("taskName").getAsString();
                            titleInput.setText(oldTitle);
                        }
                        if (task.has("taskNote") && !task.get("taskNote").isJsonNull()) {
                            oldNote = task.get("taskNote").getAsString();
                            noteInput.setText(oldNote);
                        }
                        if (task.has("dueDate") && !task.get("dueDate").isJsonNull()) {
                            oldDueDate = task.get("dueDate").getAsString();
                            dueDateInput.setText(oldDueDate);
                        }
                        if (task.has("priority") && !task.get("priority").isJsonNull()) {
                            oldPriority = task.get("priority").getAsString();
                        }
                        if (task.has("status") && !task.get("status").isJsonNull()) {
                            oldStatus = task.get("status").getAsString();
                        }
                        if (task.has("category") && task.get("category").isJsonObject()) {
                            oldCategoryId = task.get("category").getAsJsonObject().get("id").getAsLong();
                        }

                        setupSpinner(editStatusSpinner, new String[]{"TODO", "IN_PROGRESS", "DONE"}, oldStatus, value -> oldStatus = value);
                        setupSpinner(editPrioritySpinner, new String[]{"LOW", "MEDIUM", "HIGH"}, oldPriority, value -> oldPriority = value);

                        categoryService.getCategories().enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    JsonArray categoryArray = response.body().getAsJsonArray("categories");
                                    categoryList.clear();
                                    List<String> categoryNames = new ArrayList<>();

                                    for (JsonElement element : categoryArray) {
                                        Category category = new Gson().fromJson(element, Category.class);
                                        categoryList.add(category);
                                        categoryNames.add(category.getCategoryName());
                                    }

                                    int index = 0;
                                    for (int i = 0; i < categoryList.size(); i++) {
                                        if (categoryList.get(i).getId() == oldCategoryId) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    editCategorySpinner.setAdapter(adapter);
                                    editCategorySpinner.setSelection(index);

                                    editCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            oldCategoryId = categoryList.get(position).getId();
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {}
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                Log.e("CategoryLoad", "Error loading categories: " + t.getMessage());
                            }
                        });


                    } else {
                        Toast.makeText(getContext(), "Task fannst ekki í JSON svari", Toast.LENGTH_SHORT).show();
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
            String newTitle = titleInput.getText().toString().trim();
            String newNote = noteInput.getText().toString().trim();
            String newDueDate = dueDateInput.getText().toString().trim();

            JsonObject update = new JsonObject();
            update.addProperty("taskName", newTitle.isEmpty() ? oldTitle : newTitle);
            update.addProperty("taskNote", newNote.isEmpty() ? oldNote : newNote);
            update.addProperty("dueDate", newDueDate.isEmpty() ? oldDueDate : newDueDate);

            update.addProperty("priority", oldPriority);
            update.addProperty("status", oldStatus);
            update.addProperty("categoryId", oldCategoryId);

            taskService.updateTask(taskId, update).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Verkefni uppfært!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).popBackStack();
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


    }

    private void setupSpinner(Spinner spinner, String[] values, String currentValue, OnItemSelected callback) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        int currentIndex = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(currentValue)) {
                currentIndex = i;
                break;
            }
        }
        spinner.setSelection(currentIndex);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callback.onItemSelected(values[position]);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    interface OnItemSelected {
        void onItemSelected(String value);
    }

}
