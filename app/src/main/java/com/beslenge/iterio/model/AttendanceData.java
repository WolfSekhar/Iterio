package com.beslenge.iterio.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttendanceData {
    @Nullable
    private final JSONObject jsonData;
    private List<String> subjects;
    private List<Integer> attendedClasses;
    private List<Integer> totalClasses;
    private List<Double> attendancePercentage;
    private List<String> typeOfClass;
    private List<String> updatedOn;
    
    public AttendanceData(@NonNull String data) {
        this.jsonData = convertToJson(data);
        getSubjectsAttendance();
    }
    
    public int numberOfSubjects() {
        int count = 0;
        try {
            assert jsonData != null;
            if (isDataAvailable(jsonData)) {
                count = Objects.requireNonNull(jsonData).getJSONArray("griddata").length();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    @Nullable
    private JSONObject convertToJson(@NonNull String data) {
        JSONObject jsonAttendanceData = null;
        try {
            jsonAttendanceData = new JSONObject(data);
        } catch (Exception ignored) {
        }
        return jsonAttendanceData;
    }
    
    private void getSubjectsAttendance() {
        subjects = new ArrayList<>();
        attendedClasses = new ArrayList<>();
        totalClasses = new ArrayList<>();
        attendancePercentage = new ArrayList<>();
        typeOfClass = new ArrayList<>();
        updatedOn = new ArrayList<>();
        
        for (int i = 0; i < numberOfSubjects(); i++) {
            try {
                subjects.add(Objects.requireNonNull(jsonData).getJSONArray("griddata").getJSONObject(i).getString("subject"));
                attendancePercentage.add(jsonData.getJSONArray("griddata").getJSONObject(i).getDouble("TotalAttandence"));
                updatedOn.add(jsonData.getJSONArray("griddata").getJSONObject(i).getString("lastupdatedon"));
                
                if (!jsonData.getJSONArray("griddata").getJSONObject(i).getString("Patt").equals("Not Applicable")) {
                    String[] localAtten = jsonData.getJSONArray("griddata").getJSONObject(i).getString("Patt").split("/");
                    attendedClasses.add(Integer.valueOf(localAtten[0].trim()));
                    totalClasses.add(Integer.valueOf(localAtten[1].trim()));
                    typeOfClass.add("Lab");
                    
                } else {
                    String[] localAtten = jsonData.getJSONArray("griddata").getJSONObject(i).getString("Latt").split("/");
                    attendedClasses.add(Integer.valueOf(localAtten[0].trim()));
                    totalClasses.add(Integer.valueOf(localAtten[1].trim()));
                    typeOfClass.add("Theory");
                }
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<String> getSubjects() {
        return subjects;
    }
    
    public List<Integer> getAttendedClasses() {
        return attendedClasses;
    }
    
    public List<Integer> getTotalClasses() {
        return totalClasses;
    }
    
    public List<Double> getAttendancePercentage() {
        return attendancePercentage;
    }
    
    public List<String> getTypeOfClass() {
        return typeOfClass;
    }
    
    public List<String> getUpdatedOn() {
        return updatedOn;
    }
    
    private boolean isDataAvailable(JSONObject json) {
        return json.has("griddata");
    }
    
}
