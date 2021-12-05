package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class AdminHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";
    private final String orgIDPreference = "orgID";

    private String getUserIDPreference = "";
    private String teamID, teamName;

    private LinearLayout adminHome_linearLayout_notice;

    private ImageView adminHome_img_setting;

    private TextView adminHome_txt_orgName, adminHome_txt_notice;

    private PopupMenu adminHome_popupMenu_setting;

    private Cursor cursorGetOrgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        adminHome_linearLayout_notice = findViewById(R.id.adminHome_linearLayout_notice);

        adminHome_txt_orgName = findViewById(R.id.examinerHome_txt_orgName);
        adminHome_txt_notice = findViewById(R.id.adminHome_txt_notice);

        adminHome_img_setting = findViewById(R.id.adminHome_img_setting);

        adminHome_popupMenu_setting = new PopupMenu(this, adminHome_img_setting);
        adminHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, adminHome_popupMenu_setting.getMenu());
        adminHome_popupMenu_setting.setOnMenuItemClickListener(item -> {
            switch(item.getItemId()){
                case R.id.userSettingMenu_logout:
                    SharedPreferences.Editor editor = mPreferences.edit();

                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(AdminHome.this, Login.class);
                    startActivity(i);
                    finish();
                    break;
            }
            return true;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        cursorGetOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
        cursorGetOrgInfo.moveToFirst();
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(orgIDPreference, cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgID")));
        editor.commit();


        try {
            if(cursorGetOrgInfo.moveToFirst()){
                String orgName = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName"));
                adminHome_txt_orgName.setText(orgName);
            }
        }catch (Exception e){
            System.out.println("ERROR: "+ e.toString());
        }

        setNotice();
    }

    public void settings(View view) {
        adminHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    protected void onRestart(){
        super.onRestart();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        cursorGetOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
        try {
            if(cursorGetOrgInfo.moveToFirst()){
                String orgName = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName"));
                adminHome_txt_orgName.setText(orgName);
            }
        }catch (Exception e){
            System.out.println("ERROR: "+ e.toString());
        }

        setNotice();
    }

    public void setNotice(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorGetOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);

        cursorGetOrgInfo.moveToFirst();

        String orgID = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgID"));

        int countEX = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "EX").getCount();
        int countIN = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "IN").getCount();
        int countRH = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "RH").getCount();

        String orgReady = cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgReady"));

        if(countEX != 0 && countIN != 0 && countRH != 0){
            if(!orgReady.equals("1")){
                adminHome_linearLayout_notice.setVisibility(View.VISIBLE);
                adminHome_txt_notice.setText("Your organization is now ready to deal with water pollution reports! " +
                        "You may switch on \"Receive reports from user\" by clicking here ");
            }
            else{
                adminHome_linearLayout_notice.setVisibility(View.GONE);
            }
        }
        else{
            adminHome_linearLayout_notice.setVisibility(View.VISIBLE);
            adminHome_txt_notice.setText("Oops! Your organization is not ready to deal with water pollution report. " +
                    "You should add examiner, investigator team and report handler before receiving water pollution reports");
        }

    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.adminHome_cv_org:
                intent = new Intent(this, OrgDetail.class);
                intent.putExtra("orgID", cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgID")));
                break;

            case R.id.adminHome_cv_employeeManagement:
                intent = new Intent(this, AdminEmployeeManagement.class);
                break;

            case R.id.adminHome_cv_newsManagement:
                intent = new Intent(this, NewsList.class);
                break;
        }

        startActivity(intent);
    }

    public void displayToast(String message){
        Toast.makeText(AdminHome.this,message,Toast.LENGTH_SHORT).show();
    }
}