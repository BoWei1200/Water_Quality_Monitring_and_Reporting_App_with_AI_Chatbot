package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserWQIDetails extends AppCompatActivity {
    private TextView userWQIDetails_txt_title;

    private ImageView userWQIDetails_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wqidetails);

        Toolbar toolbar = findViewById(R.id.userWQIDetails_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String indicator = intent.getStringExtra("WQIInfoIndicator");
        displayToast(indicator);

        getSupportActionBar().setTitle(indicator);

        userWQIDetails_txt_title = findViewById(R.id.userWQIDetails_txt_title);

        userWQIDetails_img = findViewById(R.id.userWQIDetails_img);

        setTitleAndContent(indicator);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTitleAndContent(String indicator){
        String title = "";
        int drawable = R.drawable.rivericon;
        switch(indicator){
            case "WQI":
                title = "Water Quality Index (WQI)";

                break;

            case "DO":
                title = "Dissolved Oxygen (DO)";
                drawable = R.drawable.dissolvedoxygenicon;
                break;

            case "BOD":
                title = "Biochemical Oxygen Demand (BOD)";
                drawable = R.drawable.bodicon;
                break;

            case "COD":
                title = "Chemical Oxygen Demand (COD)";
                drawable = R.drawable.codicon;
                break;

            case "NH3N":
                title = "Ammoniacal Nitrogen (NH3-N)";
                drawable = R.drawable.nh3nicon;
                break;

            case "SS":
                title = "Suspended Solids (SS)";
                drawable = R.drawable.ssicon;
                break;

            case "pH":
                title = "pH Value";
                drawable = R.drawable.phicon;
                break;
        }

        userWQIDetails_img.setImageDrawable(getResources().getDrawable(drawable));
        userWQIDetails_txt_title.setText(title);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}