package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ReportHandlerReportFromUser extends AppCompatActivity {

    private TextView reportHandlerReportFromUser_txt_tabPending, reportHandlerReportFromUser_txt_tabCompleted;

    private TextView currentlyActiveTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_handler_report_from_user);

        Toolbar toolbar = findViewById(R.id.reportHandlerReportFromUser_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        reportHandlerReportFromUser_txt_tabPending = findViewById(R.id.reportHandlerReportFromUser_txt_tabPending);
        reportHandlerReportFromUser_txt_tabCompleted = findViewById(R.id.examinerExamination_txt_tabCompleted);

        currentlyActiveTab = reportHandlerReportFromUser_txt_tabPending;
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toWhichTab(View view) {
        setCurrentlyActiveTab(view.getId());
    }

    public void setCurrentlyActiveTab(int txtID){
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.gray));
        currentlyActiveTab.setBackgroundColor(Color.WHITE);

        currentlyActiveTab = findViewById(txtID);
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.tab_text_color));
        currentlyActiveTab.setBackground(getResources().getDrawable(R.color.tab_background));

        //change the recycler view
    }
}