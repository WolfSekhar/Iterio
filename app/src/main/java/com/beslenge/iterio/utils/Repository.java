package com.beslenge.iterio.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.network.Server;

public class Repository {
    private static Server server;
    private String username,password;
    // --Commented out by Inspection (30/8/20 5:18 PM):private static final String TAG = "LOL";

    public Repository(Context context) {
        server = new Server(context);

    }

    public void setUseridAndPasswordAndFetchData(@NonNull String username,@NonNull String password){
        this.username = username;
        this.password = password;
        new MyThread().start();
    }

    public MutableLiveData<String> getData(){
        return server.getData();
    }
    public MutableLiveData<String> getMessage(){
        return server.getMessage();
    }

    private class MyThread extends Thread{
        @Override
        public void run() {
            setPriority(MAX_PRIORITY);
            server.setUserAndPasswordAndFetchData(username, password);
        }
    }

}
