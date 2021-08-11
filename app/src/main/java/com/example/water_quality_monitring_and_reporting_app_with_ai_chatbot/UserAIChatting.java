package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserAIChatting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_aichatting);
    }

    public void toHome(View view) {
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        Boolean needFinish = true;

        switch(view.getId()){
            case R.id.userAIChatting_btn_bottomMenuReport:
                intent = new Intent(this, UserReportMenu.class);
                break;
        }

        startActivity(intent);

        if(needFinish)
            finish();
    }
}