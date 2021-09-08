package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EmployeeReportStatus extends AppCompatActivity {
    private TextView employeeReportStatus_txt_reportID, employeeReportStatus_txt_reportDateHeader, employeeReportStatus_txt_reportTime,
            employeeReportStatus_txt_reportAddress, employeeReportStatus_txt_reportLaLongitude, employeeReportStatus_txt_reportOrg,
            employeeReportStatus_txt_reportDuration, employeeReportStatus_txt_reportCause, employeeReportStatus_txt_reportDesc,
            employeeReportStatus_txt_reportStatus,

            employeeReportStatus_txt_InvDocHeader, employeeReportStatus_txt_INDocURL,
            employeeReportStatus_txt_uploadedURL, employeeReportStatus_txt_resolvingDocURL;

    private ConstraintLayout employeeReportStatus_constraintLayout_images;

    private LinearLayout employeeReportStatus_linearLayout_previous, employeeReportStatus_linearLayout_next,
            employeeReportStatus_linearLayout_InvDoc, employeeReportStatus_linearLayout_uploadedURL,

            employeeReportStatus_linearLayout_resolvingDoc, employeeReportStatus_linearLayout_uploadedResolvingDocURL,

            employeeReportStatus_linearLayout_btns,
                    employeeReportStatus_linearLayout_btnSubmit, employeeReportStatus_linearLayout_btnApproveReject;


    private ImageView employeeReportStatus_img_pollutionPhoto;

    private Button employeeReportStatus_btn_upload, employeeReportStatus_btn_uploadResolvingDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_report_status);

        Toolbar toolbar = findViewById(R.id.employeeReportStatus_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        employeeReportStatus_txt_reportID = findViewById(R.id.employeeReportStatus_txt_reportID);
        employeeReportStatus_txt_reportDateHeader = findViewById(R.id.employeeReportStatus_txt_reportDateHeader);
        employeeReportStatus_txt_reportTime = findViewById(R.id.employeeReportStatus_txt_reportTime);
        employeeReportStatus_txt_reportAddress = findViewById(R.id.employeeReportStatus_txt_reportAddress);
        employeeReportStatus_txt_reportLaLongitude = findViewById(R.id.employeeReportStatus_txt_reportLaLongitude);
        employeeReportStatus_txt_reportOrg = findViewById(R.id.employeeReportStatus_txt_reportOrg);
        employeeReportStatus_txt_reportDuration = findViewById(R.id.employeeReportStatus_txt_reportDuration);
        employeeReportStatus_txt_reportCause = findViewById(R.id.employeeReportStatus_txt_reportCause);
        employeeReportStatus_txt_reportDesc = findViewById(R.id.employeeReportStatus_txt_reportDesc);
        employeeReportStatus_txt_reportStatus = findViewById(R.id.employeeReportStatus_txt_reportStatus);
        employeeReportStatus_txt_InvDocHeader = findViewById(R.id.employeeReportStatus_txt_InvDocHeader);
        employeeReportStatus_txt_INDocURL = findViewById(R.id.employeeReportStatus_txt_INDocURL);
        employeeReportStatus_txt_uploadedURL = findViewById(R.id.employeeReportStatus_txt_uploadedURL);
        employeeReportStatus_txt_resolvingDocURL = findViewById(R.id.employeeReportStatus_txt_resolvingDocURL);

        employeeReportStatus_constraintLayout_images = findViewById(R.id.employeeReportStatus_constraintLayout_images);

        employeeReportStatus_linearLayout_previous = findViewById(R.id.employeeReportStatus_linearLayout_previous);
        employeeReportStatus_linearLayout_next = findViewById(R.id.employeeReportStatus_linearLayout_next);
        employeeReportStatus_linearLayout_InvDoc = findViewById(R.id.employeeReportStatus_linearLayout_InvDoc);
        employeeReportStatus_linearLayout_uploadedURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedURL);
        employeeReportStatus_linearLayout_resolvingDoc = findViewById(R.id.employeeReportStatus_linearLayout_resolvingDoc);
        employeeReportStatus_linearLayout_uploadedResolvingDocURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedResolvingDocURL);
        employeeReportStatus_linearLayout_btns = findViewById(R.id.employeeReportStatus_linearLayout_btns);
        employeeReportStatus_linearLayout_btnSubmit = findViewById(R.id.employeeReportStatus_linearLayout_btnSubmit);
        employeeReportStatus_linearLayout_btnApproveReject = findViewById(R.id.employeeReportStatus_linearLayout_btnApproveReject);

        employeeReportStatus_img_pollutionPhoto = findViewById(R.id.employeeReportStatus_img_pollutionPhoto);

        employeeReportStatus_btn_upload = findViewById(R.id.employeeReportStatus_btn_upload);
        employeeReportStatus_btn_uploadResolvingDoc = findViewById(R.id.employeeReportStatus_btn_uploadResolvingDoc);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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