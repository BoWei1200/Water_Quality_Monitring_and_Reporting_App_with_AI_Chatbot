package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class UserOrEmployeeDetail extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserTypePreference = "";
    private String getUserIDPreference = "";

    ImageView userEmployeeDetail_img_deleteUser;

    TextView userEmployeeDetail_txt_userID;
    TextInputEditText userEmployeeDetail_txtInputET_fName, userEmployeeDetail_txtInputET_lName, userEmployeeDetail_txtInputET_email,
            userEmployeeDetail_txtInputET_phone, userEmployeeDetail_txtInputET_addressLine, userEmployeeDetail_txtInputET_postcode,
            userEmployeeDetail_txtInputET_city;

    TextView userEmployeeDetail_txt_errorName, userEmployeeDetail_txt_errorEmail, userEmployeeDetail_txt_errorPhone,
            userEmployeeDetail_txt_errorAddress, userEmployeeDetail_txt_errorUserType;

    Spinner userEmployeeDetail_spinner_state, userEmployeeDetail_spinner_investigationTeamList;

    LinearLayout userEmployeeDetail_linearLayout_userType;
    RadioGroup userEmployeeDetail_rGroup_employeeType;

    LinearLayout userEmployeeDetail_linearLayout_investigationTeam;

    Button userEmployeeDetail_btn_update;

    String userID = "", selectedUserType = "";

    Cursor cursorUserInfo;

    Boolean nameValid = false, emailValid = false, phoneValid = false, addressValid = false;

    private static final Pattern phone_pattern = Pattern.compile("^(01)[0-46-9][0-9]{7,8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_or_employee_detail);

        Toolbar toolbar = findViewById(R.id.userEmployeeDetail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        getSupportActionBar().setTitle((getUserTypePreference.equals("SAD")) ? "User Information" :
                                        (getUserTypePreference.equals("AD")) ? "Employee Information" : "Personal Information");

        userEmployeeDetail_img_deleteUser = findViewById(R.id.userEmployeeDetail_img_deleteUser);

        userEmployeeDetail_txt_userID = findViewById(R.id.userEmployeeDetail_txt_userID);
        userEmployeeDetail_txtInputET_fName = findViewById(R.id.userEmployeeDetail_txtInputET_fName);
        userEmployeeDetail_txtInputET_lName = findViewById(R.id.userEmployeeDetail_txtInputET_lName);
        userEmployeeDetail_txtInputET_email = findViewById(R.id.userEmployeeDetail_txtInputET_email);
        userEmployeeDetail_txtInputET_phone = findViewById(R.id.userEmployeeDetail_txtInputET_phone);
        userEmployeeDetail_txtInputET_addressLine = findViewById(R.id.userEmployeeDetail_txtInputET_addressLine);
        userEmployeeDetail_txtInputET_postcode = findViewById(R.id.userEmployeeDetail_txtInputET_postcode);
        userEmployeeDetail_txtInputET_city = findViewById(R.id.userEmployeeDetail_txtInputET_city);

        userEmployeeDetail_spinner_state = findViewById(R.id.userEmployeeDetail_spinner_state);

        userEmployeeDetail_linearLayout_userType = findViewById(R.id.userEmployeeDetail_linearLayout_userType);
        userEmployeeDetail_rGroup_employeeType = findViewById(R.id.userEmployeeDetail_rGroup_employeeType);
        userEmployeeDetail_linearLayout_investigationTeam = findViewById(R.id.userEmployeeDetail_linearLayout_investigationTeam);
        userEmployeeDetail_spinner_investigationTeamList = findViewById(R.id.userEmployeeDetail_spinner_investigationTeamList);

        userEmployeeDetail_txt_errorName = findViewById(R.id.userEmployeeDetail_txt_errorName);
        userEmployeeDetail_txt_errorEmail = findViewById(R.id.userEmployeeDetail_txt_errorEmail);
        userEmployeeDetail_txt_errorPhone = findViewById(R.id.userEmployeeDetail_txt_errorPhone);
        userEmployeeDetail_txt_errorAddress = findViewById(R.id.userEmployeeDetail_txt_errorAddress);

        userEmployeeDetail_btn_update = findViewById(R.id.userEmployeeDetail_btn_update);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        cursorUserInfo = null;

        if(getUserTypePreference.equals("SAD"))
            cursorUserInfo = dbHelper.getAllUser(userID, "All");
        else if (getUserTypePreference.equals("AD") || getUserTypePreference.equals("NA")){
            if(getUserTypePreference.equals("AD")){
                Cursor cursorOrgID = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                String orgID = cursorOrgID.getString(cursorOrgID.getColumnIndex("orgID"));
                cursorUserInfo = dbHelper.getEmployeesByOrgID(orgID, userID);
            }else{
                cursorUserInfo = dbHelper.getAllUser(getUserIDPreference, "All");
                userID = getUserIDPreference;
                userEmployeeDetail_img_deleteUser.setVisibility(View.GONE);
            }


            nameValidation(userEmployeeDetail_txtInputET_fName);
            nameValidation(userEmployeeDetail_txtInputET_lName);

            userEmployeeDetail_txtInputET_email.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!TextUtils.isEmpty(s) ){
                        if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                            userEmployeeDetail_txt_errorEmail.setVisibility(View.GONE);
                            emailValid = true;
                        }else{
                            userEmployeeDetail_txt_errorEmail.setText("Invalid Email");
                            userEmployeeDetail_txt_errorEmail.setVisibility(View.VISIBLE);
                            emailValid = false;
                        }
                    }else{
                        userEmployeeDetail_txt_errorEmail.setText("Required");
                        userEmployeeDetail_txt_errorEmail.setVisibility(View.VISIBLE);
                        emailValid = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            userEmployeeDetail_txtInputET_phone.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!userEmployeeDetail_txtInputET_phone.getText().toString().isEmpty()){
                        if (phone_pattern.matcher(userEmployeeDetail_txtInputET_phone.getText().toString()).matches()) {
                            userEmployeeDetail_txt_errorPhone.setVisibility(View.GONE);
                            phoneValid = true;
                        } else {
                            userEmployeeDetail_txt_errorPhone.setText("Invalid Malaysia Phone No.");
                            userEmployeeDetail_txt_errorPhone.setVisibility(View.VISIBLE);
                            phoneValid = false;
                        }
                    }else{
                        userEmployeeDetail_txt_errorPhone.setText("Required");
                        userEmployeeDetail_txt_errorPhone.setVisibility(View.VISIBLE);
                        phoneValid = false;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            addressValidation(userEmployeeDetail_txtInputET_addressLine);
            addressValidation(userEmployeeDetail_txtInputET_postcode);
            addressValidation(userEmployeeDetail_txtInputET_city);
        }


        if(cursorUserInfo == null)
            return;

        userEmployeeDetail_txt_userID.setText("ID# " + cursorUserInfo.getString(cursorUserInfo.getColumnIndex("userID")));
        userEmployeeDetail_txtInputET_fName.setText(cursorUserInfo.getString(cursorUserInfo.getColumnIndex("fName")));
        userEmployeeDetail_txtInputET_lName.setText(cursorUserInfo.getString(cursorUserInfo.getColumnIndex("lName")));
        userEmployeeDetail_txtInputET_email.setText(cursorUserInfo.getString(cursorUserInfo.getColumnIndex("userEmail")));
        userEmployeeDetail_txtInputET_phone.setText(cursorUserInfo.getString(cursorUserInfo.getColumnIndex("phoneNo")));

        Cursor cursorGetUserAddress = dbHelper.getUserAddress(userID);
        userEmployeeDetail_txtInputET_addressLine.setText(cursorGetUserAddress.getString(cursorGetUserAddress.getColumnIndex("addressLine")));
        userEmployeeDetail_txtInputET_postcode.setText(cursorGetUserAddress.getString(cursorGetUserAddress.getColumnIndex("postcode")));
        userEmployeeDetail_txtInputET_city.setText(cursorGetUserAddress.getString(cursorGetUserAddress.getColumnIndex("city")));

        if (userEmployeeDetail_spinner_state != null) {
            userEmployeeDetail_spinner_state.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.state_array, android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (userEmployeeDetail_spinner_state != null) {
                userEmployeeDetail_spinner_state.setAdapter(adapter);
                userEmployeeDetail_spinner_state.setSelection(adapter.getPosition(cursorGetUserAddress.getString(cursorGetUserAddress.getColumnIndex("state"))));
            }
        }

        if (getUserTypePreference.equals("SAD")){
            setETxtDisabled(userEmployeeDetail_txtInputET_fName);
            setETxtDisabled(userEmployeeDetail_txtInputET_lName);
            setETxtDisabled(userEmployeeDetail_txtInputET_email);
            setETxtDisabled(userEmployeeDetail_txtInputET_phone);
            setETxtDisabled(userEmployeeDetail_txtInputET_addressLine);
            setETxtDisabled(userEmployeeDetail_txtInputET_postcode);
            setETxtDisabled(userEmployeeDetail_txtInputET_city);

            userEmployeeDetail_spinner_state.setEnabled(false);

            userEmployeeDetail_linearLayout_userType.setVisibility(View.GONE);
            userEmployeeDetail_btn_update.setVisibility(View.GONE);
        }else if (getUserTypePreference.equals("AD")){
            if (userEmployeeDetail_spinner_investigationTeamList != null) {
                userEmployeeDetail_spinner_investigationTeamList.setOnItemSelectedListener(this);

                Cursor getOrgID = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                getOrgID.moveToFirst();

                Cursor cursorGetInvestigationTeam = dbHelper.getInvestigationTeamByOrgID(getOrgID.getString(getOrgID.getColumnIndex("orgID")));

                String arrayInvestigatorTeam[] = new String[cursorGetInvestigationTeam.getCount()];

                int i = 0;
                for(cursorGetInvestigationTeam.moveToFirst(); !cursorGetInvestigationTeam.isAfterLast(); cursorGetInvestigationTeam.moveToNext()){
                    arrayInvestigatorTeam[i++] = cursorGetInvestigationTeam.getString(cursorGetInvestigationTeam.getColumnIndex("investigationTeamName"));
                }

                ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayInvestigatorTeam);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                if (userEmployeeDetail_spinner_investigationTeamList != null) {
                    userEmployeeDetail_spinner_investigationTeamList.setAdapter(adapter);
                }
            }

            String userType = cursorUserInfo.getString(cursorUserInfo.getColumnIndex("userType"));

            RadioButton rBtn;
            if(userType.equals("AD")){
                rBtn = findViewById(R.id.userEmployeeDetail_rBtn_AD);
            }
            else if(userType.equals("EX")){
                rBtn = findViewById(R.id.userEmployeeDetail_rBtn_EX);
            }
            else if(userType.equals("IN")){
                rBtn = findViewById(R.id.userEmployeeDetail_rBtn_IN);
            }
            else{
                rBtn = findViewById(R.id.userEmployeeDetail_rBtn_RH);
            }

            rBtn.setChecked(true);

        }else{
            userEmployeeDetail_linearLayout_userType.setVisibility(View.GONE);
        }


    }

    public void setETxtDisabled(TextInputEditText txt){
        txt.setEnabled(false);
        txt.setTextColor(getResources().getColor(R.color.gray));
    }

    public void nameValidation(TextInputEditText txtInputET_nameAttribute){
        txtInputET_nameAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String fNameCheck = "", LNameCheck = "";
                    fNameCheck = userEmployeeDetail_txtInputET_fName.getText().toString();
                    LNameCheck = userEmployeeDetail_txtInputET_lName.getText().toString();

                    if(fNameCheck.isEmpty() || LNameCheck.isEmpty()){
                        userEmployeeDetail_txt_errorName.setVisibility(View.VISIBLE);
                        nameValid = false;
                    }
                    else{
                        userEmployeeDetail_txt_errorName.setVisibility(View.GONE);
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

                    addressLineCheck = userEmployeeDetail_txtInputET_addressLine.getText().toString();
                    postcodeCheck = userEmployeeDetail_txtInputET_postcode.getText().toString();
                    cityCheck = userEmployeeDetail_txtInputET_city.getText().toString();

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

                        userEmployeeDetail_txt_errorAddress.setText(errorMsgAddress);
                        userEmployeeDetail_txt_errorAddress.setVisibility(View.VISIBLE);
                        addressValid = false;
                    }
                    else{
                        userEmployeeDetail_txt_errorAddress.setText("");
                        userEmployeeDetail_txt_errorAddress.setVisibility(View.GONE);
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

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayToast(String message){
        Toast.makeText(UserOrEmployeeDetail.this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(position).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    public void update(View view) {
        if(getUserTypePreference.equals("AD") || getUserTypePreference.equals("NA")){
            if(nameValid && emailValid && phoneValid && addressValid){
                try{
                    DatabaseHelper dbHelper = new DatabaseHelper(this);

                    Boolean emailExist = dbHelper.isEmail_Exist(userEmployeeDetail_txtInputET_email.getText().toString());
                    Boolean phoneExist = dbHelper.isPhone_Exist(userEmployeeDetail_txtInputET_phone.getText().toString());

                    Cursor cursorGetUserInfo;
                    if(getUserTypePreference.equals("AD")){
                        Cursor cursorOrgID = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                        String orgID = cursorOrgID.getString(cursorOrgID.getColumnIndex("orgID"));
                        cursorGetUserInfo = dbHelper.getEmployeesByOrgID(orgID, userID);
                    }
                    else{
                        cursorGetUserInfo = dbHelper.getAllUser(userID, "All");
                    }


                    if(emailExist)
                        emailExist = !(cursorGetUserInfo.getString(cursorGetUserInfo.getColumnIndex("userEmail"))).equals(userEmployeeDetail_txtInputET_email.getText().toString());

                    if(phoneExist)
                        phoneExist = !(cursorGetUserInfo.getString(cursorGetUserInfo.getColumnIndex("phoneNo"))).equals(userEmployeeDetail_txtInputET_phone.getText().toString());

                    if(!(emailExist || phoneExist)){
                        if(dbHelper.updateUserInfo(
                            userEmployeeDetail_txtInputET_fName.getText().toString(),
                            userEmployeeDetail_txtInputET_lName.getText().toString(),
                            userEmployeeDetail_txtInputET_email.getText().toString(),
                            userEmployeeDetail_txtInputET_phone.getText().toString(),
                            userEmployeeDetail_txtInputET_addressLine.getText().toString(),
                            userEmployeeDetail_txtInputET_postcode.getText().toString(),
                            userEmployeeDetail_txtInputET_city.getText().toString(),
                            userEmployeeDetail_spinner_state.getSelectedItem().toString(),
                            userID
                        )){
                            displayToast((getUserTypePreference.equals("AD")) ? "Employee updated successfully!" : "Personal Information updated!");
                        }
                        else{
                            displayToast("Something wrong in updating");
                        }

                        finish();

                    }else{
                        if(emailExist){
                            userEmployeeDetail_txt_errorEmail.setVisibility(View.VISIBLE);
                            userEmployeeDetail_txt_errorEmail.setText("This email is alredy registered");
                        }

                        if(phoneExist){
                            userEmployeeDetail_txt_errorPhone.setVisibility(View.VISIBLE);
                            userEmployeeDetail_txt_errorPhone.setText("This phone no. is already registered");
                        }
                    }

                }catch(Exception e){
                    System.out.println("\t" + e.toString());
                }
            }else{
                displayToast("Please make sure every credential is filled in correctly");
            }
        }


    }

    public void deleteUser(View view) {
        displayAlert(R.string.delete_user_title, R.string.empty_string, R.drawable.warningiconedit);
    }

    public void displayAlert(int title, int msg, int drawable){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(title){
                            case R.string.delete_user_title:
                                DatabaseHelper dbHelper = new DatabaseHelper(UserOrEmployeeDetail.this);

//                                if(dbHelper.deleteUser(userID))
//                                    displayToast("user" + userID + " deleted");

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