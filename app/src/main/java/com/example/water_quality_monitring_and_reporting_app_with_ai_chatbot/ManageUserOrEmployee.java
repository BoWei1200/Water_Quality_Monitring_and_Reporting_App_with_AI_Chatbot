package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

public class ManageUserOrEmployee extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userTypePreference = "userType";

    private String getUserTypePreference = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        Toolbar toolbar = findViewById(R.id.manageUser_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        System.out.println("usertype" + getUserTypePreference);

//        String toolbarTitle = "";
//        if(getUserTypePreference == "SAD")
//            toolbarTitle = "User Management";
//        else if (getUserTypePreference == "AD")
//            toolbarTitle = "Employee Management";

        String toolbarTitle = (getUserTypePreference.equals("SAD")) ? "Manage User" :
                                (getUserTypePreference.equals("AD")) ? "Manage Employee" : "null";
        getSupportActionBar().setTitle(toolbarTitle);


    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}