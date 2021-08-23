package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class UserUbidotsAPIkeyLoading_Searching extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String APIExistPreference = "APIExist";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_apikey_loading_searching);
    }

}