package com.beslenge.iterio.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Cookie {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Cookie(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(Pref.sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void save(String cookie){
        editor.putString(Pref.cookie,cookie);
        editor.apply();
    }

    @NonNull
    public String getCookie(){
        return Objects.requireNonNull(sharedPreferences.getString(Pref.cookie, "NA"));
    }
}
