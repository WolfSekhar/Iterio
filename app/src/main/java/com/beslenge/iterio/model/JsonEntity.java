package com.beslenge.iterio.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonEntity {
    /* this class will manage the json objects that are used for sending objects
       to servers. It contains getters and setter like emptyJsonObject  and
       idpasswordJsonObject.
     */
    
    private final JSONObject idPasswordJsonObject = new JSONObject();
    private final JSONArray emptyJsonArray = new JSONArray();
    
    /* Constructor */
    public JsonEntity() {
    
    }
    
    
    /* Getter and setter for UserID and Password */
    @NonNull
    public JSONObject getIdPasswordJsonObject() {
        return idPasswordJsonObject;
    }
    
    public void setIdPasswordJsonObject(String userId, String password) {
        try {
            this.idPasswordJsonObject
                    .put("username", userId)
                    .put("password", password)
                    .put("MemberType", "S");
            
        } catch (Exception ignored) {
        }
        
    }
    
    /* Getters for emptyJsonArray */
    @NonNull
    public JSONArray getEmptyJsonArray() {
        return emptyJsonArray;
    }
}
