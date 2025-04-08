package com.example.hbv601G.ui.home;
import static android.text.format.DateUtils.isToday;

import com.example.hbv601G.R;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import android.widget.Spinner;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hbv601G.databinding.FragmentHomeBinding;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.hbv601G.data.DummyGognVinnsla;
import com.example.hbv601G.entities.Task;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.TaskService;
import com.example.hbv601G.ui.tasks.TaskDetailFragment;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ArrayAdapter;
import java.util.Arrays;
import java.util.Set;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> allTasks = new ArrayList<>(); // store all unfiltered tasks

    private List<Task> taskList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        TaskService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewTasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        taskAdapter = new TaskAdapter(taskList, task -> deleteTask(task), service);

        recyclerView.setAdapter(taskAdapter);



        ChipGroup chipGroupDueDate = binding.chipGroupDueDate;
        ToggleButton toggleFavorites = binding.toggleFavorites;
        Spinner spinnerCategory = binding.spinnerCategory;
        Spinner spinnerPriority = binding.spinnerPriority;
        Spinner spinnerStatus = binding.spinnerStatus;

        spinnerCategory.setOnItemSelectedListener(new SimpleAdapterFilter());
        spinnerPriority.setOnItemSelectedListener(new SimpleAdapterFilter());
        spinnerStatus.setOnItemSelectedListener(new SimpleAdapterFilter());

        toggleFavorites.setOnCheckedChangeListener((btn, isChecked) -> filterTasks());
        chipGroupDueDate.setOnCheckedStateChangeListener((group, checkedIds) -> filterTasks());

        // Priority Spinner
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Any", "High", "Medium", "Low")
        );
        binding.spinnerPriority.setAdapter(priorityAdapter);

        // Status Spinner
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Any", "Pending", "Completed", "In Progress", "Cancelled")
        );
        binding.spinnerStatus.setAdapter(statusAdapter);

        Set<String> categoryNames = new HashSet<>();
        categoryNames.add("Any");

        for (Task task : allTasks) {
            if (task.getCategory() != null && task.getCategory().getCategoryName() != null) {
                categoryNames.add(task.getCategory().getCategoryName());
            }
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(categoryNames)
        );
        binding.spinnerCategory.setAdapter(categoryAdapter);


        fetchTasks();


        return root;
    }

    private void deleteTask(Task task){
        TaskService service = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);

        service.deleteTask(task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    taskList.remove(task);
                    taskAdapter.updateTasks(taskList);
                }
                else {
                    Log.e("Task Debug", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Task Debug", "Error: " + t.getMessage());
            }
        });
    }


    private void fetchTasks(){
        TaskService taskService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);

        taskService.getTasks().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    JsonObject json = response.body();

                    JsonArray taskArray = json.getAsJsonArray("tasks");

                    List<Task> tasks = new ArrayList<>();

                    for (JsonElement element: taskArray){
                        Task task = new Gson().fromJson(element, Task.class);
                        tasks.add(task);
                    }

                    Log.d("taskDebug", "size: " + tasks.size());

                  // todo: skoða betur eftir pull
                    taskList = tasks;
                    taskAdapter.updateTasks(tasks);

                    allTasks = tasks;
                    filterTasks();


                }else{
                    Log.e("Tasks", "Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private class SimpleAdapterFilter implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
            filterTasks();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            filterTasks();
        }
    }

    private void filterTasks() {
        List<Task> filtered = new ArrayList<>();

        String selectedCategory = binding.spinnerCategory.getSelectedItem() != null ?
                binding.spinnerCategory.getSelectedItem().toString() : "Any";

        String selectedPriority = binding.spinnerPriority.getSelectedItem() != null ?
                binding.spinnerPriority.getSelectedItem().toString() : "Any";

        String selectedStatus = binding.spinnerStatus.getSelectedItem() != null ?
                binding.spinnerStatus.getSelectedItem().toString() : "Any";

        boolean onlyFavorites = binding.toggleFavorites.isChecked();

        int selectedChipId = binding.chipGroupDueDate.getCheckedChipId();
        String dueDateFilter = null;
        if (selectedChipId == R.id.chipToday) dueDateFilter = "Today";
        else if (selectedChipId == R.id.chipThisWeek) dueDateFilter = "This Week";
        else if (selectedChipId == R.id.chipOverdue) dueDateFilter = "Overdue";

        Date now = new Date();

        for (Task task : allTasks) {
            boolean matches = true;

            if (!selectedCategory.equals("Any") &&
                    (task.getCategory() == null || task.getCategory().getCategoryName() == null ||
                            !selectedCategory.equalsIgnoreCase(task.getCategory().getCategoryName()))) {
                matches = false;
            }

            // Priority filter
            if (!selectedPriority.equals("Any") && (task.getPriority() == null || !selectedPriority.equalsIgnoreCase(task.getPriority()))) {
                matches = false;
            }

            // Status filter
            if (!selectedStatus.equals("Any") &&
                    (task.getStatus() == null || !selectedStatus.equalsIgnoreCase(task.getStatus()))) {
                matches = false;
            }

            // Favorites filter
            if (onlyFavorites && !Boolean.TRUE.equals(task.getFavorite())) {
                matches = false;
            }

            // Due date filter
            if (dueDateFilter != null && task.getDueDate() != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date due = sdf.parse(task.getDueDate());

                    if (due != null) {
                        if (dueDateFilter.equals("Today") && !isToday(due)) matches = false;
                        if (dueDateFilter.equals("This Week") && !isThisWeek(due)) matches = false;
                        if (dueDateFilter.equals("Overdue") && due.after(new Date())) matches = false;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    matches = false;
                }
            }


            if (matches) filtered.add(task);
        }

        taskAdapter.updateTasks(filtered);
    }

    private boolean isToday(Date date) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date);
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR);
    }

    private boolean isThisWeek(Date date) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date);
        return cal1.get(java.util.Calendar.WEEK_OF_YEAR) == cal2.get(java.util.Calendar.WEEK_OF_YEAR) &&
                cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR);
    }
}



