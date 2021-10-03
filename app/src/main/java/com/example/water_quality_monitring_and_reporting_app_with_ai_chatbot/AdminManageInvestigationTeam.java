package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AdminManageInvestigationTeam extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";
    private final String orgIDPreference = "orgID";

    private String getUserIDPreference = "";

    RecyclerView adminManageInvestigationTeam_recycleV_investigatorTeamList;

    String[] inTeamID;
    String[] inTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_investigation_team);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        Toolbar toolbar = findViewById(R.id.adminManageInvestigationTeam_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adminManageInvestigationTeam_recycleV_investigatorTeamList = findViewById(R.id.adminManageInvestigationTeam_recycleV_investigatorTeamList);

        displayRecyclerView();
    }

    protected void onRestart(){
        super.onRestart();

        displayRecyclerView();
    }

    private void displayRecyclerView() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        Cursor cursorOrg = dbHelper.getOrgInfoByUserID(getUserIDPreference);
        String orgID = cursorOrg.getString(cursorOrg.getColumnIndex("orgID"));

        cursor = dbHelper.getInvestigatorTeamInfoByOrgID(orgID);

        int i = 0, j = 0;

        int countMyReport = (! (cursor == null)) ? cursor.getCount() : 0;

        inTeamID = new String[countMyReport];
        inTeamName = new String[countMyReport];

        if (cursor!= null) {
            do {
                inTeamID[i] = cursor.getString(cursor.getColumnIndex("investigationTeamID"));
                inTeamName[i] = cursor.getString(cursor.getColumnIndex("investigationTeamName"));
                i++;
            } while (cursor.moveToNext());
        }

        AdminManageInvestigationTeamRecycleVAdapter adapter = new AdminManageInvestigationTeamRecycleVAdapter(this, inTeamID, inTeamName);
        adminManageInvestigationTeam_recycleV_investigatorTeamList.setAdapter(adapter);
        adminManageInvestigationTeam_recycleV_investigatorTeamList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}