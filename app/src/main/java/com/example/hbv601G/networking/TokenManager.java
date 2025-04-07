package com.example.hbv601G.networking;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    private final SharedPreferences prefs;

    public TokenManager(Context context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token){
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken(){
        return prefs.getString(KEY_TOKEN, null);
    }

    public void clearToken(){
        prefs.edit().remove(KEY_TOKEN).apply();
    }
}
