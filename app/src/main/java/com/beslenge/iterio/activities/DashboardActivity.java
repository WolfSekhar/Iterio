package com.beslenge.iterio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.beslenge.iterio.R;
import com.beslenge.iterio.data.Pref;
import com.beslenge.iterio.fragments.AttendanceFragment;
import com.beslenge.iterio.fragments.NoDataAvailableFragment;
import com.beslenge.iterio.viewmodel.AttendanceFragmentViewModel;
import com.beslenge.iterio.viewmodel.MyViewmodel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private final String TAG = "LOL";
    private static AttendanceFragment attendanceFragment;
    private AttendanceFragmentViewModel attendanceviewModel;
    private MyViewmodel myViewmodel;
    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private int stopStatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setUpToolbar();
        progressBar = findViewById(R.id.progress_horizontal_dashboard_activity);
        frameLayout = findViewById(R.id.frame_layout);

        prepareSharedPreferences();
        instantiateFragmentAndViewModels();


        fragmentManager = DashboardActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (!Objects.equals(sharedPreferences.getString(Pref.attendanceData, "NODATA"), "NODATA")) {
            fragmentTransaction.add(frameLayout.getId(), attendanceFragment,"attendanceTag");
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.add(frameLayout.getId(),new NoDataAvailableFragment(),"nodataTag");
            fragmentTransaction.commit();
        }


        observeDataAndMessage();

        toolbar.setOnMenuItemClickListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.menu_item_update) {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
                refetchAttendance();
                return true;
            } else if (itemId == R.id.menu_item_name) {
                new MaterialAlertDialogBuilder(DashboardActivity.this)
                        .setTitle("Name :")
                        .setMessage(sharedPreferences.getString(Pref.name, "NA"))
                        .show();
                return true;
            } else if (itemId == R.id.menu_item_set_minimum_attendance || itemId == R.id.menu_item_set_theme) {
                return true;
            } else if (itemId == R.id.menu_item_credit) {
                new MaterialAlertDialogBuilder(DashboardActivity.this)
                        .setView(R.layout.credits_view)
                        .show();
                return true;
            } else if (itemId == R.id.menu_item_logout) {
                new MaterialAlertDialogBuilder(DashboardActivity.this)
                        .setTitle("Logout !")
                        .setMessage("Do you really want to logout?")
                        .setNeutralButton("cancel", (dialog, which) -> Snackbar.make(frameLayout, "Logout Cancelled", BaseTransientBottomBar.LENGTH_SHORT).show())
                        .setPositiveButton("Yes", (dialog, which) -> {
                            editor.remove(Pref.status);
                            editor.remove(Pref.minimumAttendance);
                            editor.remove(Pref.attendanceData);
                            editor.remove(Pref.cookie);
                            editor.remove(Pref.registrationID);
                            editor.remove(Pref.name);
                            editor.apply();
                            startActivity(new Intent().setClass(DashboardActivity.this, LauncherActivity.class));
                            finish();
                        }).show();
                return true;
            }
            return false;

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_toolbar_menu_items, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        SubMenu subMenu = menu.getItem(2).getSubMenu();
        SubMenu subMenuTheme = menu.getItem(3).getSubMenu();
        switch (Objects.requireNonNull(sharedPreferences.getString(Pref.minimumAttendance, "25"))) {
            case "35":
                subMenuItemRadioSelect(subMenu, 1);
                break;
            case "50":
                subMenuItemRadioSelect(subMenu, 2);
                break;
            case "60":
                subMenuItemRadioSelect(subMenu, 3);
                break;
            case "75":
                subMenuItemRadioSelect(subMenu, 4);
                break;
            case "85":
                subMenuItemRadioSelect(subMenu, 5);
                break;
            case "95":
                subMenuItemRadioSelect(subMenu, 6);
                break;
            default:
                subMenuItemRadioSelect(subMenu, 0);
                break;
        }

        switch (sharedPreferences.getInt(Pref.theme, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                subMenuItemThemeSelect(subMenuTheme, 0);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                subMenuItemThemeSelect(subMenuTheme, 1);
                break;
            default:
        }

        return true;
    }

    @Override
    protected void onStart() {

        if (getIntent().getIntExtra("code", 0) == 200) {
            getIntent().removeExtra("code");
            Snackbar.make(findViewById(R.id.menu_item_update), Objects.requireNonNull(sharedPreferences.getString(Pref.name, "No Name")), BaseTransientBottomBar.LENGTH_LONG).show();
        } else if (getIntent().getIntExtra("code", 0) == 300) {
            getIntent().removeExtra("code");
            stopStatus=100;
        } else {
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            refetchAttendance();
        }
        super.onStart();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        getIntent().putExtra("code", 300);
        super.onStop();

    }

    private void prepareSharedPreferences() {

        sharedPreferences = this.getSharedPreferences(Pref.sharedPrefName, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
    }

    private void instantiateFragmentAndViewModels() {
        attendanceFragment = new AttendanceFragment();
        attendanceviewModel = new ViewModelProvider(DashboardActivity.this).get(AttendanceFragmentViewModel.class);
        myViewmodel = new ViewModelProvider(this).get(MyViewmodel.class);
    }

    public void onClickOfSetAttendance(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_attendance_25) {
            saveMinimumAttendance("25");
        } else if (itemId == R.id.menu_item_attendance_35) {
            saveMinimumAttendance("35");
        } else if (itemId == R.id.menu_item_attendance_50) {
            saveMinimumAttendance("50");
        } else if (itemId == R.id.menu_item_attendance_60) {
            saveMinimumAttendance("60");
        } else if (itemId == R.id.menu_item_attendance_75) {
            saveMinimumAttendance("75");
        } else if (itemId == R.id.menu_item_attendance_85) {
            saveMinimumAttendance("85");
        } else if (itemId == R.id.menu_item_attendance_95) {
            saveMinimumAttendance("95");
        }
    }

    private void saveMinimumAttendance(String minAttendanceVal) {
        editor.putString(Pref.minimumAttendance, minAttendanceVal).commit();
        attendanceviewModel.getMinimumAttendance().setValue(minAttendanceVal);
    }

    private void subMenuItemRadioSelect(@NonNull SubMenu subMenu, int index) {
        subMenu.getItem(index).setChecked(true);
    }

    private void subMenuItemThemeSelect(@NonNull SubMenu subMenu, int index) {
        subMenu.getItem(index).setChecked(true);
    }

    private void refetchAttendance() {
        String username = sharedPreferences.getString(Pref.userID, "NA");
        String password = sharedPreferences.getString(Pref.password, "NA");
        assert password != null;
        assert username != null;
        myViewmodel.setUserAndPasswordAndFetchData(username, password);
    }

    public void onSetTheme(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_light) {
            editor.putInt(Pref.theme, AppCompatDelegate.MODE_NIGHT_NO).commit();
            if (isSelectedTheme(AppCompatDelegate.getDefaultNightMode(), AppCompatDelegate.MODE_NIGHT_NO)) {
                restartActivity();
            }
        } else if (itemId == R.id.menu_item_dark) {
            editor.putInt(Pref.theme, AppCompatDelegate.MODE_NIGHT_YES).commit();
            if (isSelectedTheme(AppCompatDelegate.getDefaultNightMode(), AppCompatDelegate.MODE_NIGHT_YES)) {
                restartActivity();
            }
        }
    }

    private void restartActivity() {
        startActivity(new Intent().setClass(DashboardActivity.this, LauncherActivity.class).putExtra("code", 300));
        finish();
    }

    private boolean isSelectedTheme(int currentMode, int setMode) {
        return currentMode != setMode;
    }

    private void observeDataAndMessage() {
        myViewmodel.getData().observe(DashboardActivity.this, s -> {
            attendanceviewModel.getAttendanceData().postValue(s);
            fragmentManager.executePendingTransactions();

            Fragment fragment = fragmentManager.findFragmentById(frameLayout.getId());
            assert fragment != null;
            if (!s.equals("NODATA")) {
                if (fragment != attendanceFragment){
                    fragmentManager.beginTransaction()
                            .replace(frameLayout.getId(), attendanceFragment)
                            .commit();
                }
            } else {

                if (!fragment.equals(new NoDataAvailableFragment())){
                    fragmentManager.beginTransaction()
                            .replace(frameLayout.getId(), new NoDataAvailableFragment())
                            .commit();
                }
            }
            fragmentManager.executePendingTransactions();

        });
        myViewmodel.getMessage().observe(DashboardActivity.this, s -> {
            if (s.equals("Login Successful")) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (stopStatus != 100) {
                        Snackbar.make(findViewById(R.id.menu_item_update), "Attendance Updated", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(findViewById(R.id.menu_item_update), s, BaseTransientBottomBar.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }

}