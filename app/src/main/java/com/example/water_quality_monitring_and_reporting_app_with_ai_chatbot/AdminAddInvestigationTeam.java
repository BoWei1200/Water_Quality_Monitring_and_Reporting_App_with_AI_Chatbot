package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AdminAddInvestigationTeam extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private TextInputEditText adminAddInvestivationTeam_txtInputET_teamName;

    private TextView adminAddInvestivationTeam_txt_errorTeamName;

    private Boolean teamNameValid = false;

    private String getUserIDPreference = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_investigation_team);

        Toolbar toolbar = findViewById(R.id.adminAddInvestivationTeam_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        adminAddInvestivationTeam_txtInputET_teamName = findViewById(R.id.adminAddInvestivationTeam_txtInputET_teamName);
        adminAddInvestivationTeam_txt_errorTeamName = findViewById(R.id.adminAddInvestivationTeam_txt_errorTeamName);

        adminAddInvestivationTeam_txtInputET_teamName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!adminAddInvestivationTeam_txtInputET_teamName.getText().toString().isEmpty()){
                    adminAddInvestivationTeam_txt_errorTeamName.setVisibility(View.GONE);
                    teamNameValid = true;
                }else{
                    adminAddInvestivationTeam_txt_errorTeamName.setText("Required");
                    adminAddInvestivationTeam_txt_errorTeamName.setVisibility(View.VISIBLE);
                    teamNameValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addTeam(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        if(teamNameValid){
            if(!dbHelper.isInvestigationTemNameExist(adminAddInvestivationTeam_txtInputET_teamName.getText().toString())){

                String orgID = "";
                Cursor cursorOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);

                orgID = (cursorOrgInfo.moveToFirst()) ? cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgID")) : "";

                if(dbHelper.addInvestigationTeam(adminAddInvestivationTeam_txtInputET_teamName.getText().toString(), orgID)){
                    displayToast("Investigation team created successfully");
                    finish();
                }else{
                    displayToast("Problem occurred in adding team, please try again later");
                }
            }else{
                adminAddInvestivationTeam_txt_errorTeamName.setText("Team name already exists");
                adminAddInvestivationTeam_txt_errorTeamName.setVisibility(View.VISIBLE);
                displayToast("Team name already exists");
            }
        }else{
            displayToast("Please enter team name");
        }

    }

    public void displayToast(String message){
        Toast.makeText(AdminAddInvestigationTeam.this,message,Toast.LENGTH_SHORT).show();
    }
}