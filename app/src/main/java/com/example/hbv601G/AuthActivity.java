package com.example.hbv601G;


import android.os.Bundle;
import com.example.hbv601G.data.local.AppDatabase;
import com.example.hbv601G.data.local.UserDao;
import com.example.hbv601G.data.local.TaskDao;
import com.example.hbv601G.entities.User;
import com.example.hbv601G.entities.Task;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hbv601G.ui.login.LoginFragment;
import com.example.hbv601G.ui.login.SignupFragment;

public class AuthActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auth);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authFragmentContainer, new LoginFragment())
                .commit();

        db = AppDatabase.getInstance(this);




    }
    public AppDatabase getDatabase() {
        return db;
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
