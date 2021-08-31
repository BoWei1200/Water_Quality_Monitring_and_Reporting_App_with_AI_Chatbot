package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class SystemAdminAddOrg extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextInputEditText systemAdminAddOrg_txtInputET_orgName, systemAdminAddOrg_txtInputET_addressLine,
            systemAdminAddOrg_txtInputET_postcode, systemAdminAddOrg_txtInputET_city;

    private Spinner systemAdminAddOrg_spinner_state;

    private TextView systemAdminAddOrg_txt_errorName, systemAdminAddOrg_txt_errorAddress;

    private Boolean orgNameValid = false, orgAddressValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_add_org);

        Toolbar toolbar = findViewById(R.id.systemAdminOrg_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        systemAdminAddOrg_txtInputET_orgName = findViewById(R.id.systemAdminAddOrg_txtInputET_orgName);
        systemAdminAddOrg_txtInputET_addressLine = findViewById(R.id.systemAdminAddOrg_txtInputET_addressLine);
        systemAdminAddOrg_txtInputET_postcode = findViewById(R.id.systemAdminAddOrg_txtInputET_postcode);
        systemAdminAddOrg_txtInputET_city = findViewById(R.id.systemAdminAddOrg_txtInputET_city);
        systemAdminAddOrg_spinner_state = findViewById(R.id.systemAdminAddOrg_spinner_state);

        systemAdminAddOrg_txt_errorName = findViewById(R.id.systemAdminAddOrg_txt_errorName);
        systemAdminAddOrg_txt_errorAddress = findViewById(R.id.systemAdminAddOrg_txt_errorAddress);

        systemAdminAddOrg_txtInputET_orgName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(systemAdminAddOrg_txtInputET_orgName.getText().toString().isEmpty()){
                    systemAdminAddOrg_txt_errorName.setVisibility(View.VISIBLE);
                    orgNameValid = false;
                }else{
                    systemAdminAddOrg_txt_errorName.setVisibility(View.GONE);
                    orgNameValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addressValidation(systemAdminAddOrg_txtInputET_addressLine);
        addressValidation(systemAdminAddOrg_txtInputET_postcode);
        addressValidation(systemAdminAddOrg_txtInputET_city);

        if (systemAdminAddOrg_spinner_state != null) {
            systemAdminAddOrg_spinner_state.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.state_array, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (systemAdminAddOrg_spinner_state != null) {
                systemAdminAddOrg_spinner_state.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String spinnerLabel = adapterView.getItemAtPosition(position).toString();
        //displayToast(spinnerLabel);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addressValidation(TextInputEditText txtInputET_addressAttribute){
        txtInputET_addressAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String addressLineCheck = "", postcodeCheck = "", cityCheck = "";

                    addressLineCheck = systemAdminAddOrg_txtInputET_addressLine.getText().toString();
                    postcodeCheck = systemAdminAddOrg_txtInputET_postcode.getText().toString();
                    cityCheck = systemAdminAddOrg_txtInputET_city.getText().toString();

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

                        systemAdminAddOrg_txt_errorAddress.setText(errorMsgAddress);
                        orgAddressValid = false;
                    }
                    else{
                        systemAdminAddOrg_txt_errorAddress.setText("");
                        orgAddressValid = true;
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void add(View view) {
        if(orgNameValid && orgAddressValid){

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            if(!dbHelper.isOrgExist(systemAdminAddOrg_txtInputET_orgName.getText().toString())){
                if(dbHelper.addOrg(
                        systemAdminAddOrg_txtInputET_orgName.getText().toString(),
                        systemAdminAddOrg_txtInputET_addressLine.getText().toString(),
                        systemAdminAddOrg_txtInputET_postcode.getText().toString(),
                        systemAdminAddOrg_txtInputET_city.getText().toString(),
                        systemAdminAddOrg_spinner_state.getSelectedItem().toString())){

                    String orgID = dbHelper.getorgID(systemAdminAddOrg_txtInputET_orgName.getText().toString());
                    Intent intent = new Intent(this, SystemAdminAddAdmin.class);
                    intent.putExtra("orgID", orgID);
                    startActivity(intent);
                    finish();
                }
                else{
                    displayToast("Something wrong with the insertion! Please try again later");
                }
            }
            else{
                displayToast("Organization name already exists");
            }
        }
        else{
            displayToast("Please make sure every crendetial is filled in correctly");
        }
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}