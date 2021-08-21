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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private TextInputEditText registration_txtInputET_fName, registration_txtInputET_lName,
            registration_txtInputET_email, registration_txtInputET_phone, registration_txtInputET_addressLine,
            registration_txtInputET_postcode, registration_txtInputET_city;

    private Spinner registration_spinner_state;

    private EditText registration_txtInputET_password, registration_txtInputET_confirmPassword;

    private TextView registration_txt_errorName, registration_txt_errorEmail, registration_txt_errorPhone,
            registration_txt_errorAddress, registration_txt_errorPassword;

    private Boolean nameValid = false, emailValid = false, phoneValid = false, addressValid = false, paswordValid = false;

    private static final Pattern phone_pattern = Pattern.compile("^(01)[0-46-9][0-9]{7,8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registration_txtInputET_fName = findViewById(R.id.registration_txtInputET_fName);
        registration_txtInputET_lName = findViewById(R.id.registration_txtInputET_lName);
        registration_txtInputET_email = findViewById(R.id.registration_txtInputET_email);
        registration_txtInputET_phone = findViewById(R.id.registration_txtInputET_phone);
        registration_txtInputET_addressLine = findViewById(R.id.registration_txtInputET_addressLine);
        registration_txtInputET_postcode = findViewById(R.id.registration_txtInputET_postcode);
        registration_txtInputET_city = findViewById(R.id.registration_txtInputET_city);

        registration_spinner_state = findViewById(R.id.registration_spinner_state);

        registration_txtInputET_password = findViewById(R.id.registration_txtInputET_password);
        registration_txtInputET_confirmPassword = findViewById(R.id.registration_txtInputET_confirmPassword);

        registration_txt_errorName = findViewById(R.id.registration_txt_errorName);
        registration_txt_errorEmail = findViewById(R.id.registration_txt_errorEmail);
        registration_txt_errorPhone = findViewById(R.id.registration_txt_errorPhone);
        registration_txt_errorAddress = findViewById(R.id.registration_txt_errorAddress);
        registration_txt_errorPassword = findViewById(R.id.registration_txt_errorPassword);

        //RegistrationValidation registrationValidation = new RegistrationValidation();

        nameValidation(registration_txtInputET_fName);
        nameValidation(registration_txtInputET_lName);

        registration_txtInputET_email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s) ){
                    if(Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                        registration_txt_errorEmail.setText("");
                        emailValid = true;
                    }else{
                        registration_txt_errorEmail.setText("Invalid Email");
                        emailValid = false;
                    }
                }else{
                    registration_txt_errorEmail.setText("Required");
                    emailValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registration_txtInputET_phone.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!registration_txtInputET_phone.getText().toString().isEmpty()){
                    if (phone_pattern.matcher(registration_txtInputET_phone.getText().toString()).matches()) {
                        registration_txt_errorPhone.setText("");
                        phoneValid = true;
                    } else {
                        registration_txt_errorPhone.setText("Invalid Malaysia Phone No.");
                        phoneValid = false;
                    }
                }else{
                    registration_txt_errorPhone.setText("Required");
                    phoneValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addressValidation(registration_txtInputET_addressLine);
        addressValidation(registration_txtInputET_postcode);
        addressValidation(registration_txtInputET_city);

        if (registration_spinner_state != null) {
            registration_spinner_state.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.state_array, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (registration_spinner_state != null) {
                registration_spinner_state.setAdapter(adapter);
            }
        }

        passwordValidation(registration_txtInputET_password);
        passwordValidation(registration_txtInputET_confirmPassword);

        // email,
        // firstname, lastname,
        // phone no.,
        // address,
        // password,
        // confirm password

        //setup ubidots acc // redirect to the page to register for new account copy API credentials
        //enter API key
        //enter variables ID // register

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
                    fNameCheck = registration_txtInputET_fName.getText().toString();
                    LNameCheck = registration_txtInputET_lName.getText().toString();

                    if(fNameCheck.isEmpty() || LNameCheck.isEmpty()){
                        registration_txt_errorName.setText("Required");
                        nameValid = false;
                    }
                    else{
                        registration_txt_errorName.setText("");
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

                    addressLineCheck = registration_txtInputET_addressLine.getText().toString();
                    postcodeCheck = registration_txtInputET_postcode.getText().toString();
                    cityCheck = registration_txtInputET_city.getText().toString();

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

                        registration_txt_errorAddress.setText(errorMsgAddress);
                        addressValid = false;
                    }
                    else{
                        registration_txt_errorAddress.setText("");
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

    public void passwordValidation(EditText txtInputET_passwordAttribute){
        txtInputET_passwordAttribute.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String passwordCheck = "", confirmPasswordCheck = "";
                    passwordCheck = registration_txtInputET_password.getText().toString();
                    confirmPasswordCheck = registration_txtInputET_confirmPassword.getText().toString();

                    if(passwordCheck.isEmpty() || confirmPasswordCheck.isEmpty()){
                        registration_txt_errorPassword.setText("Required");
                        paswordValid = false;
                    }
                    else{
                        if(passwordCheck.equals(confirmPasswordCheck)){
                            registration_txt_errorPassword.setText("");
                            paswordValid = true;
                        }else{
                            registration_txt_errorPassword.setText("Password not matched");
                            paswordValid = false;
                        }
                    }
                }catch(Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void register(View view) {
        if(nameValid && emailValid && phoneValid && addressValid && paswordValid){
            try{
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                if(!dbHelper.isEmail_Exist(registration_txtInputET_email.getText().toString())){
                    if(dbHelper.addUser(
                        registration_txtInputET_email.getText().toString(),
                        registration_txtInputET_fName.getText().toString(),
                        registration_txtInputET_lName.getText().toString(),
                        registration_txtInputET_phone.getText().toString(),
                        "NA",
                        registration_txtInputET_confirmPassword.getText().toString(),
                        registration_txtInputET_addressLine.getText().toString(),
                        registration_txtInputET_postcode.getText().toString(),
                        registration_txtInputET_city.getText().toString(),
                        registration_spinner_state.getSelectedItem().toString()
                    )){
                        displayToast("Registered Successfully!");

                        Intent intent = new Intent(this, ActivitySuccessfulDisplay.class);
                        intent.putExtra("successfulDisplayIndicator", "registration");
                        startActivity(intent);
                        finish();
                    }

                }else{
                    registration_txt_errorEmail.setText("This email has been registered");
                    displayToast("This email has been registered");
                }

            }catch(Exception e){
                System.out.println("\t" + e.toString());
            }


            //store data to db


        }else{
            displayToast("Please ensure every credential is filled in correctly");
        }
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}