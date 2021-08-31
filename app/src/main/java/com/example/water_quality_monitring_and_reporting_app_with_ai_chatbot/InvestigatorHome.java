package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class InvestigatorHome extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";

    private ImageView investigatorHome_img_setting, investigatorHome_img_refresh;
    private PopupMenu investigatorHome_popupMenu_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator_home);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        investigatorHome_img_setting =findViewById(R.id.investigatorHome_img_setting);
        investigatorHome_img_refresh = findViewById(R.id.investigatorHome_img_refresh);

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
    }

    public void settings(View view) {
        investigatorHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }
}