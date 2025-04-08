package com.example.hbv601G.ui.archived;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbv601G.databinding.FragmentArchivedBinding;
import com.example.hbv601G.entities.Task;
import com.example.hbv601G.entities.User;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.TaskService;
import com.example.hbv601G.services.UserService;
import com.example.hbv601G.ui.home.TaskAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArchivedFragment extends Fragment {

    private FragmentArchivedBinding binding;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;

    private List<Task> taskList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArchivedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerViewTasks;


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new TaskAdapter(taskList, new TaskAdapter.OnClickListener() {
            // todo: implement by calling api (archived)
            @Override
            public void onDeleteClick(Task task) {
                Toast.makeText(getContext(), "delete task from archived", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onArchiveClick(Task task) {
                Toast.makeText(getContext(), "Unarchive task", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFavoriteClick(Task task) {
                Toast.makeText(getContext(), "favorite task from archived", Toast.LENGTH_SHORT).show();

            }
        }
        );
        recyclerView.setAdapter(taskAdapter);

        fetchTasks();

        return root;
    }



    private void fetchTasks(){
        TaskService taskService = NetworkingService.getRetrofitAuthInstance(requireContext()).create(TaskService.class);

        taskService.getArchived().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    JsonObject json = response.body();

                    JsonArray taskArray = json.getAsJsonArray("archivedTasks");

                    List<Task> tasks = new ArrayList<>();

                    for (JsonElement element: taskArray){
                        Task task = new Gson().fromJson(element, Task.class);
                        tasks.add(task);
                    }

                    Log.d("ArchivedTaskDebug", "size: " + tasks.size());

                    taskAdapter.updateTasks(tasks);

                }else{
                    Log.e("ArchivedTasks", "Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void loadAdmin(){
        UserService userService = NetworkingService.getRetrofitInstance().create(UserService.class);

        userService.getAllUsers().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                JsonArray usersArray = jsonObject.getAsJsonArray("users");

                for (JsonElement user: usersArray){
                    JsonObject userObj = user.getAsJsonObject();
                    String username =  userObj.get("username").getAsString();
                    Log.d("userTest", "username: " + username);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Usertest", "Error: " + t.getMessage());

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}