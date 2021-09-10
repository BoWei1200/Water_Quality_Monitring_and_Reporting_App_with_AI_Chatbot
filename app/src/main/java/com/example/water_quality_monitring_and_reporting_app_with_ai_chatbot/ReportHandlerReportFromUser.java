package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ReportHandlerReportFromUser extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserIDPreference = "";
    private String getUserTypePreference = "";

    private TextView reportHandlerReportFromUser_txt_tabPending, reportHandlerReportFromUser_txt_tabCompleted;

    private TextView currentlyActiveTab;

    private EditText reportHandlerReportFromUser_eTxt_searchBar;

    private RecyclerView reportHandlerReportFromUser_recycleV_reportList;

    private String reportIDs[];
    private String reportDates[];
    private String reportTimes[];
    private String reportStatus[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_handler_report_from_user);

        Toolbar toolbar = findViewById(R.id.reportHandlerReportFromUser_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        reportHandlerReportFromUser_txt_tabPending = findViewById(R.id.reportHandlerReportFromUser_txt_tabPending);
        reportHandlerReportFromUser_txt_tabCompleted = findViewById(R.id.examinerExamination_txt_tabCompleted);

        currentlyActiveTab = reportHandlerReportFromUser_txt_tabPending;

        reportHandlerReportFromUser_eTxt_searchBar = findViewById(R.id.reportHandlerReportFromUser_eTxt_searchBar);

        reportHandlerReportFromUser_recycleV_reportList = findViewById(R.id.reportHandlerReportFromUser_recycleV_reportList);

        reportHandlerReportFromUser_eTxt_searchBar.addTextChangedListener(new TextWatcher() {
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

    }

    @Override
    public void onRestart(){
        super.onRestart();
        redesignLayout();
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
        redesignLayout();
        //change the recycler view
    }

    public void redesignLayout(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getReportByReportHandler(
                getUserIDPreference, currentlyActiveTab.getText().toString(),
                reportHandlerReportFromUser_eTxt_searchBar.getText().toString());

        int size = (cursor != null) ? cursor.getCount() : 0;

        reportIDs = new String[size];
        reportDates = new String[size];
        reportTimes = new String[size];
        reportStatus = new String[size];

        if(cursor != null)
            returnRead(cursor);

        EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
        reportHandlerReportFromUser_recycleV_reportList.setAdapter(adapter);
        reportHandlerReportFromUser_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
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