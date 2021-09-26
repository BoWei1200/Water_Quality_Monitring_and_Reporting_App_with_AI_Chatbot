package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView adminHome_img_setting;

    private TextView adminHome_txt_orgName;

    private PopupMenu adminHome_popupMenu_setting;

    private Cursor cursorGetOrgInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        adminHome_txt_orgName = findViewById(R.id.examinerHome_txt_orgName);
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
    }

    public void settings(View view) {
        adminHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
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