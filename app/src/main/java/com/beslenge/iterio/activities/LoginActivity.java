package com.beslenge.iterio.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.beslenge.iterio.R;
import com.beslenge.iterio.Student;
import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.fragments.FaqFragment;
import com.beslenge.iterio.utils.CheckInternet;
import com.beslenge.iterio.viewmodel.MyViewmodel;

import java.util.Objects;


public class LoginActivity extends AppCompatActivity {
    
    private static final String TAG = "BOB";
    private Button aLoginButton;
    private FaqFragment faqFragment;
    private MyViewmodel aMyViewModel;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private LottieAnimationView lottie;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private EditText aUsernameEdittext, aPasswordEdittext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prepareSharedPreferences();
        findViewsById();
        instantiateViewModel();
        setUpAnimation();
        faqFragment = new FaqFragment(this);
    
    
        aLoginButton.setOnClickListener(v -> {
            new CheckInternet(LoginActivity.this, isAvailable -> {
                if (isAvailable) {
                    login();
                    aLoginButton.setAlpha(0.5f);
                    aLoginButton.setClickable(false);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.noInternet, Toast.LENGTH_SHORT).show();
                }
            });
        });
        observerMessageAndData();
        
        
    }
    
    private void login() {
        Student student = new Student(aUsernameEdittext.getText().toString().trim(),
                aPasswordEdittext.getText().toString().trim());
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
        setDefaultMinimumAttendancePercentage();
        int activityTag = 0;
        aMyViewModel.setUserAndPasswordAndFetchData(student, activityTag);
    }
    
    private void setDefaultMinimumAttendancePercentage() {
        editor.putString(Pref.minimumAttendance, "75");
        editor.apply();
    }
    
    private void prepareSharedPreferences() {
        sharedPreferences = this.getSharedPreferences(Pref.sharedPrefName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }
    
    private void findViewsById() {
        aUsernameEdittext = findViewById(R.id.editText_userId_login_layout);
        aPasswordEdittext = findViewById(R.id.editText_password_login_layout);
        aLoginButton = findViewById(R.id.button_login_login_layout);
        progressBar = findViewById(R.id.progress_horizontal_login_activity);
        linearLayout = findViewById(R.id.linear_layout_login_layout);
        lottie = findViewById(R.id.lottie_login_layout);
    }
    
    private void instantiateViewModel() {
        aMyViewModel = new ViewModelProvider(LoginActivity.this).get(MyViewmodel.class);
    }
    
    private void observerMessageAndData() {
        aMyViewModel.getServerResponseMessage().observe(LoginActivity.this, s -> {
            assert s != null;
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            aLoginButton.setAlpha(1f);
            aLoginButton.setClickable(true);
        });
    
        aMyViewModel.getServerResponseData().observe(LoginActivity.this, s -> {
        
            if (Objects.equals(sharedPreferences.getString(Pref.status, "null"), "success")) {
                Intent intent = new Intent().setClass(LoginActivity.this, DashboardActivity.class).putExtra("code", 200);
                startActivity(intent);
                finish();
            }
        });
    }
    
    private void setUpAnimation() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> runOnUiThread(() -> {
            lottie.playAnimation();
        }), 800);
        
        setObjectAnimator(lottie, 300, 500);
        setObjectAnimator(linearLayout, 600, 500);
        setObjectAnimator(aLoginButton, 900, 550);
        
    }
    
    private void setObjectAnimator(View view, int startDelay, int animationDuration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        animator.setStartDelay(startDelay);
        animator.setDuration(animationDuration);
        animator.start();
    }
    
    public void onClickInfo(View view) {
        faqFragment.show(getSupportFragmentManager(), "FAQFRAGMENT");
    }
}