package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.WorkerThread;
import androidx.core.content.ContextCompat;

public class UserWaterSensor implements Runnable{
    Boolean stop = false;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String stopSensorPreference = "stopSensor";

    private int monitorFrequency;

    public UserWaterSensor(Context context, int MODE_PRIVATE) {
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    public void run() {
        // we add 100 new entries

        int i = 0;
        //stop = get from session to stop

        while (!stop) {

            //continuously detect water quality and store it in ubidots
            //need try catch for network connection
            System.out.println("detect for "+ i++ +" times");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread class: error: "+e.toString());
            }

            stop = getStop();
        }

    }

    public void stopThread(){
        stop = true;
    }

    public Boolean getStop(){
        return (mPreferences.getString(stopSensorPreference, null).equals("1"));
    }

}
