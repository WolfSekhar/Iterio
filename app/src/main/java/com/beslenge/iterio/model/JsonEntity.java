package com.beslenge.iterio.model;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonEntity {
    /* this class will manage the json objects that are used for sending objects
       to servers. It contains getters and setter like emptyJsonObject  and
       idpasswordJsonObject .
       idpassword
     */

    private final JSONObject idpasswordJsonObject = new JSONObject();
    private final JSONArray emptyJsonArray = new JSONArray();

    /* Constructor */
    public JsonEntity() {

    }


    /* Getter and setter for UserID and Password */
    @NonNull
    public JSONObject getIdpasswordJsonObject() {
        return idpasswordJsonObject;
    }
    public void setIdpasswordJsonObject(String userId,String password) {
        try {
            this.idpasswordJsonObject.put("username",userId);
            this.idpasswordJsonObject.put("password",password);
            this.idpasswordJsonObject.put("MemberType", "S");
        }catch (Exception ignored){}

    }

    /* Getters for emptyJsonArray */
    @NonNull
    public JSONArray getEmptyJsonArray() {
        return emptyJsonArray;
    }
}
