package com.beslenge.iterio.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.Student;
import com.beslenge.iterio.network.Server;

public class Repository {
    private static Server server;
    private Student student;
    private int activityTag;
    // --Commented out by Inspection (30/8/20 5:18 PM):private static final String TAG = "LOL";
    
    public Repository(Context context) {
        server = new Server(context);
    }
    
    public void setUseridAndPasswordAndFetchData(@NonNull Student student, int activityTag) {
        this.student = student;
        this.activityTag = activityTag;
        new MyThread().start();
    }
    
    public void triggerActivate() {
    
    }
    
    public MutableLiveData<String> getServerResponseData() {
        return server.getServerResponseData();
    }
    
    public MutableLiveData<String> getServerResponseMessage() {
        return server.getServerResponseMessage();
    }
    
    private static class MyTriggerThread extends Thread {
        @Override
        public void run() {
            server.triggerClient();
        }
    }
    
    private class MyThread extends Thread {
        @Override
        public void run() {
            setPriority(MAX_PRIORITY);
            server.setUserAndPasswordAndFetchData(student, activityTag);
        }
    }
    
}
