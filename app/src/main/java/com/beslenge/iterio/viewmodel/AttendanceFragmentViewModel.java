package com.beslenge.iterio.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AttendanceFragmentViewModel extends ViewModel {

    @NonNull
    private final MutableLiveData<String> string;
    @NonNull
    private final MutableLiveData<String> minimumAttendance;
    // --Commented out by Inspection (30/8/20 5:18 PM):private final String TAG = "LOL";


    public AttendanceFragmentViewModel() {
        super();
        string = new MutableLiveData<>();
        minimumAttendance = new MutableLiveData<>();
    }

//    public AttendanceFragmentViewModel(@NonNull Application application) {
//        super(application);
//        string = new MutableLiveData<>();
//        minimumAttendance = new MutableLiveData<>();
//    }

    @NonNull
    public MutableLiveData<String> getAttendanceData() {
        return string;
    }

    @NonNull
    public MutableLiveData<String> getMinimumAttendance() {
        return minimumAttendance;
    }
}
