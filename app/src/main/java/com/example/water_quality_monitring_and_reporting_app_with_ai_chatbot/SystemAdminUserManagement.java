package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;



public class SystemAdminUserManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_user_management);

        Toolbar toolbar = findViewById(R.id.systemAdminUserManagement_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.systemAdminUserManagement_tableRow_addSystemAdmin:
                intent = new Intent(this, SystemAdminAddAdmin.class);
                intent.putExtra("addWhichAdmin", "SAD");
                break;

            case R.id.systemAdminUserManagement_tableRow_manageUser:
                intent = new Intent(this, ManageUserOrEmployee.class);
                break;
        }

        startActivity(intent);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}