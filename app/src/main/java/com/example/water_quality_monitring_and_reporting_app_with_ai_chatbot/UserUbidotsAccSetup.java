package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

public class UserUbidotsAccSetup extends AppCompatActivity {
    private TextView userUbidotsAccSetup_txt_step1Desc, userUbidotsAccSetup_txt_errorMsgAPI;
    private TextInputEditText userUbidotsAccSetup_txtInputET_step3APIKey;

    private Boolean APIValid = false, apiKeyisExist = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ubidots_acc_setup);

        Toolbar toolbar = findViewById(R.id.userUbidotsAccSetup_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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

                startActivity(new Intent(this, UserUbidotsVarIDGathering.class));
                finish();

            }catch(Exception e){
                System.out.println(e.toString());
            }
        }else{
            Toast.makeText(this,"Ubidots API key is required to enter",Toast.LENGTH_SHORT).show();
            userUbidotsAccSetup_txt_errorMsgAPI.setVisibility(View.VISIBLE);
        }
    }

    public class ApiUbidotsSetup extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString();

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Value[] doInBackground(Integer... params) {
            try{
                System.out.println("\n\n API key: " + userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString());
                ApiClient apiClient = new ApiClient(userUbidotsAccSetup_txtInputET_step3APIKey.getText().toString());

                DataSource newDevice = apiClient.createDataSource("Water Quality Monitoring");

                newDevice.createVariable("DO");
                newDevice.createVariable("BOD");
                newDevice.createVariable("COD");
                newDevice.createVariable("NH3N");
                newDevice.createVariable("SS");
                newDevice.createVariable("pH");

                Variable[] variable = newDevice.getVariables();
            }catch (Exception e){
                Toast.makeText(this,"Ubidots API key is required to enter",Toast.LENGTH_SHORT).show();
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