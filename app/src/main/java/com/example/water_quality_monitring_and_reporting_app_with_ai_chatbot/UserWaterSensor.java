package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

public class UserWaterSensor implements Runnable{
    Boolean stop = false;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String stopSensorPreference = "stopSensor";
    private final String finishDetectingPreference = "finishDetecting";
    private final String completeGetDataFromUbidotsPreference = "completeGetDataFromUbidots";
    private Context context;

    //private final String demoVariableID = "60f2b0be4763e74e29fcc3aa";

    private String API_KEY = "";
    private ApiClient apiClient;
    private Variable[] variables;

    public UserWaterSensor(Context context, int MODE_PRIVATE, String API_KEY) {
        this.context = context;
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        this.API_KEY = API_KEY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run() {
        int i = 0;
        boolean countBlock = false;
        int polluteUntil = 0;
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

        for (Variable variable : variables) {
            if (variable.getName().equals("bod"))
                BOD = variable;

            if (variable.getName().equals("cod"))
                COD = variable;

            if (variable.getName().equals("do"))
                DO = variable;

            if (variable.getName().equals("nh3n"))
                NH3N = variable;

            if (variable.getName().equals("ph"))
                pH = variable;

            if (variable.getName().equals("ss"))
                SS = variable;
        }
        //SharedPreferences.Editor editor = mPreferences.edit();

        while (!stop) {
//            editor.putString(finishDetectingPreference, "");
//            editor.commit();

            System.out.println("detect for "+ i++ +" times");

            try{
                System.out.println("API in Thread: " + API_KEY);

                //clean water
                double doVal = (Math.random() * (120-80)) + 80;
                double bodVal = (Math.random() * (5-1)) + 1;
                double codVal = (Math.random() * (20-5)) + 5;
                double nh3nVal = (Math.random() * (3-0)) + 0;
                double ssVal = (Math.random() * 50);
                double pHVal = (Math.random() * (8.5-7)) + 7;

                Value[] valuesNH3N = NH3N.getValues();
                double nh3nValRead = Double.parseDouble(String.valueOf(valuesNH3N[0].getValue()));
                if(nh3nValRead > 3){
                    int polluteForNTimes = (int)(Math.random() * (10-5)) + 5;

                    if(!countBlock){
                        polluteUntil = i + polluteForNTimes;
                        countBlock = true;
                    }
                    System.out.println("polluteUntil" +  polluteUntil);

                    if (i <= polluteUntil){
                        nh3nVal = (Math.random() * ((nh3nValRead + 25) -nh3nValRead)) + nh3nValRead;

                        bodVal = (Math.random() * (100-20)) + 20;
                        codVal = (Math.random() * (100-20)) + 20;
                        ssVal = (Math.random() * (100-50)) + 50;
                        doVal = (Math.random() * 50);
                    }
                    else{
                        nh3nVal = (Math.random() * (3-0)) + 0;
                        countBlock = false;
                    }
                }

                DO.saveValue(doVal);
                BOD.saveValue(bodVal);
                COD.saveValue(codVal);
                NH3N.saveValue(nh3nVal);
                SS.saveValue(ssVal);
                pH.saveValue(pHVal);

                Value[] valuesDO = DO.getValues();
                Value[] valuesBOD = BOD.getValues();
                Value[] valuesCOD = COD.getValues();
                 valuesNH3N = NH3N.getValues();
                Value[] valuesSS = SS.getValues();
                Value[] valuespH = pH.getValues();

                UserIoTValues userIoTValues = new UserIoTValues(valuesDO, valuesBOD, valuesCOD, valuesNH3N, valuesSS, valuespH);

                UserIoTWQICalculation WQIcalc = new UserIoTWQICalculation(
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesDO()[0].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesBOD()[0].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesCOD()[0].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesNH3N()[0].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesSS()[0].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuespH()[0].getValue())));

                WQIcalc.calculateWQI();
                double calculatedWQI = WQIcalc.getWQI();

                String status = "";

                if(calculatedWQI < 51.8){
                    if(calculatedWQI < 31.0){
                        status = "heavily polluted";
                    }else{
                        status = "polluted";
                    }
                    int drawable = R.drawable.appicon;
                    String notificationTitle = "Water from your faucet is " + status + "!";
                    String notificationText = "Detected WQI: " + String.format("%.2f", calculatedWQI);
                    NotificationSender notificationSender = new NotificationSender(context, drawable, notificationTitle, notificationText, "WQI");
                    notificationSender.start();
                }

            }catch(Exception e){
                System.out.println("ERROR IN SAVING VALUE: "+e.toString());
            }

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Thread class: error: "+e.toString());
            }

            System.out.println(stop);
            stop = getStop();
//            editor.putString(finishDetectingPreference, "1");
//            editor.commit();
        }
    }

    public Boolean getStop(){
        return (mPreferences.getString(stopSensorPreference, null).equals("1"));
    }

}
