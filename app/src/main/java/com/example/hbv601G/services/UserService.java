package com.example.hbv601G.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.hbv601G.entities.Category;
import com.example.hbv601G.entities.User;

import java.security.Provider;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService{

    @GET("admin")
    Call<List<User>> getAllUsers();

    @POST("login")
    Call<User> login(@Body User loginRequest);

    @POST("signup")
    Call<Void> signup(@Body User signupRequest);







}
