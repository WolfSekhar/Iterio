package com.beslenge.iterio.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Student {
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public Student(@NonNull Context context) {
        sharedPreferences = context.getSharedPreferences(Pref.sharedPrefName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    public void saveUserIDAndPassword(@NonNull String userID, @NonNull String password){
        editor.putString(Pref.userID,userID);
        editor.putString(Pref.password, password);
        editor.commit();
    }

    public void saveName(@NonNull String name){
        editor.putString(Pref.name, name);
        editor.commit();
    }

    public void saveLoginStatus(@NonNull String status){
        editor.putString(Pref.status, status);
        editor.commit();
    }

    public void saveRegistrationID(@NonNull String registrationID){
        editor.putString(Pref.registrationID, registrationID);
        editor.commit();
    }

    public void saveAttendance(@NonNull String attendanceData){
        editor.putString(Pref.attendanceData, attendanceData);
        editor.commit();
    }

    @Nullable
    public String getRegistrationID(){
        return sharedPreferences.getString(Pref.registrationID,"NA");
    }
    @Nullable
    public String getAttendance(){
        return sharedPreferences.getString(Pref.attendanceData,"NA");
    }

    @Nullable
    public String getStatus(){
        return sharedPreferences.getString(Pref.status,"NA");
    }
}
