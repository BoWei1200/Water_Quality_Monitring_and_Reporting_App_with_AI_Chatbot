package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.view.View;

import android.os.Bundle;


public class UserHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
    }

    public void toReportMenu(View view) {
        Intent intent = new Intent(this, UserReportMenu.class);
        startActivity(intent);
        finish();
    }

    public void toHome(View view) {

    }
}