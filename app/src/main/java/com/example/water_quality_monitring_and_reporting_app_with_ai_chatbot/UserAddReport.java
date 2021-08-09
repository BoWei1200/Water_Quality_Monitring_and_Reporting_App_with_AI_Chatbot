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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.material.textfield.TextInputEditText;

import static android.text.TextUtils.split;

public class UserAddReport extends AppCompatActivity implements LocationListener{

    private LinearLayout userAddReport_linearLayout_previous, userAddReport_linearLayout_next;
    private TextView userAddReport_txt_Address, userAddReport_txt_LongLatitude;

    private TextInputEditText userAddReport_etxtInput_pollutionDesc;

    private ImageView userAddReport_img_pollutionPhoto, userAddReport_img_previous, userAddReport_img_next, userAddReport_img_addIcon;

    private Bitmap[] imageBitmap; private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_report);

        Toolbar toolbar = findViewById(R.id.userAddReport_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userAddReport_linearLayout_previous = findViewById(R.id.userAddReport_linearLayout_previous);
        userAddReport_linearLayout_next = findViewById(R.id.userAddReport_linearLayout_next);

        userAddReport_txt_Address = findViewById(R.id.userAddReport_txt_Address);
        userAddReport_txt_LongLatitude = findViewById(R.id.userAddReport_txt_LaLongtitude);

        userAddReport_etxtInput_pollutionDesc = findViewById(R.id.userAddReport_etxtInput_pollutionDesc);

        userAddReport_img_pollutionPhoto = findViewById(R.id.userAddReport_img_pollutionPhoto);
        userAddReport_img_previous = findViewById(R.id.userAddReport_img_previous);
        userAddReport_img_next = findViewById(R.id.userAddReport_img_next);
        userAddReport_img_addIcon = findViewById(R.id.userAddReport_img_addIcon);

        imageBitmap = new Bitmap[5];

        getLocation(userAddReport_etxtInput_pollutionDesc);
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
            userAddReport_txt_Address.setText(addressSplitList[0]
                    + addressSplitList[1]
                    + addressSplitList[2]
                    + ", " + addressList.get(0).getLocality()
                    + ", " + addressList.get(0).getAdminArea()
                    + ", " + addressList.get(0).getPostalCode());

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            userAddReport_txt_LongLatitude.setText( latitude+ ", " +longitude);

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
        if(photoIndex < 5){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, 1);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        }else
            displayToast("You can only take 5 photos as evidence");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap[photoIndex] = (Bitmap) extras.get("data");
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            //ImageView img = findViewById(R.id.imageView);
            currentDisplayingPhotoIndex = photoIndex;

            userAddReport_img_pollutionPhoto.setImageBitmap(imageBitmap[photoIndex]);

            userAddReport_img_pollutionPhoto.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            userAddReport_img_pollutionPhoto.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            userAddReport_img_pollutionPhoto.requestLayout();

            userAddReport_img_pollutionPhoto.setAdjustViewBounds(true);

            userAddReport_img_pollutionPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

            userAddReport_img_pollutionPhoto.setOnClickListener(v -> {
                Intent intent = new Intent(UserAddReport.this, UserAddReportPhotoViewer.class);
                startActivity(intent);
            });
            photoIndex++;
            prevNextandOtherBtnsDisplay();

            userAddReport_img_addIcon.setVisibility(View.VISIBLE);

            userAddReport_linearLayout_previous.bringToFront();
            userAddReport_linearLayout_next.bringToFront();
            userAddReport_img_addIcon.bringToFront();
        }
    }

    public void report(View view) {

    }

    public void prevNextandOtherBtnsDisplay(){
        if(photoIndex-1 > 0){
            if(currentDisplayingPhotoIndex > 0){
                userAddReport_linearLayout_previous.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex < photoIndex-1){
                userAddReport_linearLayout_next.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex == 0){
                userAddReport_linearLayout_previous.setVisibility(View.GONE);
            }
            if(currentDisplayingPhotoIndex == photoIndex-1){
                userAddReport_linearLayout_next.setVisibility(View.GONE);
            }
        }else{
            userAddReport_linearLayout_previous.setVisibility(View.GONE);
            userAddReport_linearLayout_next.setVisibility(View.GONE);
        }

        if(photoIndex < 5){
            userAddReport_img_addIcon.setVisibility(View.VISIBLE);
            userAddReport_img_addIcon.bringToFront();
        }else{
            userAddReport_img_addIcon.setVisibility(View.GONE);
        }
    }

    public void viewPrevious(View view) {
        userAddReport_img_pollutionPhoto.setImageBitmap(imageBitmap[--currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        userAddReport_img_pollutionPhoto.setImageBitmap(imageBitmap[++currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
        System.out.println(currentDisplayingPhotoIndex);
        System.out.println(photoIndex);
    }
}