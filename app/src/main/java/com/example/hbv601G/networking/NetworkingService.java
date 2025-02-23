package com.example.hbv601G.networking;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import com.google.gson.*;


public class NetworkingService extends Service {

    private final String baseURL;
    private JsonElement jsonElement;
    private int code; //?


    public NetworkingService(){
        baseURL = "api_url_a_render"; //todo, laga api og setja á render
    }


    // cite your sources:
    // https://www.baeldung.com/guide-to-okhttp
    public JsonElement getRequest(String url){
        return null;
    }

    public JsonElement deleteRequest(String url){
        return null;
    }

    public JsonElement postRequest(String url, String data){
        return null;
    }

    public JsonElement patchRequest(String url, String data){
        return null;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}






