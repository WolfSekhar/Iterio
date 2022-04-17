package com.beslenge.iterio.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ServerPreferences {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    
    public ServerPreferences(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(Pref.sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    
    public void saveCookie(String cookie) {
        editor.putString(Pref.cookie, cookie);
        editor.apply();
    }
    
    public void saveIp(String ip) {
        editor.putString(Pref.ip, ip);
        editor.apply();
    }
    
    @NonNull
    public String getCookie() {
        return Objects.requireNonNull(sharedPreferences.getString(Pref.cookie, "NA"));
    }
    
    @NonNull
    public String getIp() {
        return Objects.requireNonNull(sharedPreferences.getString(Pref.ip, "NA"));
    }
    
}
