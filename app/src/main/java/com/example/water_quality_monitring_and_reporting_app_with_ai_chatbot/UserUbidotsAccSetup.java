package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class UserUbidotsAccSetup extends AppCompatActivity {
    private TextView userUbidotsAccSetup_txt_step1Desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_acc_setup);

        userUbidotsAccSetup_txt_step1Desc = findViewById(R.id.userUbidotsAccSetup_txt_step1Desc);

        userUbidotsAccSetup_txt_step1Desc.setClickable(true);
        userUbidotsAccSetup_txt_step1Desc.setMovementMethod(LinkMovementMethod.getInstance());
        userUbidotsAccSetup_txt_step1Desc.setText(Html.fromHtml("Please register or login your Ubidots account <br/><br/>" +
                "        Click the link below for: <br/><br/>" +
                "        1. \tRegistration: <a href = \"https://industrial.ubidots.com/accounts/signup_industrial/\">https://industrial.ubidots.com/accounts/signup_industrial/</a> <br/>" +
                "        2. \tLogin: <a href = \"https://industrial.ubidots.com/accounts/signin/\">https://industrial.ubidots.com/accounts/signin/"));


    }
}