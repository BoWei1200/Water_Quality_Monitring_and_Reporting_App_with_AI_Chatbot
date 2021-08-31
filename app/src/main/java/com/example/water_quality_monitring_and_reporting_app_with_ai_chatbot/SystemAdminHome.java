package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class SystemAdminHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";

    private TextView systemAdminHome_txt_numOfOrg;

    private ImageView systemAdminHome_img_setting;
    private PopupMenu systemAdminHome_popupMenu_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        systemAdminHome_txt_numOfOrg = findViewById(R.id.systemAdminHome_txt_numOfOrg);

        systemAdminHome_img_setting = findViewById(R.id.investigatorHome_img_setting);

        systemAdminHome_popupMenu_setting = new PopupMenu(this, systemAdminHome_img_setting);
        systemAdminHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, systemAdminHome_popupMenu_setting.getMenu());
        systemAdminHome_popupMenu_setting.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.userSettingMenu_logout:
                        SharedPreferences.Editor editor = mPreferences.edit();

                        editor.clear();

                        editor.apply();

                        Intent i = new Intent(SystemAdminHome.this, Login.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        int registeredOrg = dbHelper.getOrgNum();

        systemAdminHome_txt_numOfOrg.setText(Integer.toString(registeredOrg));
    }

    public void settings(View view) {
        systemAdminHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.systemAdminHome_btn_manageOrg:
                intent = new Intent(this, SystemAdminOrgManagement.class);
                break;

            case R.id.systemAdminHome_btn_manageNews:
                intent = new Intent(this, NewsManagement.class);
                break;
        }

        startActivity(intent);
    }
}