package com.example.hbv601G.services;

import com.example.hbv601G.entities.Category;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface CategoryService {

    @GET("/categories")
    Call<JsonObject> getCategories();

    @DELETE("/categories/{id}")
    Call<Void> deleteCategory(@Path("id") long id);

    @POST("/categories")
    Call<Void> createCategory(@Body Category category);



}
