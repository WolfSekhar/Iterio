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
    private MyViewmodel aMyViewmodel;
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
        aMyViewmodel.setUserAndPasswordAndFetchData(student, activityTag);
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
        aMyViewmodel = new ViewModelProvider(LoginActivity.this).get(MyViewmodel.class);
    }
    
    private void observerMessageAndData() {
        aMyViewmodel.getServerResponseMessage().observe(LoginActivity.this, s -> {
            assert s != null;
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            aLoginButton.setAlpha(1f);
            aLoginButton.setClickable(true);
        });
        
        aMyViewmodel.getServerResponseData().observe(LoginActivity.this, s -> {
            
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
        
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(lottie, View.ALPHA, 0f, 1f);
        animator2.setStartDelay(300);
        animator2.setDuration(500);
        animator2.start();
        
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(linearLayout, View.ALPHA, 0f, 1f);
        animator1.setStartDelay(600);
        animator1.setDuration(500);
        animator1.start();
        //Login button animation
        ObjectAnimator animator = ObjectAnimator.ofFloat(aLoginButton, View.ALPHA, 0f, 1f);
        animator.setStartDelay(900);
        animator.setDuration(500);
        animator.start();
        
    }
    
    public void onClickInfo(View view) {
        faqFragment.show(getSupportFragmentManager(), "FAQFRAGMENT");
    }
}