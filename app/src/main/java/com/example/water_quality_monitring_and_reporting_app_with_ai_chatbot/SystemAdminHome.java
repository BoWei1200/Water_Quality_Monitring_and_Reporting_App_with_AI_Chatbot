package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class SystemAdminHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";

    private ImageView systemAdminHome_img_setting;
    private PopupMenu systemAdminHome_popupMenu_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        systemAdminHome_img_setting = findViewById(R.id.systemAdminHome_img_setting);

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

    public void settings(View view) {
        systemAdminHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }
}