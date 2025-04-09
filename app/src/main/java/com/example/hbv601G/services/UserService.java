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

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    */

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


    // fields are optional, so either fill out all or autocomplete with old values,
    // else we end up with some value as the empty string
    @FormUrlEncoded
    @PATCH("/update")
    Call<JsonObject> update(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );


    @DELETE("/delete/me")
    Call<JsonObject> deleteUser();

    @Multipart
    @POST("/upload-pic")
    Call<JsonObject> uploadImage(
            @Part MultipartBody.Part image
    );
    /*
   // frá chat, hvernig á að kalla á api með image
    File file = new File(filePath); // filePath = actual image file path
RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

UserService service = NetworkingService.getRetrofitAuthInstance(context).create(UserService.class);

service.uploadProfilePicture(body).enqueue(new Callback<JsonObject>() {
    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        if (response.isSuccessful() && response.body() != null) {
            String uploadedPath = response.body().get("path").getAsString();
            Log.d("Upload", "Success! Path: " + uploadedPath);
            // You can now use this path in the UI if needed
        } else {
            Log.e("Upload", "Failed with code: " + response.code());
        }
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        Log.e("Upload", "Error: " + t.getMessage());
    }
});

     */


}
