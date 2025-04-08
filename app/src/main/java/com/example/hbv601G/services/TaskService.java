package com.example.hbv601G.services;

import com.example.hbv601G.entities.Task;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.PATCH;
import retrofit2.http.Headers;

public interface TaskService {

    @GET("/tasks")
    Call<JsonObject> getTasks();


    @GET("/tasks/{id}")
    Call<JsonObject> getTaskById(
            @Path("id") long taskId
    );

    @GET("/tasks/archives")
    Call<JsonObject> getArchived();

    @POST("/tasks")
    Call<JsonObject> createTask(@Body Task task);

    @PATCH("/tasks/{id}")
    @Headers("Content-Type: application/json")
    Call<JsonObject> updateTask(
            @Path("id") long taskId,
            @Body JsonObject taskUpdateBody
    );




}
