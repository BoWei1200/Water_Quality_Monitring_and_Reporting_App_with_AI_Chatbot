package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class SystemAdminAddAdmin extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextInputEditText systemAdminAddAdmin_txtInputET_fName, systemAdminAddAdmin_txtInputET_lName,
            systemAdminAddAdmin_txtInputET_email, systemAdminAddAdmin_txtInputET_phone, systemAdminAddAdmin_txtInputET_addressLine,
            systemAdminAddAdmin_txtInputET_postcode, systemAdminAddAdmin_txtInputET_city;

    private Spinner systemAdminAddAdmin_spinner_state;

    private TextView systemAdminAddAdmin_txt_errorName, systemAdminAddAdmin_txt_errorEmail, systemAdminAddAdmin_txt_errorPhone,
            systemAdminAddAdmin_txt_errorAddress;

    private Boolean nameValid = false, emailValid = false, phoneValid = false, addressValid = false;

    private static final Pattern phone_pattern = Pattern.compile("^(01)[0-46-9][0-9]{7,8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_add_admin);

        systemAdminAddAdmin_txtInputET_fName = findViewById(R.id.systemAdminAddAdmin_txtInputET_fName);
        systemAdminAddAdmin_txtInputET_lName = findViewById(R.id.systemAdminAddAdmin_txtInputET_lName);
        systemAdminAddAdmin_txtInputET_email = findViewById(R.id.systemAdminAddAdmin_txtInputET_email);
        systemAdminAddAdmin_txtInputET_phone = findViewById(R.id.systemAdminAddAdmin_txtInputET_phone);
        systemAdminAddAdmin_txtInputET_addressLine = findViewById(R.id.systemAdminAddAdmin_txtInputET_addressLine);
        systemAdminAddAdmin_txtInputET_postcode = findViewById(R.id.systemAdminAddAdmin_txtInputET_postcode);
        systemAdminAddAdmin_txtInputET_city = findViewById(R.id.systemAdminAddAdmin_txtInputET_city);

        systemAdminAddAdmin_spinner_state = findViewById(R.id.systemAdminAddAdmin_spinner_state);

        systemAdminAddAdmin_txt_errorName = findViewById(R.id.systemAdminAddAdmin_txt_errorName);
        systemAdminAddAdmin_txt_errorEmail = findViewById(R.id.systemAdminAddAdmin_txt_errorEmail);
        systemAdminAddAdmin_txt_errorPhone = findViewById(R.id.systemAdminAddAdmin_txt_errorPhone);
        systemAdminAddAdmin_txt_errorAddress = findViewById(R.id.systemAdminAddAdmin_txt_errorAddress);

        nameValidation(systemAdminAddAdmin_txtInputET_fName);
        nameValidation(systemAdminAddAdmin_txtInputET_lName);

        systemAdminAddAdmin_txtInputET_email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s) ){
                    if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        systemAdminAddAdmin_txt_errorEmail.setText("");
                        emailValid = true;
                    }else{
                        systemAdminAddAdmin_txt_errorEmail.setText("Invalid Email");
                        emailValid = false;
                    }
                }else{
                    systemAdminAddAdmin_txt_errorEmail.setText("Required");
                    emailValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        systemAdminAddAdmin_txtInputET_phone.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!systemAdminAddAdmin_txtInputET_phone.getText().toString().isEmpty()){
                    if (phone_pattern.matcher(systemAdminAddAdmin_txtInputET_phone.getText().toString()).matches()) {
                        systemAdminAddAdmin_txt_errorPhone.setText("");
                        phoneValid = true;
                    } else {
                        systemAdminAddAdmin_txt_errorPhone.setText("Invalid Malaysia Phone No.");
                        phoneValid = false;
                    }
                }else{
                    systemAdminAddAdmin_txt_errorPhone.setText("Required");
                    phoneValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addressValidation(systemAdminAddAdmin_txtInputET_addressLine);
        addressValidation(systemAdminAddAdmin_txtInputET_postcode);
        addressValidation(systemAdminAddAdmin_txtInputET_city);

        if (systemAdminAddAdmin_spinner_state != null) {
            systemAdminAddAdmin_spinner_state.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.state_array, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (systemAdminAddAdmin_spinner_state != null) {
                systemAdminAddAdmin_spinner_state.setAdapter(adapter);
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

    public void nameValidation(TextInputEditText txtInputET_nameAttribute){
        txtInputET_nameAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String fNameCheck = "", LNameCheck = "";
                    fNameCheck = systemAdminAddAdmin_txtInputET_fName.getText().toString();
                    LNameCheck = systemAdminAddAdmin_txtInputET_lName.getText().toString();

                    if(fNameCheck.isEmpty() || LNameCheck.isEmpty()){
                        systemAdminAddAdmin_txt_errorName.setText("Required");
                        nameValid = false;
                    }
                    else{
                        systemAdminAddAdmin_txt_errorName.setText("");
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

                    addressLineCheck = systemAdminAddAdmin_txtInputET_addressLine.getText().toString();
                    postcodeCheck = systemAdminAddAdmin_txtInputET_postcode.getText().toString();
                    cityCheck = systemAdminAddAdmin_txtInputET_city.getText().toString();

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

                        systemAdminAddAdmin_txt_errorAddress.setText(errorMsgAddress);
                        addressValid = false;
                    }
                    else{
                        systemAdminAddAdmin_txt_errorAddress.setText("");
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

    public void addAdmin(View view) {
        if(nameValid && emailValid && phoneValid && addressValid){
            try{
                DatabaseHelper dbHelper = new DatabaseHelper(this);

                Boolean emailExist = dbHelper.isEmail_Exist(systemAdminAddAdmin_txtInputET_email.getText().toString());
                Boolean phoneExist = dbHelper.isPhone_Exist(systemAdminAddAdmin_txtInputET_phone.getText().toString());

                if(!(emailExist || phoneExist)){

                    String randomizedPassword = "Hy" + (Math.random() * 99999);

                    if(dbHelper.addUser(
                            systemAdminAddAdmin_txtInputET_email.getText().toString(),
                            systemAdminAddAdmin_txtInputET_fName.getText().toString(),
                            systemAdminAddAdmin_txtInputET_lName.getText().toString(),
                            systemAdminAddAdmin_txtInputET_phone.getText().toString(),
                            "AD",
                            randomizedPassword,
                            systemAdminAddAdmin_txtInputET_addressLine.getText().toString(),
                            systemAdminAddAdmin_txtInputET_postcode.getText().toString(),
                            systemAdminAddAdmin_txtInputET_city.getText().toString(),
                            systemAdminAddAdmin_spinner_state.getSelectedItem().toString()
                    )){
                        displayToast("Registered Successfully!");

                        Intent intent = new Intent(this, ActivitySuccessfulDisplay.class);
                        intent.putExtra("successfulDisplayIndicator", "registration");
                        startActivity(intent);
                        finish();
                    }

                }else{
                    if(emailExist){
                        systemAdminAddAdmin_txt_errorEmail.setText("This email is alredy registered");
                        displayToast("This email has been registered");
                    }

                    if(phoneExist){
                        systemAdminAddAdmin_txt_errorPhone.setText("This phone no. is already registered");
                        displayToast("This phone no. is already registered");
                    }
                }

            }catch(Exception e){
                System.out.println("\t" + e.toString());
            }
        }else{
            displayToast("Please ensure every credential is filled in correctly");

            //shortcut
//            Intent intent = new Intent(this, ActivitySuccessfulDisplay.class);
//            intent.putExtra("successfulDisplayIndicator", "registration");
//            startActivity(intent);
//            finish();
        }
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}