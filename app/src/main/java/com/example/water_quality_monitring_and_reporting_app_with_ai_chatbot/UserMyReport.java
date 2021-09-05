package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

public class UserMyReport extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";

    private RecyclerView userMyReport_recycleV_reportList;

    private String myReportIDs[];
    private String myReportDates[];
    private String myReportTimes[];
    private String myReportStatus[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_my_report);

        Toolbar toolbar = findViewById(R.id.userMyReport_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        userMyReport_recycleV_reportList = findViewById(R.id.userMyReport_recycleV_reportList);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int countMyReport = dbHelper.getMyReport(getUserIDPreference).getCount();

        myReportIDs = new String[countMyReport];
        myReportDates = new String[countMyReport];
        myReportTimes = new String[countMyReport];
        myReportStatus = new String[countMyReport];

        loadMyReportFromDatabase();

        UserMyReportRecycleVAdapter adapter = new UserMyReportRecycleVAdapter(this, myReportIDs, myReportDates, myReportTimes, myReportStatus);
        userMyReport_recycleV_reportList.setAdapter(adapter);
        userMyReport_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
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
        Cursor cursor = dbHelper.getMyReport(getUserIDPreference);

        returnRead(cursor);
    }

    private Cursor returnRead(Cursor cursor) {
        int i = 0, j = 0;
        if (cursor.moveToFirst()) {
            do {
                myReportIDs[i] = cursor.getString(cursor.getColumnIndex("reportID"));
                myReportDates[i] = cursor.getString(cursor.getColumnIndex("reportDate"));
                myReportTimes[i] = cursor.getString(cursor.getColumnIndex("reportTime"));
                myReportStatus[i] = cursor.getString(cursor.getColumnIndex("reportStatus"));

                i++;
            } while (cursor.moveToNext());
        }
        return cursor;
    }
}