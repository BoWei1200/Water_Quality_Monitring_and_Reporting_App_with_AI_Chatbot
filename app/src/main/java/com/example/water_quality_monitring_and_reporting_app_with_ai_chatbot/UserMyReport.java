package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class UserMyReport extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";

    private ConstraintLayout userMyReport_constraintLayout_longPressOperation;
    private RecyclerView userMyReport_recycleV_reportList;

    private String myReportIDs[];
    private String myReportDates[];
    private String myReportTimes[];
    private String myReportStatus[];

    ArrayList<String> reportIDSelected = new ArrayList<String>();

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

        userMyReport_constraintLayout_longPressOperation = findViewById(R.id.userMyReport_constraintLayout_longPressOperation);
        userMyReport_recycleV_reportList = findViewById(R.id.userMyReport_recycleV_reportList);

        displayRecyclerView();
    }

    protected void onRestart(){
        super.onRestart();

        displayRecyclerView();
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(reportIDSelected.size() == 0){
            super.onBackPressed();
        }
        else{
            reportIDSelected.clear();
            displayRecyclerView();
            userMyReport_constraintLayout_longPressOperation.setVisibility(View.GONE);
        }
    }

    private void displayRecyclerView(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorGetMyReport = dbHelper.getMyReport(getUserIDPreference);
        int countMyReport = (! (cursorGetMyReport == null)) ? cursorGetMyReport.getCount() : 0;

        myReportIDs = new String[countMyReport];
        myReportDates = new String[countMyReport];
        myReportTimes = new String[countMyReport];
        myReportStatus = new String[countMyReport];

        loadMyReportFromDatabase();

        UserMyReportRecycleVAdapter adapter = new UserMyReportRecycleVAdapter(this, myReportIDs, myReportDates, myReportTimes, myReportStatus, reportIDSelected);
        userMyReport_recycleV_reportList.setAdapter(adapter);
        userMyReport_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadMyReportFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getMyReport(getUserIDPreference);

        int i = 0, j = 0;
        if (cursor != null) {
            do {
                myReportIDs[i] = cursor.getString(cursor.getColumnIndex("reportID"));
                myReportDates[i] = cursor.getString(cursor.getColumnIndex("reportDate"));
                myReportTimes[i] = cursor.getString(cursor.getColumnIndex("reportTime"));
                myReportStatus[i] = cursor.getString(cursor.getColumnIndex("reportStatus"));

                i++;
            } while (cursor.moveToNext());
        }
    }

    public void deleteReport(View view) {
        for (String i:reportIDSelected) {
            System.out.println("ID selected " + i +", ");
        }

        displayAlert(R.string.delete_selected_report, R.string.empty_string, R.drawable.warningiconedit);
    }

    public void displayAlert(int title, int msg, int drawable){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(title){
                            case R.string.delete_selected_report:
                                DatabaseHelper dbHelper = new DatabaseHelper(UserMyReport.this);
                                if(dbHelper.deleteReport(reportIDSelected))
                                    displayToast("Selected report(s) deleted");

                                reportIDSelected.clear();
                                userMyReport_constraintLayout_longPressOperation.setVisibility(View.GONE);
                                displayRecyclerView();
                                break;
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(drawable)
                .show();
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}