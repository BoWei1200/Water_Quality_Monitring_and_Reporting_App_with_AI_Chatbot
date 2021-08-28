package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivitySuccessfulDisplay extends AppCompatActivity {
    private String passedActivity = "";
    private TextView activitySuccessful_txt_msgSuccessfulHeader, activitySuccessful_txt_msgSuccessfulDesc;
    private Button activitySuccessful_btn_proceedToSth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_display);

        Intent intent = getIntent();
        passedActivity = intent.getStringExtra("successfulDisplayIndicator");

        activitySuccessful_txt_msgSuccessfulHeader = findViewById(R.id.activitySuccessful_txt_msgSuccessfulHeader);
        activitySuccessful_txt_msgSuccessfulDesc = findViewById(R.id.activitySuccessful_txt_msgSuccessfulDesc);

        activitySuccessful_btn_proceedToSth = findViewById(R.id.activitySuccessful_btn_proceedToSth);

        displaySuccessfulContent(passedActivity);
    }

    private void displaySuccessfulContent(String passedActivity) {
        String header = "", desc = "", proceedToSth = "";
        switch(passedActivity){
            case "registration":
                header = "Registration Successful!";
                desc = "You can now setup the connection of your sensor to monitor your water quality";
                proceedToSth = "Setup Now";

                break;
        }

        activitySuccessful_txt_msgSuccessfulHeader.setText(header);
        activitySuccessful_txt_msgSuccessfulDesc.setText(desc);
        activitySuccessful_btn_proceedToSth.setText(proceedToSth);
    }

    public void proceedToSth(View view) {
        switch(passedActivity){
            case "registration":
                startActivity(new Intent(this, UserUbidotsAccSetup.class));
                break;

        }
    }

    public void skip(View view) {
        switch(passedActivity){
            case "registration":
                startActivity(new Intent(this, Login.class));
                break;
        }
    }
}