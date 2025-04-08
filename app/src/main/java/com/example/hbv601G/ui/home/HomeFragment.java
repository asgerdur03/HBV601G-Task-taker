package com.example.hbv601G.ui.home;
import com.example.hbv601G.R;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

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

                    taskList = tasks;
                    taskAdapter.updateTasks(tasks);

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
}