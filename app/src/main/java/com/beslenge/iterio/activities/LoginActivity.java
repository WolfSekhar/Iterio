package com.beslenge.iterio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.beslenge.iterio.viewmodel.MyViewmodel;
import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    // --Commented out by Inspection (30/8/20 5:18 PM):private static final String TAG = "BOB";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EditText aUsernameEdittext, aPasswordEdittext;
    private Button aLoginButton;
    private MyViewmodel aMyViewmodel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prepareSharedPreferences();
        findViewsById();
        instantiateViewModel();


        aLoginButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            setDefaultMinimumAttendancePercentage();
            aMyViewmodel.setUserAndPasswordAndFetchData(aUsernameEdittext.getText().toString(), aPasswordEdittext.getText().toString());

        });
        observerMessageAndData();
    }

    private void setDefaultMinimumAttendancePercentage() {
        editor.putString(Pref.minimumAttendance, "25");
        editor.apply();
    }

    private void prepareSharedPreferences() {
        sharedPreferences = this.getSharedPreferences(Pref.sharedPrefName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    private void findViewsById(){
        aUsernameEdittext = findViewById(R.id.editText_userId_login_layout);
        aPasswordEdittext = findViewById(R.id.editText_password_login_layout);
        aLoginButton = findViewById(R.id.button_login_login_layout);
        progressBar = findViewById(R.id.progress_horizontal_login_activity);
    }

    private void instantiateViewModel(){
        aMyViewmodel = new ViewModelProvider(LoginActivity.this).get(MyViewmodel.class);
    }

    private void observerMessageAndData(){
        aMyViewmodel.getMessage().observe(LoginActivity.this, s -> {
            assert s != null;
            if (s.equals("Login Successful")){
                progressBar.setVisibility(View.GONE);
            }
            new Handler().postDelayed(() -> runOnUiThread(() -> progressBar.setVisibility(View.GONE)),4000);
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
        });

        aMyViewmodel.getData().observe(LoginActivity.this, s -> {

            if (Objects.equals(sharedPreferences.getString(Pref.status, "null"), "success")) {
                Intent intent = new Intent().setClass(LoginActivity.this, DashboardActivity.class).putExtra("code", 200);
                startActivity(intent);
                finish();
            }
        });
    }

}