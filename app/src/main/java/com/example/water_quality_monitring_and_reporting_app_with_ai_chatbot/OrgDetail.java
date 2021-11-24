package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class OrgDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userTypePreference = "userType";
    String getUserTypePreference = "";
    String orgID = "";

    private ConstraintLayout orgDetail_coordinateLayout;
    private TextView orgDetail_txt_orgID;
    private EditText orgDetail_eTxt_orgName;
    private TextInputEditText orgDetail_txtInputET_addressLine, orgDetail_txtInputET_postcode, orgDetail_txtInputET_city;
    private Spinner orgDetail_spinner_state;
    private Switch orgDetail_switch_ready;
    private ImageView orgDetail_img_deleteOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_detail);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        Toolbar toolbar = findViewById(R.id.orgDetail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle((getUserTypePreference.equals("SAD")) ? "Organization Details" : "My Organization");

        orgDetail_coordinateLayout = findViewById(R.id.orgDetail_coordinateLayout);
        orgDetail_txt_orgID = findViewById(R.id.orgDetail_txt_orgID);
        orgDetail_eTxt_orgName = findViewById(R.id.orgDetail_eTxt_orgName);

        orgDetail_txtInputET_addressLine = findViewById(R.id.orgDetail_txtInputET_addressLine);
        orgDetail_txtInputET_postcode = findViewById(R.id.orgDetail_txtInputET_postcode);
        orgDetail_txtInputET_city = findViewById(R.id.orgDetail_txtInputET_city);

        orgDetail_spinner_state = findViewById(R.id.orgDetail_spinner_state);

        orgDetail_switch_ready = findViewById(R.id.orgDetail_switch_ready);

        orgDetail_img_deleteOrg = findViewById(R.id.orgDetail_img_deleteOrg);

        Intent intent = getIntent();
        orgID = intent.getStringExtra("orgID");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorOrgInfo = dbHelper.getOrgInfoByOrgID(orgID);

        orgDetail_txt_orgID.setText(orgID);
        cursorOrgInfo.moveToFirst();
        if(cursorOrgInfo != null){
            orgDetail_eTxt_orgName.setText(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgName")));

            orgDetail_txtInputET_addressLine.setText(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgAddressLine")));
            orgDetail_txtInputET_postcode.setText(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgPostCode")));
            orgDetail_txtInputET_city.setText(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgCity")));

            orgDetail_img_deleteOrg.setVisibility((getUserTypePreference.equals("SAD")) ? View.VISIBLE : View.GONE);

                if (orgDetail_spinner_state != null) {
                orgDetail_spinner_state.setOnItemSelectedListener(this);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.state_array, android.R.layout.simple_spinner_item);

                // Specify the layout to use when the list of choices appears.
                adapter.setDropDownViewResource
                        (android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner.
                if (orgDetail_spinner_state != null) {
                    orgDetail_spinner_state.setAdapter(adapter);
                    orgDetail_spinner_state.setSelection(adapter.getPosition(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgState"))));
                }
            }

            orgDetail_switch_ready.setChecked(cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgReady")).equals("1"));
        }

        orgDetail_switch_ready.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //check whether they have three kinds of users
                    int countEX = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "EX").getCount();
                    int countIN = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "IN").getCount();
                    int countRH = dbHelper.getOrgEmployeeByOrgIDAndUserType(orgID, "RH").getCount();

                    if(countEX==0 || countIN==0 || countRH==0){
                        Snackbar.make(findViewById(R.id.orgDetail_coordinateLayout), "Your organization does not have examiner / investigator / report handler to handle reports",
                                5000)
                                .show();
                        orgDetail_switch_ready.setChecked(false);
                        return;
                    }
                }
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

    public void displayToast(String message){
        Toast.makeText(OrgDetail.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(position).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void update(View view) {
        String orgReady = (orgDetail_switch_ready.isChecked()) ? "1" : "0";

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if(dbHelper.updateOrgInfo(orgID,
                orgDetail_eTxt_orgName.getText().toString(),
                orgDetail_txtInputET_addressLine.getText().toString(),
                orgDetail_txtInputET_postcode.getText().toString(),
                orgDetail_txtInputET_city.getText().toString(),
                orgDetail_spinner_state.getSelectedItem().toString(),
                orgReady)){
            displayToast("Organization information updated");
        }else{
            displayToast("Problem in updating");
        }

        finish();
    }

    public void deleteOrg(View view) {
        displayAlert(R.string.delete_org_title, R.string.empty_string, R.drawable.warningiconedit);
    }

    public void displayAlert(int title, int msg, int drawable){

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(OrgDetail.this);
                        switch(title){
                            case R.string.delete_org_title:

                                Cursor cursorGetReportByOrg = dbHelper.getAllProcessingReportByOrgID(orgID);

                                if(cursorGetReportByOrg != null){
                                    for(cursorGetReportByOrg.moveToFirst(); !cursorGetReportByOrg.isAfterLast(); cursorGetReportByOrg.moveToNext()){
                                        dbHelper.updateReportStatusByReportID(cursorGetReportByOrg.getString(cursorGetReportByOrg.getColumnIndex("reportID")), "Rejected");
                                    }
                                }

                                Cursor cursorGetEmployeeByOrg = dbHelper.getEmployeesByOrgID(orgID, "", "All");
                                if(cursorGetEmployeeByOrg != null){
                                    for(cursorGetEmployeeByOrg.moveToFirst(); !cursorGetEmployeeByOrg.isAfterLast(); cursorGetEmployeeByOrg.moveToNext()){
                                        if(cursorGetEmployeeByOrg.getString(cursorGetEmployeeByOrg.getColumnIndex("userType")).equals("IN"))
                                            dbHelper.deleteInvestigator(cursorGetEmployeeByOrg.getString(cursorGetEmployeeByOrg.getColumnIndex("userID")));

                                        dbHelper.deleteEmployee(cursorGetEmployeeByOrg.getString(cursorGetEmployeeByOrg.getColumnIndex("userID")));
                                    }
                                }

                                Cursor cursorGetINTeam = dbHelper.getInvestigationTeamByOrgID(orgID);
                                if(cursorGetINTeam.moveToFirst()){
                                    for(; !cursorGetINTeam.isAfterLast(); cursorGetINTeam.moveToNext()){
                                        dbHelper.deleteINTeam(cursorGetINTeam.getString(cursorGetINTeam.getColumnIndex("investigationTeamID")));
                                    }
                                }

                                if(dbHelper.deleteOrg(orgID)){
                                    displayToast("Organization deleted");
                                }

                                finish();

                                break;
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(drawable)
                .show();
    }
}