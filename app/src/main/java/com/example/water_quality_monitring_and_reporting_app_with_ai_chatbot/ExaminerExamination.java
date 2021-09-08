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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class ExaminerExamination extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserIDPreference = "";
    private String getUserTypePreference = "";

    private TextView examinerExamination_txt_tabPending, examinerExamination_txt_tabCompleted;

    private TextView currentlyActiveTab;

    private EditText examinerExamination_eTxt_searchBar;

    private Spinner examinerExamination_spinner_filter;

    private RecyclerView examinerExamination_recycleV_reportList;

    private String reportIDs[];
    private String reportDates[];
    private String reportTimes[];
    private String reportStatus[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examiner_examination);

        Toolbar toolbar = findViewById(R.id.examinerExamination_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        examinerExamination_txt_tabPending = findViewById(R.id.examinerExamination_txt_tabPending);
        examinerExamination_txt_tabCompleted = findViewById(R.id.examinerExamination_txt_tabCompleted);

        examinerExamination_eTxt_searchBar = findViewById(R.id.examinerExamination_eTxt_searchBar);

        examinerExamination_spinner_filter = findViewById(R.id.examinerExamination_spinner_filter);

        examinerExamination_recycleV_reportList = findViewById(R.id.examinerExamination_recycleV_reportList);

        currentlyActiveTab = examinerExamination_txt_tabPending;

        examinerExamination_eTxt_searchBar.addTextChangedListener(new TextWatcher() {
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

        if (examinerExamination_spinner_filter != null) {
            examinerExamination_spinner_filter.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.filter_examination, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (examinerExamination_spinner_filter != null) {
                examinerExamination_spinner_filter.setAdapter(adapter);
            }
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursorGetReport = dbHelper.getReportByExaminerID(
                getUserIDPreference, currentlyActiveTab.getText().toString(),
                examinerExamination_spinner_filter.getSelectedItem().toString(),
                examinerExamination_eTxt_searchBar.getText().toString());

        int countMyReport = !(cursorGetReport==null) ? cursorGetReport.getCount() : 0;

        if(countMyReport != 0){
            reportIDs = new String[countMyReport];
            reportDates = new String[countMyReport];
            reportTimes = new String[countMyReport];
            reportStatus = new String[countMyReport];

            loadMyReportFromDatabase();

            EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
            examinerExamination_recycleV_reportList.setAdapter(adapter);
            examinerExamination_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        examinerExamination_eTxt_searchBar.setText("");
        redesignLayout();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toWhichTab(View view) {
        setCurrentlyActiveTab(view.getId());

        examinerExamination_eTxt_searchBar.setText("");
        redesignLayout();

    }

    public void redesignLayout(){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getReportByExaminerID(getUserIDPreference,
                currentlyActiveTab.getText().toString(),
                examinerExamination_spinner_filter.getSelectedItem().toString(),
                examinerExamination_eTxt_searchBar.getText().toString());

        int size = (cursor != null) ? cursor.getCount() : 0;

        reportIDs = new String[size];
        reportDates = new String[size];
        reportTimes = new String[size];
        reportStatus = new String[size];

        if(cursor != null)
            returnRead(cursor);

        EmployeeReportRecycleVAdapter adapter = new EmployeeReportRecycleVAdapter(this, reportIDs, reportDates, reportTimes, reportStatus, getUserTypePreference);
        examinerExamination_recycleV_reportList.setAdapter(adapter);
        examinerExamination_recycleV_reportList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setCurrentlyActiveTab(int txtID){
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.gray));
        currentlyActiveTab.setBackgroundColor(Color.WHITE);

        currentlyActiveTab = findViewById(txtID);
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.tab_text_color));
        currentlyActiveTab.setBackground(getResources().getDrawable(R.color.tab_background));

        //change the recycler view
    }

    private void loadMyReportFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getReportByExaminerID(
                getUserIDPreference, currentlyActiveTab.getText().toString(),
                examinerExamination_spinner_filter.getSelectedItem().toString(),
                examinerExamination_eTxt_searchBar.getText().toString());

        returnRead(cursor);
    }

    private Cursor returnRead(Cursor cursor) {
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
        return cursor;
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}