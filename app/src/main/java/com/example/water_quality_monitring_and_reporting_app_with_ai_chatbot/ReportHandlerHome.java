package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class ReportHandlerHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";

    private ImageView reportHandlerHome_img_setting;

    private PopupMenu reportHandlerHome_popupMenu_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_handler_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        reportHandlerHome_img_setting = findViewById(R.id.reportHandlerHome_img_setting);

        reportHandlerHome_popupMenu_setting = new PopupMenu(this, reportHandlerHome_img_setting);
        reportHandlerHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, reportHandlerHome_popupMenu_setting.getMenu());
        reportHandlerHome_popupMenu_setting.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()){
                case R.id.userSettingMenu_logout:
                    SharedPreferences.Editor editor = mPreferences.edit();

                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(ReportHandlerHome.this, Login.class);
                    startActivity(i);
                    finish();
                    break;
            }
            return true;
        });
    }

    public void settings(View view) {
        reportHandlerHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.reportHandlerHome_cv_reportFromUser:
                intent = new Intent(this, ReportHandlerReportFromUser.class);
                break;

            case R.id.reportHandlerHome_cv_news:
                intent = new Intent(this, NewsList.class);
                break;
        }

        startActivity(intent);
    }
}