package com.example.hbv601G.networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkingService{
    private static final String BASE_URL = "https://hugbo-1-api.onrender.com";
    private static Retrofit retrofit;



    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            // TODO: Add AuthInterpretor
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }





}


