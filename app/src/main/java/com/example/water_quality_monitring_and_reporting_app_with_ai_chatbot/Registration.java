package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

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

    private Boolean nameValid = false, emailValid = false, phoneValid = false, addressValid = false, postCode = false;

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

        registration_txtInputET_fName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registration_txtInputET_lName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
                        registration_txt_errorPhone.setText("Invalid Phone No.");
                        phoneValid = false;
                    }
                }else{
                    registration_txt_errorPhone.setText("Required");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

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
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void nameValidation(){
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
            //txtErrorIC.setText(e.toString());
        }
    }
}