package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class OrgDetail extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userTypePreference = "userType";
    String getUserTypePreference = "";

    private TextView orgDetail_txt_orgID;
    private EditText orgDetail_eTxt_orgName, orgDetail_eTxt_orgAddress;
    private Switch orgDetail_switch_ready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_detail);

        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        Toolbar toolbar = findViewById(R.id.orgDetail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle((getUserTypePreference.equals("SAD")) ? "Organization Details" : "My Organization");

        orgDetail_txt_orgID = findViewById(R.id.orgDetail_txt_orgID);
        orgDetail_eTxt_orgName = findViewById(R.id.orgDetail_eTxt_orgName);
        orgDetail_eTxt_orgAddress = findViewById(R.id.orgDetail_eTxt_orgAddress);

        orgDetail_switch_ready = findViewById(R.id.orgDetail_switch_ready);

        Intent intent = getIntent();
        String orgID = intent.getStringExtra("orgID");


        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorOrgInfo = dbHelper.getOrgInfoByOrgID(orgID);

        orgDetail_txt_orgID.setText(orgID);

        if(cursorOrgInfo != null){
            orgDetail_eTxt_orgName.setText(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgName")));

            String orgAddress = cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgAddressLine"))
                            + ", " + cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgPostCode"))
                            + ", " + cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgCity"))
                            + ", " + cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgState"));

            orgDetail_eTxt_orgAddress.setText(orgAddress);

            orgDetail_switch_ready.setChecked(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgReady")).equals("1"));
        }

    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}