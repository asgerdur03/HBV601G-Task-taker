package com.example.hbv601G.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.example.hbv601G.data.local.UserDao;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbv601G.AuthActivity;
import com.example.hbv601G.MainActivity;
import com.example.hbv601G.R;
import com.example.hbv601G.data.local.AppDatabase;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.networking.TokenManager;
import com.example.hbv601G.services.UserService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.hbv601G.data.local.TaskDao;

import com.example.hbv601G.services.TaskService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


import com.example.hbv601G.entities.User;
import com.example.hbv601G.entities.Task;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView goToSignup;
    private AuthActivity authActivity;

    private UserDao userDao;
    private TaskDao taskDao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInctenceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton =view.findViewById(R.id.loginButton);
        goToSignup = view.findViewById(R.id.goToSignup);

        authActivity = (AuthActivity) getActivity();

        AppDatabase db = AppDatabase.getInstance(requireContext());
        userDao = db.userDao();
        taskDao = db.taskDao();

        loginButton.setOnClickListener(v -> loginUser());
        goToSignup.setOnClickListener(v -> authActivity.switchToSignup());

        return view;
    }

    private void loginUser(){
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        UserService userService = NetworkingService.getRetrofitInstance().create(UserService.class);

        userService.login(username, password).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null){
                    JsonObject jsonObject = response.body();

                    String token = jsonObject.get("token").getAsString();
                    JsonObject userJson = jsonObject.getAsJsonObject("user");
                    String usernameFromJson = userJson.get("username").getAsString();

                    User user = new User();
                    user.setId(userJson.get("id").getAsInt());
                    user.setUsername(userJson.get("username").getAsString());
                    user.setPassword(password);
                    user.setGmail(userJson.get("gmail").getAsString());
                    user.setProfilePicture(userJson.has("profilePicture") && !userJson.get("profilePicture").isJsonNull()
                            ? userJson.get("profilePicture").getAsString() : null);
                    user.setEnabled(userJson.get("enabled").getAsBoolean());

                    userDao.insertUser(user);

                    Log.d("Login", "User: " + usernameFromJson);

                    TokenManager tokenManager = new TokenManager(requireContext());
                    tokenManager.saveToken(token);
                    Log.d("TokenTest", "Saved token: " + tokenManager.getToken());
                    SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
                    prefs.edit().putString("logged_in_username", user.getUsername()).apply();


                    TaskService taskService = NetworkingService.getRetrofitInstance().create(TaskService.class);

                    taskService.getTasks().enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                JsonArray taskArray = response.body().getAsJsonArray();

                                if (taskArray != null) {
                                    List<Task> tasksToSave = new ArrayList<>();

                                    for (JsonElement element : taskArray) {
                                        JsonObject taskObj = element.getAsJsonObject();
                                        Task task = new Task();
                                        task.setId(taskObj.get("id").getAsInt());
                                        task.setTitle(taskObj.get("title").getAsString());
                                        task.setDescription(taskObj.get("description").getAsString());
                                        task.setDueDate(taskObj.get("dueDate").getAsString());
                                        task.setCompleted(taskObj.get("completed").getAsBoolean());
                                        task.setUserId(user.getId());
                                        tasksToSave.add(task);

                                        task.setTaskName(taskObj.has("title") ? taskObj.get("title").getAsString() : "");
                                        task.setTaskNote(taskObj.has("description") ? taskObj.get("description").getAsString() : "");
                                        task.setPriority(taskObj.has("priority") ? taskObj.get("priority").getAsString() : "Medium");
                                        task.setStatus(taskObj.has("status") ? taskObj.get("status").getAsString() : "Pending");
                                        task.setFavorite(taskObj.has("favorite") && !taskObj.get("favorite").isJsonNull() ? taskObj.get("favorite").getAsBoolean() : false);
                                        task.setArchived(taskObj.has("archived") && !taskObj.get("archived").isJsonNull() ? taskObj.get("archived").getAsBoolean() : false);
                                    }

                                    taskDao.deleteTasksForUser(user.getId());
                                    taskDao.insertAll(tasksToSave);


                                    Log.d("Login", "Tasks saved: " + tasksToSave.size());
                                }
                            }
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            requireActivity().finish();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("Login", "Failed to fetch tasks: " + t.getMessage());
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            requireActivity().finish();
                        }
                    });
                } else {
                    Log.e("Login", "failed: " + response.code());
                    Toast.makeText(getActivity(), "invalid login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Login", "API failed: " + t.getMessage());

                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                User localUser = userDao.getUserByCredentials(username, password);

                if (localUser != null) {
                    Log.d("Login", "Offline login success for user: " + localUser.getUsername());
                    Toast.makeText(getActivity(), "Offline login success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    requireActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "Offline login failed. No local user found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

       /*
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String savedEmail = prefs.getString("user_email", "demo");
        String savedPassword = prefs.getString("user_password", "demho");
        if (email.equals(savedEmail) && password.equals(savedPassword)) {
            saveToken("token");
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Rangt notandanafn eða lykilorð", Toast.LENGTH_SHORT).show();
        }*/

    }


    private void saveToken(String token){
        SharedPreferences prefs = getActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt_token", "mock_token_123456");
        editor.apply();
    }



}