package com.beslenge.iterio.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.utils.Repository;

public class MyViewmodel extends AndroidViewModel {

    private static Repository repository;
    private MutableLiveData<String> data;
    private MutableLiveData<String> message;


    public MyViewmodel(@NonNull Application application) {
        super(application);
        data = new MutableLiveData<>();
        message = new MutableLiveData<>();
        repository = new Repository(application.getApplicationContext());
        message = repository.getMessage();
        data = repository.getData();

    }

    public void setUserAndPasswordAndFetchData(@NonNull String username ,@NonNull String password){
        repository.setUseridAndPasswordAndFetchData(username,password);
    }

    public MutableLiveData<String> getData(){
        return data;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

}
