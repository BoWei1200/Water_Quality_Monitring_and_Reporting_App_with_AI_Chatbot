package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

public class UserUbidotsAccSetup extends AppCompatActivity {
    private TextView userUbidotsAccSetup_txt_step1Desc, userUbidotsAccSetup_txt_errorMsgAPI;
    private TextInputEditText userUbidotsAccSetup_txtInputET_step3APIKey;


    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String APIExistPreference = "APIExist";
    SharedPreferences.Editor editor;


    private Boolean APIValid = false, apiKeyisExist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_acc_setup);

        Toolbar toolbar = findViewById(R.id.userUbidotsAccSetup_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editor = mPreferences.edit();

        editor.putString(APIExistPreference, "0"); //set to not exist initially
        editor.commit();

        userUbidotsAccSetup_txt_step1Desc = findViewById(R.id.userUbidotsAccSetup_txt_step1Desc);
        userUbidotsAccSetup_txtInputET_step3APIKey = findViewById(R.id.userUbidotsAccSetup_txtInputET_step3APIKey);
        userUbidotsAccSetup_txt_errorMsgAPI = findViewById(R.id.userUbidotsAccSetup_txt_errorMsgAPI);

        userUbidotsAccSetup_txt_step1Desc.setClickable(true);
        userUbidotsAccSetup_txt_step1Desc.setMovementMethod(LinkMovementMethod.getInstance());
        userUbidotsAccSetup_txt_step1Desc.setText(Html.fromHtml("Please register or login your Ubidots account <br/><br/>" +
                "        Click the link below for: <br/><br/>" +
                "        1. \tRegistration: <a href = \"https://industrial.ubidots.com/accounts/signup_industrial/\">https://industrial.ubidots.com/accounts/signup_industrial/</a> <br/>" +
                "        2. \tLogin: <a href = \"https://industrial.ubidots.com/accounts/signin/\">https://industrial.ubidots.com/accounts/signin/"));

        userUbidotsAccSetup_txtInputET_step3APIKey.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String API = "";

                try{
                    API = userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString();
                    if(!API.isEmpty()){
                        userUbidotsAccSetup_txt_errorMsgAPI.setVisibility(View.GONE);
                        APIValid = true;
                    }else{
                        userUbidotsAccSetup_txt_errorMsgAPI.setVisibility(View.VISIBLE);
                        APIValid = false;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
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

    public void next(View view) {
        if(APIValid){
            try{
                String API_KEY = userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString();
                System.out.println(API_KEY);
                //ApiUbidots ubidots = new ApiUbidots(API_KEY);
                new ApiUbidotsSetup().execute();

                startActivity(new Intent(this, UserUbidotsAPIkeyLoading_Searching.class));
                finish();

            }catch(Exception e){
                System.out.println("ERROR OUTSIDE CLASS     "+ e.toString());
            }
        }else{
            Toast.makeText(this,"Ubidots API key is required to enter",Toast.LENGTH_SHORT).show();
            userUbidotsAccSetup_txt_errorMsgAPI.setVisibility(View.VISIBLE);
        }
    }

    public void displayToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public class ApiUbidotsSetup extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString();
        private ApiClient apiClient;
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Value[] doInBackground(Integer... params) {
            try{
                System.out.println("\n\n API key: " + userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString());
                apiClient = new ApiClient(userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString());

                DataSource newDevice = apiClient.createDataSource("Water Quality Monitoring");

                newDevice.createVariable("do");
                newDevice.createVariable("bod");
                newDevice.createVariable("cod");
                newDevice.createVariable("nh3n");
                newDevice.createVariable("ss");
                newDevice.createVariable("ph");

                try{
                    Variable[] variable = apiClient.getVariables();

                    startActivity(new Intent(UserUbidotsAccSetup.this, UserUbidotsScanDevice.class));
                    finish();
                }catch(Exception e){
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    try{
                                        displayToast("API key not existing");
                                    }catch(Exception e){
                                    }
                                }
                            });

//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                }
                        }
                    }).start();

                    System.out.println("ERROR!!      "+ e.toString());
                    startActivity(getIntent());
                    finish();
                }
//                System.out.println(mPreferences.getString(APIExistPreference, null));

            }catch (Exception e){
                System.out.println("HIHIH  " + e.toString());
            }
            Value[] valuesDO = new Value[0];
            return valuesDO;
        }

        @Override
        protected void onPostExecute(Value[] variableValues) {
            super.onPostExecute(variableValues);
        }
    }
}