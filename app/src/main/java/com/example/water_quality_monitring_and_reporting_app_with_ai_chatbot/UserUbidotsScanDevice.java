package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

public class UserUbidotsScanDevice extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String scannedDeviceExistPreference = "scannedDeviceExist";

    private Switch serUbidotsScanDevice_switch_sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_scan_device);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        serUbidotsScanDevice_switch_sensor = findViewById(R.id.serUbidotsScanDevice_switch_sensor);
    }


    public void save(View view) {
        SharedPreferences.Editor editor = mPreferences.edit();

        if(serUbidotsScanDevice_switch_sensor.isChecked()){
            editor.putString(scannedDeviceExistPreference, "1");
        }else{
            editor.putString(scannedDeviceExistPreference, "");
        }

        editor.commit();

        startActivity(new Intent(this, Login.class));
        finish();
    }
}