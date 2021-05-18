package com.beslenge.iterio.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class AttendanceFragmentViewModel extends AndroidViewModel {
    
    @NonNull
    private final MutableLiveData<String> string;
    @NonNull
    private final MutableLiveData<String> minimumAttendance;
    
    public AttendanceFragmentViewModel(@NonNull Application application) {
        super(application);
        string = new MutableLiveData<>();
        minimumAttendance = new MutableLiveData<>();
    }
    
    @NonNull
    public MutableLiveData<String> getAttendanceData() {
        return string;
    }
    
    @NonNull
    public MutableLiveData<String> getMinimumAttendance() {
        return minimumAttendance;
    }
}
