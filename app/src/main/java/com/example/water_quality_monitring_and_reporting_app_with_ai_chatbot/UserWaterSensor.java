package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.SharedPreferences;

import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Variable;

public class UserWaterSensor implements Runnable{
    Boolean stop = false;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String stopSensorPreference = "stopSensor";
    private final String completeGetDataFromUbidotsPreference = "completeGetDataFromUbidots";

    //private final String demoVariableID = "60f2b0be4763e74e29fcc3aa";

    private String API_KEY = "";
    private ApiClient apiClient;
    private Variable[] variables;

    public UserWaterSensor(Context context, int MODE_PRIVATE, String API_KEY) {
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        this.API_KEY = API_KEY;
    }

    public void run() {
        int i = 0;
        //stop = get from session to stop
        apiClient = new ApiClient(API_KEY);

        DataSource[] devices = apiClient.getDataSources();

        DataSource device = null;

        for (int j = 0; j < devices.length; j++){
            if(devices[j].getName().equals("Water Quality Monitoring")){
                device = devices[j];
                break;
            }
        }

        variables = device.getVariables();

        Variable BOD = null;
        Variable COD = null;
        Variable DO = null;
        Variable NH3N = null;
        Variable pH = null;
        Variable SS = null;

        for (int j=0; j < variables.length; j++){
            if(variables[j].getName().equals("bod"))
                BOD = variables[j];

            if(variables[j].getName().equals("cod"))
                COD = variables[j];

            if(variables[j].getName().equals("do"))
                DO = variables[j];

            if(variables[j].getName().equals("nh3n"))
                NH3N = variables[j];

            if(variables[j].getName().equals("ph"))
                pH = variables[j];

            if(variables[j].getName().equals("ss"))
                SS = variables[j];
        }

        while (!stop) {
            System.out.println("detect for "+ i++ +" times");

            try{
                System.out.println("API in Thread: " + API_KEY);

                //clean water
                double doVal = (Math.random() * (120-80)) + 80;
                double bodVal = (Math.random() * (5-1)) + 1;
                double codVal = (Math.random() * (20-5)) + 5;
                double nh3nVal = (Math.random() * (4-0)) + 0;
                double ssVal = (Math.random() * 50);
                double pHVal = (Math.random() * (8.5-7)) + 7;

//                if(pollutantIsDetected){
//                    nh3nVal = (Math.random() * (100-4)) + 4;
//
//                    bodVal = (Math.random() * (5-1)) + 1;
//                    do---;
//                }

                DO.saveValue(doVal);
                BOD.saveValue(bodVal);
                COD.saveValue(codVal);
                NH3N.saveValue(nh3nVal);
                SS.saveValue(ssVal);
                pH.saveValue(pHVal);

            }catch(Exception e){
                System.out.println("ERROR IN SAVING VALUE: "+e.toString());
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread class: error: "+e.toString());
            }

            stop = getStop();
        }
    }

    public Boolean getStop(){
        return (mPreferences.getString(stopSensorPreference, null).equals("1"));
    }

    public Boolean getCompleteGetDataFromUbidots(){
        return (mPreferences.getString(completeGetDataFromUbidotsPreference, null).equals("1"));
    }

}
