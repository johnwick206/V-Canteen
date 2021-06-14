package com.example.canteenapp;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserData {
    public static String name, phoneNumber, emailId;


    public UserData() {
        //for firebase
    }

    public UserData(String name, String phoneNumber, String emailId) {
        UserData.name = name;
        UserData.phoneNumber = phoneNumber;
        UserData.emailId = emailId;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }
}
