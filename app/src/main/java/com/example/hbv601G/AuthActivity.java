package com.example.hbv601G;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbv601G.ui.login.LoginFragment;
import com.example.hbv601G.ui.login.SignupFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authFragmentContainer, new LoginFragment())
                .commit();

    }

    public void switchToSignup(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authFragmentContainer, new SignupFragment())
                .commit();
    }

    public void switchToLogin(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authFragmentContainer, new LoginFragment())
                .commit();
    }




}
