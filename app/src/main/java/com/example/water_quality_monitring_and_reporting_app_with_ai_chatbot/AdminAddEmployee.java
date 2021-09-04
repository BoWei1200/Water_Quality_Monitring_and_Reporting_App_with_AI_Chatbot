package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class AdminAddEmployee extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";

    private TextInputEditText adminAddEmployee_txtInputET_fName, adminAddEmployee_txtInputET_lName, adminAddEmployee_txtInputET_email,
            adminAddEmployee_txtInputET_phone, adminAddEmployee_txtInputET_addressLine, adminAddEmployee_txtInputET_postcode,
            adminAddEmployee_txtInputET_city;

    private Spinner adminAddEmployee_spinner_state, adminAddEmployee_spinner_investigationTeamList;

    private RadioGroup adminAddEmployee_rGroup_employeeType;

    private LinearLayout adminAddEmployee_linearLayout_investigationTeam;

    private TextView adminAddEmployee_txt_errorName, adminAddEmployee_txt_errorEmail, adminAddEmployee_txt_errorPhone,
            adminAddEmployee_txt_errorAddress, adminAddEmployee_txt_errorUserType;

    private Boolean nameValid = false, emailValid = false, phoneValid = false, addressValid = false, userTypeValid = false;

    private static final Pattern phone_pattern = Pattern.compile("^(01)[0-46-9][0-9]{7,8}$");

    private String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_employee);

        Toolbar toolbar = findViewById(R.id.adminAddEmployee_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        adminAddEmployee_txtInputET_fName = findViewById(R.id.adminAddEmployee_txtInputET_fName);
        adminAddEmployee_txtInputET_lName  =findViewById(R.id.adminAddEmployee_txtInputET_lName);
        adminAddEmployee_txtInputET_email = findViewById(R.id.adminAddEmployee_txtInputET_email);
        adminAddEmployee_txtInputET_phone = findViewById(R.id.adminAddEmployee_txtInputET_phone);
        adminAddEmployee_txtInputET_addressLine = findViewById(R.id.adminAddEmployee_txtInputET_addressLine);
        adminAddEmployee_txtInputET_postcode = findViewById(R.id.adminAddEmployee_txtInputET_postcode);
        adminAddEmployee_txtInputET_city = findViewById(R.id.adminAddEmployee_txtInputET_city);

        adminAddEmployee_spinner_state = findViewById(R.id.adminAddEmployee_spinner_state);
        adminAddEmployee_spinner_investigationTeamList = findViewById(R.id.adminAddEmployee_spinner_investigationTeamList);

        adminAddEmployee_rGroup_employeeType = findViewById(R.id.adminAddEmployee_rGroup_employeeType);

        adminAddEmployee_linearLayout_investigationTeam = findViewById(R.id.adminAddEmployee_linearLayout_investigationTeam);

        adminAddEmployee_txt_errorName = findViewById(R.id.adminAddEmployee_txt_errorName);
        adminAddEmployee_txt_errorEmail = findViewById(R.id.adminAddEmployee_txt_errorEmail);
        adminAddEmployee_txt_errorPhone = findViewById(R.id.adminAddEmployee_txt_errorPhone);
        adminAddEmployee_txt_errorAddress = findViewById(R.id.adminAddEmployee_txt_errorAddress);
        adminAddEmployee_txt_errorUserType = findViewById(R.id.adminAddEmployee_txt_errorUserType);

        nameValidation(adminAddEmployee_txtInputET_fName);
        nameValidation(adminAddEmployee_txtInputET_lName);

        adminAddEmployee_txtInputET_email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s) ){
                    if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        adminAddEmployee_txt_errorEmail.setText("");
                        emailValid = true;
                    }else{
                        adminAddEmployee_txt_errorEmail.setText("Invalid Email");
                        emailValid = false;
                    }
                }else{
                    adminAddEmployee_txt_errorEmail.setText("Required");
                    emailValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        adminAddEmployee_txtInputET_phone.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!adminAddEmployee_txtInputET_phone.getText().toString().isEmpty()){
                    if (phone_pattern.matcher(adminAddEmployee_txtInputET_phone.getText().toString()).matches()) {
                        adminAddEmployee_txt_errorPhone.setText("");
                        phoneValid = true;
                    } else {
                        adminAddEmployee_txt_errorPhone.setText("Invalid Malaysia Phone No.");
                        phoneValid = false;
                    }
                }else{
                    adminAddEmployee_txt_errorPhone.setText("Required");
                    phoneValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addressValidation(adminAddEmployee_txtInputET_addressLine);
        addressValidation(adminAddEmployee_txtInputET_postcode);
        addressValidation(adminAddEmployee_txtInputET_city);

        if (adminAddEmployee_spinner_state != null) {
            adminAddEmployee_spinner_state.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.state_array, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (adminAddEmployee_spinner_state != null) {
                adminAddEmployee_spinner_state.setAdapter(adapter);
            }
        }

        if (adminAddEmployee_spinner_investigationTeamList != null) {
            adminAddEmployee_spinner_investigationTeamList.setOnItemSelectedListener(this);

            DatabaseHelper dbHelper = new DatabaseHelper(this);

            Cursor getOrgID = dbHelper.getOrgInfoByUserID(getUserIDPreference);
            getOrgID.moveToFirst();

            Cursor cursorGetInvestigationTeam = dbHelper.getInvestigationTeamByOrgID(getOrgID.getString(getOrgID.getColumnIndex("orgID")));

            String arrayInvestigatorTeam[] = new String[cursorGetInvestigationTeam.getCount()];

            int i = 0;
            for(cursorGetInvestigationTeam.moveToFirst(); !cursorGetInvestigationTeam.isAfterLast(); cursorGetInvestigationTeam.moveToNext()){
                arrayInvestigatorTeam[i++] = cursorGetInvestigationTeam.getString(cursorGetInvestigationTeam.getColumnIndex("investigationTeamName"));
            }

            ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayInvestigatorTeam);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (adminAddEmployee_spinner_investigationTeamList != null) {
                adminAddEmployee_spinner_investigationTeamList.setAdapter(adapter);
            }
        }
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(position).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void nameValidation(TextInputEditText txtInputET_nameAttribute){
        txtInputET_nameAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String fNameCheck = "", LNameCheck = "";
                    fNameCheck = adminAddEmployee_txtInputET_fName.getText().toString();
                    LNameCheck = adminAddEmployee_txtInputET_lName.getText().toString();

                    if(fNameCheck.isEmpty() || LNameCheck.isEmpty()){
                        adminAddEmployee_txt_errorName.setText("Required");
                        nameValid = false;
                    }
                    else{
                        adminAddEmployee_txt_errorName.setText("");
                        nameValid = true;
                    }
                }catch(Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void addressValidation(TextInputEditText txtInputET_addressAttribute){
        txtInputET_addressAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String addressLineCheck = "", postcodeCheck = "", cityCheck = "";

                    addressLineCheck = adminAddEmployee_txtInputET_addressLine.getText().toString();
                    postcodeCheck = adminAddEmployee_txtInputET_postcode.getText().toString();
                    cityCheck = adminAddEmployee_txtInputET_city.getText().toString();

                    if(addressLineCheck.isEmpty() || postcodeCheck.isEmpty() || cityCheck.isEmpty()){
                        String errorMsgAddress = "Require: "; int errorAmount = 0;

                        if(addressLineCheck.isEmpty()){
                            errorMsgAddress += "address line";
                            errorAmount++;
                        }

                        if(postcodeCheck.isEmpty()){
                            errorMsgAddress += (errorAmount > 0) ? ", postcode" : "postcode";
                            errorAmount++;
                        }

                        if(cityCheck.isEmpty()){
                            errorMsgAddress += (errorAmount > 0) ? ", city" : "city";
                            errorAmount++;
                        }

                        adminAddEmployee_txt_errorAddress.setText(errorMsgAddress);
                        addressValid = false;
                    }
                    else{
                        adminAddEmployee_txt_errorAddress.setText("");
                        addressValid = true;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void addEmployee(View view) {
        if(nameValid && emailValid && phoneValid && addressValid && userTypeValid){
            try{
                DatabaseHelper dbHelper = new DatabaseHelper(this);

                Boolean emailExist = dbHelper.isEmail_Exist(adminAddEmployee_txtInputET_email.getText().toString());
                Boolean phoneExist = dbHelper.isPhone_Exist(adminAddEmployee_txtInputET_phone.getText().toString());

                if(!(emailExist || phoneExist)){
                    String randomizedPassword = "Hy" + (int)(Math.random() * 99999);

                    if(dbHelper.addUser(
                            adminAddEmployee_txtInputET_email.getText().toString(),
                            adminAddEmployee_txtInputET_fName.getText().toString(),
                            adminAddEmployee_txtInputET_lName.getText().toString(),
                            adminAddEmployee_txtInputET_phone.getText().toString(),
                            userType,
                            randomizedPassword,
                            adminAddEmployee_txtInputET_addressLine.getText().toString(),
                            adminAddEmployee_txtInputET_postcode.getText().toString(),
                            adminAddEmployee_txtInputET_city.getText().toString(),
                            adminAddEmployee_spinner_state.getSelectedItem().toString()
                    )){
                        String orgID = "";
                        Cursor cursorOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);

                        orgID = (cursorOrgInfo.moveToFirst()) ? cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgID")) : "";

                        if(dbHelper.addEmployeeOrg(orgID, dbHelper.getUserID(adminAddEmployee_txtInputET_email.getText().toString()))){
                            displayToast("Employee added successfully!");

                            String name = adminAddEmployee_txtInputET_fName.getText().toString() + " " + adminAddEmployee_txtInputET_lName.getText().toString();
                            String message = name + ", your HydroMy password is " + randomizedPassword;

                            finish();

                            sendEmail(adminAddEmployee_txtInputET_email.getText().toString(), message);
                        }else{
                            displayToast("Problem occurred during assigning organization");
                        }
                    }else{
                        displayToast("Problem occurred during registration");
                    }

                }else{
                    if(emailExist){
                        adminAddEmployee_txt_errorEmail.setText("This email is alredy registered");
                        displayToast("This email has been registered");
                    }

                    if(phoneExist){
                        adminAddEmployee_txt_errorPhone.setText("This phone no. is already registered");
                        displayToast("This phone no. is already registered");
                    }
                }

            }catch(Exception e){
                System.out.println("\t" + e.toString());
            }
        }else {
            displayToast("Please ensure every credential is filled in correctly");
        }
    }

    public void selectUserType(View view) {
        switch (view.getId()){
            case R.id.adminAddEmployee_rBtn_AD:
                userType = "AD";
                break;

            case R.id.adminAddEmployee_rBtn_EX:
                userType = "EX";
                break;

            case R.id.adminAddEmployee_rBtn_IN:
                userType = "IN";
                break;

            case R.id.adminAddEmployee_rBtn_RH:
                userType = "RH";
                break;
        }

        userTypeValid = true;
        adminAddEmployee_txt_errorUserType.setVisibility(View.GONE);

        if(view.getId() == R.id.adminAddEmployee_rBtn_IN){
            DatabaseHelper dbHelper = new DatabaseHelper(this);

            Cursor getOrgID = dbHelper.getOrgInfoByUserID(getUserIDPreference);
            getOrgID.moveToFirst();

            Cursor cursorGetInvestigatorTeam = dbHelper.getInvestigationTeamByOrgID(getOrgID.getString(getOrgID.getColumnIndex("orgID")));

            if(cursorGetInvestigatorTeam.getCount() == 0){
                RadioButton rtnIN = findViewById(R.id.adminAddEmployee_rBtn_IN);
                rtnIN.setChecked(false);
                userTypeValid = false;
                adminAddEmployee_txt_errorUserType.setVisibility(View.VISIBLE);

                displayToast("Investigation Team should be created first");

                adminAddEmployee_linearLayout_investigationTeam.setVisibility(View.GONE);
            }else{
                adminAddEmployee_linearLayout_investigationTeam.setVisibility(View.VISIBLE);
            }

        }else{
            adminAddEmployee_linearLayout_investigationTeam.setVisibility(View.GONE);
        }
    }

    protected boolean sendEmail(String toUserEmail, String message) {
        String[] TO = {toUserEmail};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HydroMy Message");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void displayToast(String message){
        Toast.makeText(AdminAddEmployee.this,message,Toast.LENGTH_SHORT).show();
    }
}