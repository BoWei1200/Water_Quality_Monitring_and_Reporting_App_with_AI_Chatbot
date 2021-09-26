package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminReportListToPost extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";
    private final String orgIDPreference = "orgID";

    private String getUserIDPreference = "";
    private String getUserTypePreference = "";
    private String getOrgIDPreference = "";

    private RecyclerView adminReportList_recycleV_reportList;

    private String reportIDs[];
    private String reportDates[];
    private String reportTimes[];
    private String reportStatus[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_list_to_post);

        Toolbar toolbar = findViewById(R.id.adminReportList_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);
        getOrgIDPreference = mPreferences.getString(orgIDPreference, null);

        adminReportList_recycleV_reportList = findViewById(R.id.adminReportList_recycleV_reportList);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursorGetReport = dbHelper.getPostableReportByOrgID(getOrgIDPreference);

        int countMyReport = !(cursorGetReport==null) ? cursorGetReport.getCount() : 0;

        if(countMyReport != 0){
            reportIDs = new String[countMyReport];
            reportDates = new String[countMyReport];
            reportTimes = new String[countMyReport];
            reportStatus = new String[countMyReport];

            loadMyReportFromDatabase();

            EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
            adminReportList_recycleV_reportList.setAdapter(adapter);
            adminReportList_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMyReportFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getPostableReportByOrgID(getOrgIDPreference);

        int i = 0, j = 0;
        if (cursor.moveToFirst()) {
            do {
                reportIDs[i] = cursor.getString(cursor.getColumnIndex("reportID"));
                reportDates[i] = cursor.getString(cursor.getColumnIndex("reportDate"));
                reportTimes[i] = cursor.getString(cursor.getColumnIndex("reportTime"));
                reportStatus[i] = cursor.getString(cursor.getColumnIndex("reportStatus"));

                i++;
            } while (cursor.moveToNext());
        }
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}