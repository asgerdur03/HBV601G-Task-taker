package com.example.hbv601G.services;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TaskService {

    @GET("/tasks")
    Call<JsonObject> getTasks();


    @GET("/tasks/{id}")
    Call<JsonObject> getTaskById(
            @Path("id") long taskId
    );

    @GET("/tasks/archives")
    Call<JsonObject> getArchived();




}
