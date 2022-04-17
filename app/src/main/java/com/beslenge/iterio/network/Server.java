package com.beslenge.iterio.network;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beslenge.iterio.Student;
import com.beslenge.iterio.data.StudentPreferences;
import com.beslenge.iterio.data.ServerPreferences;
import com.beslenge.iterio.model.AttendanceData;
import com.beslenge.iterio.model.JsonEntity;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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

@SuppressWarnings({"UnnecessaryLocalVariable", "NullableProblems"})
public class Server {
    private static int error404counter = 0;
    public final String TAG = "BOB";
    private final OkHttpClient client = new OkHttpClient(); // client for networking
    private final JsonEntity jsonEntity = new JsonEntity();
    private final MediaType JSON = MediaType.parse("application/json"); // Request Media Type
    @NonNull
    private final StudentPreferences studentPreferences;
    @NonNull
    private final MutableLiveData<String> serverResponseData;
    @NonNull
    private final MutableLiveData<String> serverResponseMessage;
    @NonNull
    private final ServerPreferences serverPreferences;
    private Student student;
    private String IPADDRESS;
    
    public Server(@NonNull Context context) {
        serverPreferences = new ServerPreferences(context);
        serverResponseData = new MutableLiveData<>();
        serverResponseMessage = new MutableLiveData<>();
        studentPreferences = new StudentPreferences(context);
    }
    
    
    public void setUserAndPasswordAndFetchData(Student student, int activityTag) {
        this.student = student;
        jsonEntity.setIdPasswordJsonObject(student.getUserName(), student.getPassword());
        if (activityTag == 0) {
            fetchIp();
        } else {
            login();
        }
        
    }
    
    private void login() {
        
        IPADDRESS = serverPreferences.getIp();
        
        final Request request = new Request.Builder()
                .url("http://" + IPADDRESS + "/CampusPortalSOA/login")
                .post(RequestBody.create(String.valueOf(jsonEntity.getIdPasswordJsonObject()), JSON))
                .build();
        // New Asynchronous call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (error404counter == 7) {
                    error404counter = 0;
                    fetchIp();
                } else {
                    serverResponseMessage.postValue("ITER SERVER DOWN");
                }
                error404counter++;
                
                
            }
            
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                error404counter = 0;
                try {
                    Response lResponse = response;
    
                    JSONObject lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());
    
                    if (lJsonResponse.getString("status").equals("success")) {
                        serverPreferences.saveCookie(Objects.requireNonNull(lResponse.header("Set-Cookie")).substring(11, 39)); // Saves Cookie
                        studentPreferences.saveName(lJsonResponse.getString("name"));
                        studentPreferences.saveUserIDAndPassword(student.getUserName(), student.getPassword());
                        studentPreferences.saveLoginStatus(lJsonResponse.getString("status"));
                        serverResponseMessage.postValue(lJsonResponse.getString("message"));
                        fetchRegistrationID();
                    } else {
                        studentPreferences.saveLoginStatus(lJsonResponse.getString("status"));
                        serverResponseMessage.postValue(lJsonResponse.getString("message"));
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
                .url("http://" + IPADDRESS + "/CampusPortalSOA/studentSemester/lov")
                .addHeader("Cookie", "JSESSIONID=" + serverPreferences.getCookie())
                .post(RequestBody.create(String.valueOf(jsonEntity.getEmptyJsonArray()), JSON))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                serverResponseData.postValue("NODATA");
            }
    
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
    
                    Response lResponce = response;
                    JSONObject lJsoneResponse = new JSONObject(Objects.requireNonNull(lResponce.body()).source().readUtf8());
                    String ID = lJsoneResponse
                            .getJSONArray("studentdata")
                            .getJSONObject(0)
                            .getString("REGISTRATIONID");
                    if (!ID.isEmpty()) {
                        studentPreferences.saveRegistrationID(ID);
                        fetchAttendance(studentPreferences.getRegistrationID());
                    } else {
                        serverResponseData.postValue("NODATA");
    
                    }
                } catch (JSONException e) {
                    serverResponseData.postValue("NODATA");
                    e.printStackTrace();
                }
            }
        });
    }
    
    //getAttendance
    public void fetchAttendance(String registrationID) {
    
        Request request = new Request.Builder()
                .url("http://" + IPADDRESS + "/CampusPortalSOA/attendanceinfo")
                .addHeader("Cookie", "JSESSIONID=" + serverPreferences.getCookie())
                .post(RequestBody.create("{registerationid:" + registrationID + "}", JSON))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                serverResponseData.postValue("NODATA");
    
            }
    
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        
                Response lResponse = response;
                JSONObject lJsonResponse;
                try {
                    lJsonResponse = new JSONObject(Objects.requireNonNull(lResponse.body()).source().readUtf8());
                    if (new AttendanceData(lJsonResponse.toString()).numberOfSubjects() != 0) {
        
                        studentPreferences.saveAttendance(lJsonResponse.toString());
                        serverResponseData.postValue(lJsonResponse.toString());
                    } else {
                        serverResponseData.postValue("NODATA");
        
                    }
    
    
                } catch (JSONException e) {
                    e.printStackTrace();
    
                }
        
        
            }
        });
    }
    
    public void triggerClient() {
    
    }
    
    private void fetchIp() {
        Request request = new Request.Builder().url("https://soa.ac.in/iter")
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                serverResponseMessage.postValue("SOA Website Down. Try after sometime");
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                Document document = Jsoup.parse(Objects.requireNonNull(response.body()).source().readUtf8());
                Elements link = document.select("a");
                for (Element ele : link) {
                    if (ele.toString().contains("/CampusPortalSOA/")) {
                        String url = ele.attr("href");
                        String ip = url.substring(url.indexOf("//") + 2, url.indexOf("/Campus"));
                        serverPreferences.saveIp(ip);
                        
                        login();
                    }
                }
            }
        });
    }
    
    @NonNull
    public MutableLiveData<String> getServerResponseData() {
        return serverResponseData;
    }
    
    @NonNull
    public MutableLiveData<String> getServerResponseMessage() {
        return serverResponseMessage;
    }
    
    
}
