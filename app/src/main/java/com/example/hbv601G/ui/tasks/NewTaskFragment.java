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
import androidx.fragment.app.Fragment;

import com.example.hbv601G.R;
import com.example.hbv601G.databinding.FragmentNewTaskBinding;
import com.example.hbv601G.entities.Category;
import com.example.hbv601G.entities.Task;
import com.example.hbv601G.entities.TaskPriority;
import com.example.hbv601G.entities.TaskStatus;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.CategoryService;
import com.example.hbv601G.services.TaskService;
import com.example.hbv601G.ui.category.CategoryFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTaskFragment extends Fragment {
    private FragmentNewTaskBinding binding;
    private EditText taskTitle, taskNote, dueDate;
    private Button saveTask;
    private Spinner categorySpinner, statusSpinner, prioritySpinner;

    private long selectedCategoryId = -1;
    private String selectedStatus = null;
    private String selectedPriority = null;

    private List<Category> categoryList;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentNewTaskBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        taskNote = root.findViewById(R.id.inputTaskNote);
        taskTitle = root.findViewById(R.id.inputTaskTitle);
        dueDate = root.findViewById(R.id.inputTaskDueDate);
        saveTask = root.findViewById(R.id.buttonSave);

        categorySpinner = root.findViewById(R.id.categorySpinner);
        statusSpinner = root.findViewById(R.id.statusSpinner);
        prioritySpinner = root.findViewById(R.id.prioritySpinner);

        loadCategories();
        loadEnumSpinner(prioritySpinner, TaskPriority.values(), false);
        loadEnumSpinner(statusSpinner, TaskStatus.values(), true);

        saveTask.setOnClickListener(v -> createTask());


        return root;
    }

    private void createTask(){
        String title = taskTitle.getText().toString().trim();
        String note = taskNote.getText().toString().trim();
        String due = dueDate.getText().toString().trim();

        if (title.isEmpty() || due.isEmpty() || selectedCategoryId == -1
                || selectedStatus == null || selectedPriority == null) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Task newTask = new Task(title, note, selectedStatus,selectedPriority, due, selectedCategoryId );

        TaskService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);

        service.createTask(newTask).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    Log.d("Task Debug", "Task created");
                    Toast.makeText(getContext(), "Task Created Successfully",Toast.LENGTH_SHORT).show();
                    taskNote.setText("");
                    taskTitle.setText("");
                    dueDate.setText("");
                }else {
                    Log.e("Task Debug", "Failed: "+ response.code());
                    Toast.makeText(getContext(), "Failed to create task",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Task Debug", "Error: " + t.getMessage());

            }
        });
    }

    private void loadCategories() {
        CategoryService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(CategoryService.class);

        service.getCategories().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    JsonObject json = response.body();

                    JsonArray categoryArray = json.getAsJsonArray("categories");

                    List<Category> parsedList = new ArrayList<>();
                    for (JsonElement element: categoryArray){
                        Category category = new Gson().fromJson(element, Category.class);
                        parsedList.add(category);
                        Log.d("Task Debug", "Added :" + category.toString());
                    }
                    categoryList = parsedList;

                    List<String> categoryNames = new ArrayList<>();
                    for (Category category: categoryList){
                        categoryNames.add(category.getCategoryName());
                    }
                    setupSpinner(categorySpinner, categoryNames, (position) ->{
                        selectedCategoryId = categoryList.get(position).getId();
                    });

                } else{
                    Log.e("Task Debug", "Failed code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Task Debug", "Error: "+ t.getMessage());
            }
        });
    }


    private <T extends Enum<T>> void loadEnumSpinner(Spinner spinner, T[] values, boolean isStatus) {
        List<String> items = new ArrayList<>();

        for (T value : values) {
            items.add(value.name());
        }
        setupSpinner(spinner, items, (position) -> {
            if (isStatus){
                selectedStatus = items.get(position);
            }else{
                selectedPriority = items.get(position);
            }
        });
    }

    private void setupSpinner(Spinner spinner, List<String> items, OnItemSelected onItemSelected ){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onItemSelected.onItemSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    private interface OnItemSelected{
        void onItemSelected(int position);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
