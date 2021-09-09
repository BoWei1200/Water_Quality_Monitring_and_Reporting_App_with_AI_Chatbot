package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

public class InvestigatorInvestigation extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserIDPreference = "";
    private String getUserTypePreference = "";

    String passedActivity;
    String teamID;

    private EditText investigatorInvestigation_eTxt_searchBar;

    private RecyclerView investigatorInvestigation_recycleV_reportList;

    private String reportIDs[];
    private String reportDates[];
    private String reportTimes[];
    private String reportStatus[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator_investigation);

        Toolbar toolbar = findViewById(R.id.investigatorInvestigation_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        Intent intent = getIntent();
        passedActivity = intent.getStringExtra("passedActivity");
        teamID = intent.getStringExtra("teamID");

        String toolbarTitle = (passedActivity.equals("Pending")) ? "Pending Investigation" : "My Investigation";
        getSupportActionBar().setTitle(toolbarTitle);

        investigatorInvestigation_eTxt_searchBar = findViewById(R.id.investigatorInvestigation_eTxt_searchBar);
        investigatorInvestigation_recycleV_reportList = findViewById(R.id.investigatorInvestigation_recycleV_reportList);

        investigatorInvestigation_eTxt_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                redesignLayout();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        redesignLayout();

//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//        Cursor cursorGetReport = dbHelper.getReportByInvestigationTeam(
//                teamID, passedActivity,
//                investigatorInvestigation_eTxt_searchBar.getText().toString());
//
//        int countMyReport = !(cursorGetReport==null) ? cursorGetReport.getCount() : 0;
//
//        if(countMyReport != 0){
//            reportIDs = new String[countMyReport];
//            reportDates = new String[countMyReport];
//            reportTimes = new String[countMyReport];
//            reportStatus = new String[countMyReport];
//
//
//
//            EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
//            investigatorInvestigation_recycleV_reportList.setAdapter(adapter);
//            investigatorInvestigation_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
//        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        redesignLayout();
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void redesignLayout(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getReportByInvestigationTeam(
                teamID, passedActivity,
                investigatorInvestigation_eTxt_searchBar.getText().toString());

        int size = (cursor != null) ? cursor.getCount() : 0;

        reportIDs = new String[size];
        reportDates = new String[size];
        reportTimes = new String[size];
        reportStatus = new String[size];

        if(cursor != null)
            returnRead(cursor);

        EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
        investigatorInvestigation_recycleV_reportList.setAdapter(adapter);
        investigatorInvestigation_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
    }

    private Cursor returnRead(Cursor cursor) {
        int i = 0;

        if (cursor.moveToFirst()) {
            do {
                reportIDs[i] = cursor.getString(cursor.getColumnIndex("reportID"));
                reportDates[i] = cursor.getString(cursor.getColumnIndex("reportDate"));
                reportTimes[i] = cursor.getString(cursor.getColumnIndex("reportTime"));
                reportStatus[i] = cursor.getString(cursor.getColumnIndex("reportStatus"));

                i++;
            } while (cursor.moveToNext());
        }
        return cursor;
    }


}