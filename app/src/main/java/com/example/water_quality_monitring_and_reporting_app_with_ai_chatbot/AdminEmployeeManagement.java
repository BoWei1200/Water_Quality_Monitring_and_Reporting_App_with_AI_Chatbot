package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AdminEmployeeManagement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_management);

        Toolbar toolbar = findViewById(R.id.adminEmployeeManagement_toolbar);
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

        switch(view.getId()){
            case R.id.adminEmployeeManagement_tableRow_addEmployee:
                intent = new Intent(this, AdminAddEmployee.class);
                break;

            case R.id.adminEmployeeManagement_tableRow_manageEmployee:
                intent = new Intent(this, ManageUserOrEmployee.class);
                break;

            case R.id.adminEmployeeManagement_tableRow_addInvestigationTeam:
                intent = new Intent(this, AdminAddInvestigationTeam.class);
                break;

            case R.id.adminEmployeeManagement_tableRow_manageInvestigationTeam:
                intent = new Intent(this, AdminManageInvestigationTeam.class);
                break;
        }

        startActivity(intent);
    }
}