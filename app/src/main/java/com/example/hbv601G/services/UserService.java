package com.example.hbv601G.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.hbv601G.entities.Category;
import com.example.hbv601G.entities.User;
import com.google.gson.JsonObject;

import java.security.Provider;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserService{

    /*
    for diffrent input reqs from api:

    @POST("json-endpoint")
    Call<Something> sendJson(@Body SomeObject body);

    @FormUrlEncoded
    @POST("login")
        Call<TokenResponse> login(
        @Field("username") String username,
        @Field("password") String password
    );

    @Multipart
       @POST("upload")
        Call<UploadResponse> uploadFile(
        @Part MultipartBody.Part file,
        @Part("description") RequestBody description
    );
    *
    * */

    @GET("/admin")
    Call<JsonObject> getAllUsers();

    @FormUrlEncoded
    @POST("/login")
    Call<JsonObject> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("/signup")
    Call<JsonObject> signup(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );








}
