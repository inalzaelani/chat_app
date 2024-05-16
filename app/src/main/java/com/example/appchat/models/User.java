package com.example.appchat.models;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class User {
    private String phone;
    private String userName;
    private Timestamp createdTimeStamp;

    public User(String phone, String userName, Timestamp createdTimeStamp) {
        this.phone = phone;
        this.userName = userName;
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(Timestamp createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }
}
