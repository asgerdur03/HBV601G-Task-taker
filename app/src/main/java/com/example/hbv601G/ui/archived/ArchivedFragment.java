package com.example.hbv601G.ui.archived;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hbv601G.databinding.FragmentArchivedBinding;
import com.example.hbv601G.entities.User;
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArchivedFragment extends Fragment {

    private FragmentArchivedBinding binding;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArchivedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // TODO: remove, Add actual call to Archived
        loadAdmin();

        return root;
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