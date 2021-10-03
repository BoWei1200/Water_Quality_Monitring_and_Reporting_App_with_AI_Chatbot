package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class InvestigatorTeamDetail extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserTypePreference = "";

    private TextView investigatorTeamDetail_txt_teamID, investigatorTeamDetail_txt_teamName;

    private RecyclerView investigatorTeamDetail_recycleV_teamMemberList;

    private String teamID;
    private String teamMemberNames[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investigator_team_detail);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        Toolbar toolbar = findViewById(R.id.investigatorTeamDetail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle((getUserTypePreference.equals("AD")) ? "Investigator Team Detail" : "My Investigator Team");

        investigatorTeamDetail_txt_teamID = findViewById(R.id.investigatorTeamDetail_txt_teamID);
        investigatorTeamDetail_txt_teamName = findViewById(R.id.investigatorTeamDetail_txt_teamName);

        Intent intent = getIntent();
        teamID = intent.getStringExtra("teamID");
        String teamName = intent.getStringExtra("teamName");

        investigatorTeamDetail_txt_teamID.setText(teamID);
        investigatorTeamDetail_txt_teamName.setText(teamName);

        investigatorTeamDetail_recycleV_teamMemberList = findViewById(R.id.investigatorTeamDetail_recycleV_teamMemberList);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int countTeamMember = dbHelper.getTeamMemberNum(teamID);
        teamMemberNames = new String[countTeamMember];
        loadTeamMemberFromDatabase();

        InvestigatorTeamDetailMemberRecycleVAdapter adapter = new InvestigatorTeamDetailMemberRecycleVAdapter(this, teamMemberNames);
        investigatorTeamDetail_recycleV_teamMemberList.setAdapter(adapter);
        investigatorTeamDetail_recycleV_teamMemberList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadTeamMemberFromDatabase() {
        //load all user in the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getTeamMember(teamID);
        //load data from database
        //then store into the User Class
        returnRead(cursor);
    }

    private Cursor returnRead(Cursor cursor) {
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                teamMemberNames[i++] = cursor.getString(cursor.getColumnIndex("fName")) + " " + cursor.getString(cursor.getColumnIndex("lName"));
            } while (cursor.moveToNext());
        }
        return cursor;
    }
}