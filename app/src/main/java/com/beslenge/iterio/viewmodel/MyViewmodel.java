package com.beslenge.iterio.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.Student;
import com.beslenge.iterio.utils.Repository;

public class MyViewmodel extends AndroidViewModel {
    
    private static Repository repository;
    
    
    public MyViewmodel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());
        
    }
    
    public void setUserAndPasswordAndFetchData(@NonNull Student student, int activityTag) {
        repository.setUseridAndPasswordAndFetchData(student, activityTag);
    }
    
    public MutableLiveData<String> getServerResponseData() {
        return repository.getServerResponseData();
    }
    
    public MutableLiveData<String> getServerResponseMessage() {
        return repository.getServerResponseMessage();
        
    }
    
    public void triggerRepository() {
        repository.triggerActivate();
    }
    
}
