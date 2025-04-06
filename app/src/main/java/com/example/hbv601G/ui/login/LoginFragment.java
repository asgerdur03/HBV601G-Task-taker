package com.example.hbv601G.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.hbv601G.networking.NetworkingService;
import com.example.hbv601G.services.UserService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText usernameInput, passwordInput;
    private Button loginButton;
    private TextView goToSignup;
    private AuthActivity authActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInctenceState){
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton =view.findViewById(R.id.loginButton);
        goToSignup = view.findViewById(R.id.goToSignup);

        authActivity = (AuthActivity) getActivity();

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
                    JsonObject user = jsonObject.getAsJsonObject("user");
                    String username = user.get("username").getAsString();

                    Log.d("Login", "Token: " + token);
                    Log.d("Login", "User: " + username);
                    // TODO: Save the token, so i can call the locked routes, else the redirect is enough

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();
                }
                else{
                    Log.e("Login", "failed: " + response.code());
                    Toast.makeText(getActivity(), "invalid login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Login", "failed: " + t.getMessage());
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