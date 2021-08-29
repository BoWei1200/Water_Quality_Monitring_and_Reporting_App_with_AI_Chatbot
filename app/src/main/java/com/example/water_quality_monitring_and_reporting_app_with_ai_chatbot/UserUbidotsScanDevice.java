package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class UserUbidotsScanDevice extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String scannedDeviceExistPreference = "scannedDeviceExist";
    private final String stopSensorPreference = "stopSensor";
    private final String currentRunningSensorPreference = "currentRunningSensor";

    private Switch userUbidotsScanDevice_switch_sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_scan_device);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        userUbidotsScanDevice_switch_sensor = findViewById(R.id.userUbidotsScanDevice_switch_sensor);
    }

    public void save(View view) {
        SharedPreferences.Editor editor = mPreferences.edit();

        if(userUbidotsScanDevice_switch_sensor.isChecked()){
            editor.putString(scannedDeviceExistPreference, "1");
            editor.putString(stopSensorPreference, "");
        }else{
            editor.putString(scannedDeviceExistPreference, "");
            editor.putString(stopSensorPreference, "1");
        }
        editor.putString(currentRunningSensorPreference, "");
        editor.commit();

        startActivity(new Intent(this, Login.class));
        finish();
    }
}