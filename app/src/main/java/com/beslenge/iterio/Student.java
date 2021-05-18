package com.beslenge.iterio;

public class Student {
    protected String userName;
    protected String password;
    
    public Student(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getPassword() {
        return password;
    }
}
