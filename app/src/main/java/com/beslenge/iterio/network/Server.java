package com.beslenge.iterio.network;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.data.Cookie;
import com.beslenge.iterio.data.Student;
import com.beslenge.iterio.model.AttendanceData;
import com.beslenge.iterio.model.JsonEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



/* This class is used for connection to server and retrieve information .
 *   It does POST request to the server with username and password that is
 *  retrieved from the EditText Field from LoginActivity .
 */


@SuppressWarnings("UnnecessaryLocalVariable")
public class Server {
    private final JsonEntity jsonEntity = new JsonEntity();
    @Nullable
    private final MediaType JSON = MediaType.parse("application/json"); // Request Media Type
    public final OkHttpClient client = new OkHttpClient(); // client for networking
    public final String TAG = "BOB";
    private String username;
    private String password;
    @NonNull
    private final Student student;
    @NonNull
    private final MutableLiveData<String> data;
    @NonNull
    private final MutableLiveData<String> message;
    @NonNull
    private final Cookie cookie;


    public Server(@NonNull Context context) {
        cookie = new Cookie(context);
        data = new MutableLiveData<>();
        message = new MutableLiveData<>();
        student = new Student(context);

    }

    public void setUserAndPasswordAndFetchData(String username, String password) {
        jsonEntity.setIdpasswordJsonObject(username, password);
        this.username = username;
        this.password = password;

        login();
    }

    private void login() {


        //Request URL
        final Request request = new Request.Builder()
                .url("http://136.233.14.3:8282/CampusPortalSOA/login")
                .post(RequestBody.create(JSON, String.valueOf(jsonEntity.getIdpasswordJsonObject())))
                .build();
        // New Asynchronous call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                message.postValue("ITER SERVER Down");
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    Response lResponse = response;
                    Log.d(TAG, "onResponse: code " + lResponse.code());
                    JSONObject lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());

                    if (lJsonResponse.getString("status").equals("success")) {
                        cookie.save(Objects.requireNonNull(lResponse.header("Set-Cookie")).substring(11, 39)); // Saves Cookie
                        Log.d(TAG, "onResponse: " + Objects.requireNonNull(lResponse.header("Set-Cookie")).substring(11, 39));
                        student.saveName(lJsonResponse.getString("name"));
                        student.saveUserIDAndPassword(username, password);
                        student.saveLoginStatus(lJsonResponse.getString("status"));
                        message.postValue(lJsonResponse.getString("message"));
                        fetchRegistrationID();
                    } else {
                        student.saveLoginStatus(lJsonResponse.getString("status"));
                        message.postValue(lJsonResponse.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });
    }

    /* Getting Student Registration ID */
    private void fetchRegistrationID() {
        Request request = new Request.Builder()
                .url("http://136.233.14.3:8282/CampusPortalSOA/studentSemester/lov")
                .addHeader("Cookie", "JSESSIONID=" + cookie.getCookie())
                .post(RequestBody.create(JSON, String.valueOf(jsonEntity.getEmptyJsonArray())))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure:fetch reg id   " + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {
                    Log.d(TAG, "fetch regid ok");
                    Response lResponce = response;
                    JSONObject lJsoneResponse = new JSONObject(Objects.requireNonNull(lResponce.body()).source().readUtf8());
                    student.saveRegistrationID(lJsoneResponse
                            .getJSONArray("studentdata")
                            .getJSONObject(0)
                            .getString("REGISTRATIONID"));
                    fetchAttendance(student.getRegistrationID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //getAttendance
    public void fetchAttendance(String registrationID) {

        Request request = new Request.Builder()
                .url("http://136.233.14.3:8282/CampusPortalSOA/attendanceinfo")
                .addHeader("Cookie", "JSESSIONID=" + cookie.getCookie())
                .post(RequestBody.create(JSON, "{registerationid:" + registrationID + "}"))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure:fetch attendance data   " + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "fetch attendacne okey");
                Response lResponse = response;
                JSONObject lJsonResponse;
                try {
                    lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());
                    if (new AttendanceData(lJsonResponse.toString()).numberOfSubjects() != 0){
                        student.saveAttendance(lJsonResponse.toString());
                        data.postValue(student.getAttendance());
                    }else {
                        data.postValue("NODATA");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @NonNull
    public MutableLiveData<String> getData() {
        return data;
    }

    @NonNull
    public MutableLiveData<String> getMessage() {
        return message;
    }


}
