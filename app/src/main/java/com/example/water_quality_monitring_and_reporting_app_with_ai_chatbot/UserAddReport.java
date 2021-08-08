package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.MenuItem;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.text.TextUtils.split;

public class UserAddReport extends AppCompatActivity implements LocationListener{
    TextInputLayout textInputLayout_name, textInputLayout_phone, textInputLayout_nric,
            textInputLayout_nric_confirm, textInputLayout_addressLine, textInputLayout_city,
            textInputLayout_postcode, textInputLayout_state, textInputLayout_password,
            textInputLayout_password_confirm;

    TextInputEditText textInputEditText_name, textInputEditText_phone, textInputEditText_nric,
            textInputEditText_nric_confirm, textInputEditText_addressLine,
            textInputEditText_city, textInputEditText_postcode, textInputEditText_state,
            textInputEditText_password, textInputEditText_password_confirm;

    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_report);

        Toolbar toolbar = findViewById(R.id.userAddReport_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        textInputEditText_addressLine = findViewById(R.id.et_register_address);
//        textInputEditText_city = findViewById(R.id.et_register_city);
//        textInputEditText_postcode = findViewById(R.id.et_register_postcode);
//        textInputEditText_state = findViewById(R.id.et_register_state);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getLocation(View view) { // WHEN USER CLICK ON 'location' ICON
        //progressBar.setVisibility(View.VISIBLE);//enable the progressbar
        grantPermission();//check if user have permission
        checkLocationIsEnabled(); // redirect user to location setting
        getLocation();//get the exact location
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void checkLocationIsEnabled() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false; //initialization
        boolean networkEnabled = false; //initialization
        //check if location is enabled
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //check if network is enabled
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!gpsEnabled && !networkEnabled) { //if GPS is disabled
            new AlertDialog.Builder(UserAddReport.this) //create Dialog box
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent is used to redirect to GPS setting
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null).show();
        }
    }

    private void grantPermission() {
        if (ActivityCompat.checkSelfPermission(UserAddReport.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(UserAddReport.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            //grant permission
            ActivityCompat.requestPermissions(UserAddReport.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            displayToast("GPS is locating your current position");
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            //get location by using latitude and longitude
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            // split the full address by ,
            String[] addressSplitList = split(addressList.get(0).getAddressLine(0), ",");
            textInputEditText_addressLine.setText(addressSplitList[0] + addressSplitList[1] + addressSplitList[2]);
            textInputEditText_city.setText(addressList.get(0).getLocality());
            textInputEditText_state.setText(addressList.get(0).getAdminArea());
            textInputEditText_postcode.setText(addressList.get(0).getPostalCode());
//            progressBar.setVisibility(View.GONE);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to detect your location.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, 1);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ImageView img = findViewById(R.id.imageView);
//            img.setImageBitmap(imageBitmap);
        }
    }
}