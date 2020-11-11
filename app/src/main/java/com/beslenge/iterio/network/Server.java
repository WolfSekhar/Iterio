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
                Log.d(TAG, "Login connection failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "Login connection Success");
                try {
                    Response lResponse = response;

                    JSONObject lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());

                    if (lJsonResponse.getString("status").equals("success")) {
                        Log.d(TAG, "Login Successful");
                        cookie.save(Objects.requireNonNull(lResponse.header("Set-Cookie")).substring(11, 39)); // Saves Cookie
                        student.saveName(lJsonResponse.getString("name"));
                        student.saveUserIDAndPassword(username, password);
                        student.saveLoginStatus(lJsonResponse.getString("status"));
                        message.postValue(lJsonResponse.getString("message"));
                        fetchRegistrationID();
                    } else {
                        Log.d(TAG, "Login Unsuccessful");
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
                Log.d(TAG, "Registration Id connection Unsuccessful");
                data.postValue("NODATA");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "Registration Id connection SUccessful");
                try {

                    Response lResponce = response;
                    JSONObject lJsoneResponse = new JSONObject(Objects.requireNonNull(lResponce.body()).source().readUtf8());
                    String ID = lJsoneResponse
                            .getJSONArray("studentdata")
                            .getJSONObject(0)
                            .getString("REGISTRATIONID");
                    if (!ID.isEmpty()) {
                        Log.d(TAG, "Registration Id is successfully fetched");
                        student.saveRegistrationID(ID);
                        fetchAttendance(student.getRegistrationID());
                    } else {
                        data.postValue("NODATA");
                        Log.d(TAG, "Registration Id is Unsuccessfully fetched");
                    }
                } catch (JSONException e) {
                    data.postValue("NODATA");
                    e.printStackTrace();
                    Log.d(TAG, "Registration Id is Unsuccessfully fetched -- extraction of ID error");
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
                data.postValue("NODATA");
                Log.d(TAG, "Attedance Fetch Connection Unsuccessful");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "Attedance Fetch Connection successful");
                Response lResponse = response;
                JSONObject lJsonResponse;
                try {
                    lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());
                    if (new AttendanceData(lJsonResponse.toString()).numberOfSubjects() != 0){
                        Log.d(TAG, "Attendance Data Available");
                        student.saveAttendance(lJsonResponse.toString());
                        data.postValue(student.getAttendance());
                    }else {
                        data.postValue("NODATA");
                        Log.d(TAG, "Attendance Data Not Available");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Attendance Data NOT Available -- error during Extraction");
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
