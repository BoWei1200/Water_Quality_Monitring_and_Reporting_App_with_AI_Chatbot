package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;

public class AdminAddEmployee extends AppCompatActivity {

    private TextInputEditText adminAddEmployee_txtInputET_fName, adminAddEmployee_txtInputET_lName, adminAddEmployee_txtInputET_email,
            adminAddEmployee_txtInputET_phone, adminAddEmployee_txtInputET_addressLine, adminAddEmployee_txtInputET_postcode,
            adminAddEmployee_txtInputET_city;

    private Spinner adminAddEmployee_spinner_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_employee);

        Toolbar toolbar = findViewById(R.id.adminAddEmployee_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adminAddEmployee_txtInputET_fName = findViewById(R.id.adminAddEmployee_txtInputET_fName);
        adminAddEmployee_txtInputET_lName  =findViewById(R.id.adminAddEmployee_txtInputET_lName);
        adminAddEmployee_txtInputET_email = findViewById(R.id.adminAddEmployee_txtInputET_email);
        adminAddEmployee_txtInputET_phone = findViewById(R.id.adminAddEmployee_txtInputET_phone);
        adminAddEmployee_txtInputET_addressLine = findViewById(R.id.adminAddEmployee_txtInputET_addressLine);
        adminAddEmployee_txtInputET_postcode = findViewById(R.id.adminAddEmployee_txtInputET_postcode);
        adminAddEmployee_txtInputET_city = findViewById(R.id.adminAddEmployee_txtInputET_city);

        adminAddEmployee_spinner_state = findViewById(R.id.adminAddEmployee_spinner_state);
        



    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void addEmployee(View view) {

    }
}