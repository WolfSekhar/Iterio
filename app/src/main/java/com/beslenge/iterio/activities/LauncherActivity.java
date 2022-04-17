package com.beslenge.iterio.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.data.StudentPreferences;

import java.util.Objects;

public class LauncherActivity extends Activity {
    private StudentPreferences studentPreferences;
    
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setTheme();
        
        studentPreferences = new StudentPreferences(this);
        
        goToLoginOrDashboardActivity();
        
    }
    
    private void setTheme() {
        AppCompatDelegate.setDefaultNightMode(this.getSharedPreferences(Pref.sharedPrefName, MODE_PRIVATE).getInt(Pref.theme, -1));
    }
    
    private void goToLoginOrDashboardActivity() {
        if (Objects.requireNonNull(studentPreferences.getStatus()).equals("success")) {
            if (getIntent().getIntExtra("code", 0) == 300) {
                startActivity(new Intent().setClass(LauncherActivity.this, DashboardActivity.class).putExtra("code", 300));
            } else {
                startActivity(new Intent().setClass(LauncherActivity.this, DashboardActivity.class));
            }
        } else {
            startActivity(new Intent().setClass(LauncherActivity.this, LoginActivity.class));
        }
        finish();
    }
}
