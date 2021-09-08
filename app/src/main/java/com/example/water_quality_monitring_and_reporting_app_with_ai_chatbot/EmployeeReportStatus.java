package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

public class EmployeeReportStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_report_status);

        Toolbar toolbar = findViewById(R.id.employeeReportStatus_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    public void viewImage(View view) {
    }

    public void toImageViewer(View view) {
    }

    public void viewPrevious(View view) {
    }

    public void viewNext(View view) {
    }

    public void upload(View view) {
    }

    public void removeFile(View view) {
    }

    public void submit(View view) {
    }

    public void approveOrReject(View view) {
        switch (view.getId()){
            case R.id.employeeReportStatus_btn_approve:
                break;

            case R.id.employeeReportStatus_btn_reject:
                break;
        }
    }
}