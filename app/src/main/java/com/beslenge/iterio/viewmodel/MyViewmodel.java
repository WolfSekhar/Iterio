package com.beslenge.iterio.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.utils.Repository;

public class MyViewmodel extends AndroidViewModel {

    private static Repository repository;


    public MyViewmodel(@NonNull Application application) {
        super(application);
        repository = new Repository(application.getApplicationContext());

    }

    public void setUserAndPasswordAndFetchData(@NonNull String username ,@NonNull String password){
        repository.setUseridAndPasswordAndFetchData(username,password);
    }

    public MutableLiveData<String> getData(){
        return repository.getData();
    }

    public MutableLiveData<String> getMessage() {
        return repository.getMessage();

    }

}
