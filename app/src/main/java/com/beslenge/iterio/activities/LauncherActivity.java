package com.beslenge.iterio.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.data.Student;

import java.util.Objects;

public class LauncherActivity extends Activity {
    private Student student;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTheme();

        student = new Student(this);

        goToLoginOrDashboardAcitivity();

    }

    private void setTheme() {
        AppCompatDelegate.setDefaultNightMode(this.getSharedPreferences(Pref.sharedPrefName, MODE_PRIVATE).getInt(Pref.theme, -1));
    }

    private void goToLoginOrDashboardAcitivity() {
        if (Objects.requireNonNull(student.getStatus()).equals("success")) {
            if (getIntent().getIntExtra("code", 0) == 300) {
                startActivity(new Intent().setClass(LauncherActivity.this, DashboardActivity.class).putExtra("code", 300));
            } else {
                startActivity(new Intent().setClass(LauncherActivity.this, DashboardActivity.class));
            }
            finish();
        } else {
            startActivity(new Intent().setClass(LauncherActivity.this, LoginActivity.class));
            finish();
        }
    }
}
