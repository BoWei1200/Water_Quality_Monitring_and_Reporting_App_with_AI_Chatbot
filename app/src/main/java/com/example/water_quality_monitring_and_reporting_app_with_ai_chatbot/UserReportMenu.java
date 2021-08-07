package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserReportMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_menu);
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        Boolean needFinish = true;

        switch(view.getId()){
            case R.id.userReportMenu_btn_bottomMenuAIChat:
                intent = new Intent(this, UserAIChatting.class);
                break;

            case R.id.userReportMenu_tableRow_myReport:
                intent = new Intent(this, UserMyReport.class);
                needFinish = false;
                break;

            case R.id.userReportMenu_tableRow_addReport:
                intent = new Intent(this, UserAddReportDraft.class);
                needFinish = false;
                break;
        }

        startActivity(intent);

        if(needFinish)
            finish();
    }

    public void toHome(View view) {
        finish();
    }
}