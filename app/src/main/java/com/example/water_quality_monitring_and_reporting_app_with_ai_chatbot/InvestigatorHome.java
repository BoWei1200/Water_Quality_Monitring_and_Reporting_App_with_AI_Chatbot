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

public class InvestigatorHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";
    private String teamID, teamName;

    private ImageView investigatorHome_img_setting, investigatorHome_img_refresh;
    private PopupMenu investigatorHome_popupMenu_setting;

    private TextView investigatorHome_txt_orgName, investigatorHome_txt_investigationTeamID, investigatorHome_txt_investigationTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        investigatorHome_img_setting =findViewById(R.id.investigatorHome_img_setting);
        investigatorHome_img_refresh = findViewById(R.id.investigatorHome_img_refresh);

        investigatorHome_txt_orgName = findViewById(R.id.investigatorHome_txt_orgName);
        investigatorHome_txt_investigationTeamID = findViewById(R.id.investigatorHome_txt_investigationTeamID);
        investigatorHome_txt_investigationTeamName = findViewById(R.id.investigatorHome_txt_investigationTeamName);

        investigatorHome_popupMenu_setting = new PopupMenu(this, investigatorHome_img_setting);
        investigatorHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, investigatorHome_popupMenu_setting.getMenu());
        investigatorHome_popupMenu_setting.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.userSettingMenu_logout:
                        SharedPreferences.Editor editor = mPreferences.edit();

                        editor.clear();
                        editor.apply();

                        Intent i = new Intent(InvestigatorHome.this, Login.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursorGetOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
        Cursor cursorGetInvestigatorTeamInfo = dbHelper.getInvestigatorTeamInfoByUserID(getUserIDPreference);

        try {
            if(cursorGetOrgInfo.moveToFirst() && cursorGetInvestigatorTeamInfo.moveToFirst()){
                String orgName = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName"));

                teamID = cursorGetInvestigatorTeamInfo.getString(cursorGetInvestigatorTeamInfo.getColumnIndex("investigationTeamID"));
                teamName = cursorGetInvestigatorTeamInfo.getString(cursorGetInvestigatorTeamInfo.getColumnIndex("investigationTeamName"));

                investigatorHome_txt_orgName.setText(orgName);

                investigatorHome_txt_investigationTeamID.setText("Team ID: " + teamID);
                investigatorHome_txt_investigationTeamName.setText(teamName);
            }
        }catch (Exception e){
            System.out.println("ERROR: "+ e.toString());
        }
    }

    public void settings(View view) {
        investigatorHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.investigatorHome_cv_myInvestigationTeam:
                intent = new Intent(this, InvestigatorTeamDetail.class);
                intent.putExtra("teamID", teamID);
                intent.putExtra("teamName", teamName);
                break;

            case R.id.investigatorHome_cv_pendingInvestigation:
                intent = new Intent(this, InvestigatorInvestigation.class);
                intent.putExtra("passedActivity", "pending");
                break;

            case R.id.investigatorHome_cv_myInvestigation:
                intent = new Intent(this, InvestigatorInvestigation.class);
                intent.putExtra("passedActivity", "completed");
                break;

            case R.id.investigatorHome_cv_news:
                intent = new Intent(this, NewsList.class);
                break;
        }

        startActivity(intent);
    }
}