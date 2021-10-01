package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SystemAdminUserManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_user_management);
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.systemAdminUserManagement_tableRow_addSystemAdmin:
                intent = new Intent(this, SystemAdminAddSystemAdmin.class);
                break;

            case R.id.systemAdminUserManagement_tableRow_manageUser:
                intent = new Intent(this, ManageUserOrEmployee.class);
                break;
        }

        startActivity(intent);
    }
}