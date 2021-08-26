package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

public class UserWaterSensor extends Thread{
    private Boolean stop = false;

    public UserWaterSensor() {

    }

    public void run() {
        // we add 100 new entries
        int i = 0;
        while (!stop) {

            //detect water quality and store it in ubidots
            System.out.println("detect for " + i + "times");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void stopThread(){
        stop = true;
    }
}
