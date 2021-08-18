package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private EditText edLoginIC;
    private EditText edLoginPassword;
    private Button btnLogin;
    private TextView txtErrorIC, txtErrorPass;
    private DatabaseHelper dbHelper;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name

    // Key for current NRIC
    private final String NRICPreference = "NRIC";
    // Key for current isAdmin
    private final String isAdminPreference = "isAdmin"; //usertype
    private final String passwordPreference = "password";

    Boolean edLoginICValid = false, edPasswordValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edLoginIC = findViewById(R.id.ed_Login_NRIC);
        edLoginPassword = findViewById(R.id.ed_Login_Password);
        btnLogin = findViewById(R.id.btn_Login_login);
        txtErrorIC = findViewById(R.id.txt_login_errorMsgNRIC);
        txtErrorPass = findViewById(R.id.txt_login_errorMsgPassword);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String getNRICPreference = mPreferences.getString(NRICPreference, null);
        String getisAdminPreference = mPreferences.getString(isAdminPreference, null);
        String getPasswordPreference = mPreferences.getString(passwordPreference, null);

        if(getNRICPreference != null && getPasswordPreference != null){
            if(getisAdminPreference.equals("1")){ //normal user or admin?
                startActivity(new Intent(this,AdminHome.class));
            }else{
                startActivity(new Intent(this,UserHome.class));
            }
        }

        edLoginIC.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String ICCheck = "";
                    ICCheck = edLoginIC.getText().toString();
                    if(ICCheck.isEmpty()){
                        txtErrorIC.setText("Required");
                        edLoginICValid = false;
                    }
                    else{
                        txtErrorIC.setText("");
                        edLoginICValid = true;
                    }
                }catch(Exception e) {
                    txtErrorIC.setText(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edLoginPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passCheck = "";
                passCheck = edLoginPassword.getText().toString();
                if(passCheck.isEmpty()){
                    txtErrorPass.setText("Required");
                    edPasswordValid = false;
                }
                else{
                    txtErrorPass.setText("");
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
        startActivity(new Intent (this, UserHome.class));
        finish();
    }
}