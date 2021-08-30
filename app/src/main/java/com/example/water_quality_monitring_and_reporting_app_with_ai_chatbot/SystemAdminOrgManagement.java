package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class SystemAdminOrgManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_org_management);

        Toolbar toolbar = findViewById(R.id.systemAdminManageOrg_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch (view.getId()){
            case R.id.systemAdminManageOrg_tableRow_addOrg:
                intent = new Intent(this, SystemAdminAddOrg.class);
                break;

            case R.id.systemAdminManageOrg_tableRow_manageOrg:
                intent = new Intent(this, SystemAdminManageOrg.class);
                break;
        }

        startActivity(intent);
    }
}