package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class ExaminerHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";
    private String teamID, teamName;

    private ImageView examinerHome_img_setting;

    private TextView examinerHome_txt_orgName;

    private PopupMenu examinerHome_popupMenu_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examiner_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        examinerHome_txt_orgName = findViewById(R.id.examinerHome_txt_orgName);
        examinerHome_img_setting = findViewById(R.id.examinerHome_img_setting);

        examinerHome_popupMenu_setting = new PopupMenu(this, examinerHome_img_setting);
        examinerHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, examinerHome_popupMenu_setting.getMenu());
        examinerHome_popupMenu_setting.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()){
                case R.id.userSettingMenu_logout:
                    SharedPreferences.Editor editor = mPreferences.edit();

                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(ExaminerHome.this, Login.class);
                    startActivity(i);
                    finish();
                    break;
            }
            return true;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursorGetOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
        Cursor cursorGetInvestigatorTeamInfo = dbHelper.getInvestigatorTeamInfoByUserID(getUserIDPreference);

        try {
            if(cursorGetOrgInfo.moveToFirst() && cursorGetInvestigatorTeamInfo.moveToFirst()){
                String orgName = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName"));
                examinerHome_txt_orgName.setText(orgName);
            }
        }catch (Exception e){
            System.out.println("ERROR: "+ e.toString());
        }
    }

    public void settings(View view) {
        examinerHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.examinerHome_cv_examinationOnReport:
                intent = new Intent(this, ExaminerExamination.class);
                break;

            case R.id.examinerHome_cv_news:
                intent = new Intent(this, NewsList.class);
                break;
        }

        startActivity(intent);
    }
}