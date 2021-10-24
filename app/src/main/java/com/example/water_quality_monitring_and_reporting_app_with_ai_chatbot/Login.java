package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private EditText login_eTxt_email;
    private EditText login_eTxt_password;
    private TextView login_txt_errorMsgEmail, login_txt_errorMsgPassword;
    private DatabaseHelper dbHelper;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String emailPreference = "email";
    private final String userTypePreference = "userType";
    private final String passwordPreference = "password";
    private final String apiPreference = "api";

    Boolean edLoginEmailValid = false, edPasswordValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_eTxt_email = findViewById(R.id.login_eTxt_email);
        login_eTxt_password = findViewById(R.id.login_eTxt_password);
        login_txt_errorMsgEmail = findViewById(R.id.login_txt_errorMsgEmail);
        login_txt_errorMsgPassword = findViewById(R.id.login_txt_errorMsgPassword);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String getuserIDPreference = mPreferences.getString(userIDPreference, null);
        String getEmailPreference = mPreferences.getString(emailPreference, null);
        String getUserTypePreference = mPreferences.getString(userTypePreference, null);
        String getPasswordPreference = mPreferences.getString(passwordPreference, null);

        if(getuserIDPreference != null && getEmailPreference != null && getPasswordPreference != null && getUserTypePreference != null){
            toWhichPageByUserType(getUserTypePreference);
        }

        login_eTxt_email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String emailCheck = "";
                    emailCheck = login_eTxt_email.getText().toString();
                    if(emailCheck.isEmpty()){
                        login_txt_errorMsgEmail.setText("Required");
                        edLoginEmailValid = false;
                    }
                    else{
                        login_txt_errorMsgEmail.setText("");
                        edLoginEmailValid = true;
                    }
                }catch(Exception e) {
                    login_txt_errorMsgEmail.setText(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        login_eTxt_password.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passCheck = "";
                passCheck = login_eTxt_password.getText().toString();
                if(passCheck.isEmpty()){
                    login_txt_errorMsgPassword.setText("Required");
                    edPasswordValid = false;
                }
                else{
                    login_txt_errorMsgPassword.setText("");
                    edPasswordValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finishAffinity();
    }


    public void login(View view) {
        if(edLoginEmailValid && edPasswordValid){
            String edEmail = login_eTxt_email.getText().toString();
            String edPassword = login_eTxt_password.getText().toString();

            try{
                dbHelper = new DatabaseHelper(this);

                if(dbHelper.isEmail_Exist(edEmail)){ // email existing or not?
                    Cursor cursor = dbHelper.readInfo(edEmail);
                    cursor.moveToFirst();
                    String passwordDb = cursor.getString(cursor.getColumnIndex("password"));
                    String userType = cursor.getString(cursor.getColumnIndex("userType"));
                    String name = cursor.getString(cursor.getColumnIndex("fName")) +  " " +cursor.getString(cursor.getColumnIndex("lName"));

                    if(!edPassword.equals(passwordDb)){
                        Toast.makeText(Login.this, "Invalid email or password!",Toast.LENGTH_SHORT).show();
                    }else{
                        SharedPreferences.Editor editor = mPreferences.edit();

                        editor.putString(userIDPreference, dbHelper.getUserID(edEmail));
                        editor.putString(emailPreference, edEmail);
                        editor.putString(userTypePreference, userType); //user or admin
                        editor.putString(passwordPreference, edPassword);
                        editor.putString(apiPreference, dbHelper.getAPIKey(dbHelper.getUserID(edEmail)));

                        editor.commit();

                        //To show the account holder name that the user logged in
                        displayToast("Welcome, "+name+" !");

                        toWhichPageByUserType(userType);

                        finish();
                    }
                }else{
                    displayToast("Invalid email or password!");
                }

            }catch(Exception e){
                System.out.println(e.toString());
            }
        }
        else{
            displayToast("Please make sure every credential is filled in correctly");
        }

        //shortcut to app
//        startActivity(new Intent (this, UserHome.class));
//        finish();
    }

    public void toWhichPageByUserType(String userType){
        if(userType.equals("NA")){ //normal user
            startActivity(new Intent(this,UserHome.class));
        }
        else if (userType.equals("SAD")){
            startActivity(new Intent(this,SystemAdminHome.class));
        }
        else if (userType.equals("AD")){
            startActivity(new Intent(this,AdminHome.class));
        }
        else if(userType.equals("IN")){
            startActivity(new Intent(this,InvestigatorHome.class));
        }
        else if(userType.equals("EX")){
            startActivity(new Intent(this,ExaminerHome.class));
        }
        else if(userType.equals("RH")){
            startActivity(new Intent(this,ReportHandlerHome.class));
        }
    }

    public void toUserRegister(View view) {
        startActivity(new Intent (this, Registration.class));
    }

    public void displayToast(String message){
        Toast.makeText(Login.this,message,Toast.LENGTH_SHORT).show();
    }
}