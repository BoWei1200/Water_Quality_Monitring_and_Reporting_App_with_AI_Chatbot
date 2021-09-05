package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.text.TextUtils.split;

public class UserAddReport extends AppCompatActivity implements LocationListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";

    private String getUserIDPreference = "";

    private StorageReference storageReference;
    private DatabaseReference databaseReferece;

    private LinearLayout userAddReport_linearLayout_previous, userAddReport_linearLayout_next;
    private TextView userAddReport_txt_Address, userAddReport_txt_LongLatitude,
            userAddReport_txt_photoAmount, userAddReport_txt_errorMsgDesc,
            userAddReport_txt_errorMsgLaLongitude, userAddReport_txt_errorMsgAddress;

    private TextInputEditText userAddReport_etxtInput_pollutionDesc;

    private ImageView userAddReport_img_pollutionPhoto, userAddReport_img_previous, userAddReport_img_next, userAddReport_img_addIcon, userAddReport_img_deleteIcon;

    private Uri[] imageUri; Uri currentTakenImage; private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private int getLocation = 0;

    Boolean discardOrNot = false, deleteOrNot = false;

    LocationManager locationManager;
    String[] addressSplitList = null;
    double longitude = 0.0;
    double latitude = 0.0;

    List<Address> addressList = null;

    String[] reportImageFilePaths;

    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_report);

        Toolbar toolbar = findViewById(R.id.userAddReport_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReferece = FirebaseDatabase.getInstance().getReference("reportFromUserImage");

        userAddReport_linearLayout_previous = findViewById(R.id.userAddReport_linearLayout_previous);
        userAddReport_linearLayout_next = findViewById(R.id.userAddReport_linearLayout_next);

        userAddReport_txt_Address = findViewById(R.id.userAddReport_txt_Address);
        userAddReport_txt_LongLatitude = findViewById(R.id.userAddReport_txt_LaLongtitude);
        userAddReport_txt_photoAmount = findViewById(R.id.userAddReport_txt_photoAmount);
        userAddReport_txt_errorMsgDesc = findViewById(R.id.userAddReport_txt_errorMsgDesc);
        userAddReport_txt_errorMsgLaLongitude = findViewById(R.id.userAddReport_txt_errorMsgLaLongitude);
        userAddReport_txt_errorMsgAddress = findViewById(R.id.userAddReport_txt_errorMsgAddress);

        userAddReport_etxtInput_pollutionDesc = findViewById(R.id.userAddReport_etxtInput_pollutionDesc);

        userAddReport_img_pollutionPhoto = findViewById(R.id.userAddReport_img_pollutionPhoto);
        userAddReport_img_previous = findViewById(R.id.userAddReport_img_previous);
        userAddReport_img_next = findViewById(R.id.userAddReport_img_next);
        userAddReport_img_addIcon = findViewById(R.id.userAddReport_img_addIcon);
        userAddReport_img_deleteIcon = findViewById(R.id.userAddReport_img_deleteIcon);

        imageUri = new Uri[5];

        if(getLocation == 0){
            getLocation(userAddReport_etxtInput_pollutionDesc);
            getLocation++;
        }

        userAddReport_etxtInput_pollutionDesc.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String descCheck = "";
                    descCheck = userAddReport_etxtInput_pollutionDesc.getText().toString();
                    if(descCheck.isEmpty()){
                        userAddReport_txt_errorMsgDesc.setVisibility(View.VISIBLE);
                    }
                    else{
                        userAddReport_txt_errorMsgDesc.setVisibility(View.GONE);
                    }
                }catch(Exception e) {

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
            displayAlert(R.string.discard_report_title, R.string.discard_report_desc, R.drawable.warningiconedit);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        displayAlert(R.string.discard_report_title, R.string.discard_report_desc, R.drawable.warningiconedit);
        if(discardOrNot){
            super.onBackPressed();
        }
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
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Intent is used to redirect to GPS setting
                            finish();
                        }
                    }).show();
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
            addressList = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            // split the full address by ,
            addressSplitList = split(addressList.get(0).getAddressLine(0), ",");
            userAddReport_txt_Address.setText(addressSplitList[0]
                    + addressSplitList[1]
                    + addressSplitList[2]
                    + ", " + addressList.get(0).getLocality()
                    + ", " + addressList.get(0).getAdminArea()
                    + ", " + addressList.get(0).getPostalCode());

            longitude = location.getLongitude();
            latitude = location.getLatitude();

            userAddReport_txt_LongLatitude.setText( latitude+ ", " +longitude);
            userAddReport_txt_errorMsgLaLongitude.setVisibility(View.GONE);
            userAddReport_txt_errorMsgAddress.setVisibility(View.GONE);

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
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                currentTakenImage = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentTakenImage);
                startActivityForResult(takePictureIntent, IMAGE_PICK_CAMERA_CODE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        }else
            displayToast("You can only take 5 photos as evidence");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                imageUri[photoIndex] = (Uri) extras.get("data");
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //ImageView img = findViewById(R.id.imageView);

            imageUri[photoIndex] = currentTakenImage;
            currentDisplayingPhotoIndex = photoIndex;

            userAddReport_img_pollutionPhoto.setImageURI(imageUri[photoIndex]);

            userAddReport_img_pollutionPhoto.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            userAddReport_img_pollutionPhoto.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

            userAddReport_img_pollutionPhoto.requestLayout();

            userAddReport_img_pollutionPhoto.setAdjustViewBounds(true);

            userAddReport_img_pollutionPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

            userAddReport_img_pollutionPhoto.setOnClickListener(v -> {
                Intent intent = new Intent(UserAddReport.this, UserAddReportPhotoViewer.class);
                intent.putExtra("imageToDisplay", imageUri[currentDisplayingPhotoIndex].toString());
                startActivity(intent);
            });

            photoIndex++;
            prevNextandOtherBtnsDisplay();
            setTxtPhotoAmount();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void report(View view) {
        if(photoIndex != 0
                && !userAddReport_etxtInput_pollutionDesc.getText().toString().isEmpty()
                && !userAddReport_txt_LongLatitude.getText().equals("")
                && !userAddReport_txt_Address.getText().equals("")){

            reportImageFilePaths = new String[photoIndex];
            //store image to file
            saveImage(imageUri);

            //store to db
            String reportDesc = userAddReport_etxtInput_pollutionDesc.getText().toString();

            DateTimeFormatter currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime nowDate = LocalDateTime.now();
            String reportDate = currentDate.format(nowDate);

            DateTimeFormatter currentTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime nowTime = LocalDateTime.now();
            String reportTime = currentTime.format(nowTime);

            String reportaddressLine = addressSplitList[0] + addressSplitList[1] + addressSplitList[2];
            String reportPostcode = addressList.get(0).getPostalCode();
            String reportCity = addressList.get(0).getLocality();
            String reportState = addressList.get(0).getAdminArea();

            DatabaseHelper dbHelper = new DatabaseHelper(this);

            String selectedOrgPostcode = "";

            String prevPostcode = "";
            String availableOrgPostcode = "";
            Cursor cursorAvailableOrgPostcode = dbHelper.getAvailableOrgPostcodeByState(reportState);

            Boolean found = false;

            for (cursorAvailableOrgPostcode.moveToFirst(); !cursorAvailableOrgPostcode.isAfterLast(); cursorAvailableOrgPostcode.moveToNext()) {
                availableOrgPostcode = cursorAvailableOrgPostcode.getString(cursorAvailableOrgPostcode.getColumnIndex("orgPostCode"));

                if(reportPostcode.compareTo(availableOrgPostcode) > 0){
                    prevPostcode = availableOrgPostcode;
                    continue;
                }
                else if(reportPostcode.compareTo(availableOrgPostcode) == 0){
                    selectedOrgPostcode = availableOrgPostcode;
                    found = true;
                    break;
                }
                else{
                    break;
                }
            }

            if(!found){
                if(prevPostcode == "")
                    selectedOrgPostcode = availableOrgPostcode;
                else{
                    if(cursorAvailableOrgPostcode.isAfterLast()){
                        selectedOrgPostcode = prevPostcode;
                    }
                    else{
                        int diffWithPrev = reportPostcode.compareTo(prevPostcode);
                        int diffWithNext = availableOrgPostcode.compareTo(reportPostcode);

                        if(diffWithPrev < diffWithNext){
                            selectedOrgPostcode = prevPostcode;
                        }
                        else if(diffWithNext < diffWithPrev){
                            selectedOrgPostcode = availableOrgPostcode;
                        }
                        else{
                            Random r = new Random();
                            int low = 1;
                            int high = 2;
                            int randomNum = r.nextInt(high-low) + low;

                            selectedOrgPostcode = (randomNum == 1) ? prevPostcode : availableOrgPostcode;
                        }
                    }
                }
            }

            String selectedOrgID = "";
            Cursor cursorAvailableOrgID = dbHelper.getAvailableOrgIDBySelectedPostcode(selectedOrgPostcode);
            cursorAvailableOrgID.moveToFirst();

            selectedOrgID = (cursorAvailableOrgID.getCount() != 0) ? dbHelper.getOrgIDWithLeastReports(cursorAvailableOrgID) :  dbHelper.getorgID("Department of Environment (DOE) Kuala Lumpur");

            //sequentially get examiner.
            System.out.println("Selected OrgID: " +selectedOrgID);
            String selectedExaminerID = "";

            Cursor cursorAvailableExaminerID = dbHelper.getAvailableExaminerByOrgID(selectedOrgID);
            cursorAvailableExaminerID.moveToFirst();

            selectedExaminerID = dbHelper.getExaminerIDWithLeastReports(cursorAvailableExaminerID);

            if(dbHelper.addReport(reportDesc, reportDate, reportTime, "Pending", selectedExaminerID, selectedOrgID, getUserIDPreference,
                    reportImageFilePaths, reportaddressLine, reportPostcode, reportCity, reportState, Double.toString(latitude), Double.toString(longitude))){

                displayToast("Reported Successfully!");
                finish();
            }
        }else{
            displayToast("Please make sure every requirement is completed");
            //String errorMsg = ""; int errorAmount = 0;
            if(photoIndex == 0){
                userAddReport_txt_photoAmount.setTextColor(getResources().getColor(R.color.red));
            }

            if(userAddReport_etxtInput_pollutionDesc.getText().toString().isEmpty()){
                userAddReport_txt_errorMsgDesc.setVisibility(View.VISIBLE);
            }

            if(userAddReport_txt_LongLatitude.getText().equals("")){
                userAddReport_txt_errorMsgLaLongitude.setVisibility(View.VISIBLE);
            }

            if(userAddReport_txt_Address.getText().equals("")){
                userAddReport_txt_errorMsgAddress.setVisibility(View.VISIBLE);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveImage(Uri[] imageUri) {



//        //String root = Environment.getExternalStorageDirectory().toString();
//        File filePathString = Environment.getExternalStorageDirectory();
//        File filePath = new File(filePathString + "/reported_water_pollution_images/");
//        //String root = "/sdcard";
////        System.out.println("root: " + root);
////        String imgFilePath = root + "/reported_water_pollution_images/";
////        System.out.println(imgFilePath);
//        if(!filePath.exists())
//            filePath.mkdirs();
////        File myDir = new File(imgFilePath);
////        myDir.mkdirs();

        DateTimeFormatter currentDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime nowDate = LocalDateTime.now();
        String reportDateTime = currentDate.format(nowDate);

        String imageName = getUserIDPreference + reportDateTime;

        for (int i = 0; i < photoIndex; i++){
            String imageNameConcat = "Image-" + imageName + i + ".jpg";
            reportImageFilePaths[i] = imageNameConcat;
            StorageReference reference =storageReference.child(imageNameConcat);
            reference.putFile(imageUri[i]).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isComplete()) ;
                            Uri uri = uriTask.getResult();

                            databaseReferece.child(databaseReferece.push().getKey()).setValue(imageNameConcat);
                        }
                    }
            );

//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri[i]);
//                BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(reportImageFilePaths[i], false);
//                bitmap = decoder.decodeRegion(new Rect(10, 10, 50, 50), null);
//
//                FileOutputStream out = new FileOutputStream(file);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                out.flush();
//                out.close();
//
//            } catch (Exception e) {
//                System.out.println("ERROR in Storing images: " + e.toString());
//            }
        }
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

        if(photoIndex > 0 && photoIndex < 5){
            userAddReport_img_addIcon.setVisibility(View.VISIBLE);
            userAddReport_img_deleteIcon.setVisibility(View.VISIBLE);
        }else{
            if(photoIndex == 5){
                userAddReport_img_deleteIcon.setVisibility(View.VISIBLE);
            }else{
                userAddReport_img_deleteIcon.setVisibility(View.GONE);
            }
            userAddReport_img_addIcon.setVisibility(View.GONE);
        }

        userAddReport_linearLayout_previous.bringToFront();
        userAddReport_linearLayout_next.bringToFront();
        userAddReport_img_addIcon.bringToFront();
        userAddReport_img_deleteIcon.bringToFront();
    }

    public void viewPrevious(View view) {
        userAddReport_img_pollutionPhoto.setImageURI(imageUri[--currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        userAddReport_img_pollutionPhoto.setImageURI(imageUri[++currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
    }

    public void displayAlert(int title, int msg, int drawable){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(title){
                            case R.string.discard_report_title:
                                discardOrNot = true;
                                finish();
                                break;

                            case R.string.delete_photo_title:
                                Uri[] anotherPhotoArray = new Uri[imageUri.length];

                                for (int i = 0, k = 0; i < imageUri.length; i++) {
                                    if (i == currentDisplayingPhotoIndex)
                                        continue;

                                    anotherPhotoArray[k++] = imageUri[i];
                                }

                                imageUri = anotherPhotoArray;
                                photoIndex--;

                                if(photoIndex == 0){
                                    userAddReport_img_pollutionPhoto.setImageDrawable(getResources().getDrawable(R.drawable.takephoto));

                                    int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());
                                    userAddReport_img_pollutionPhoto.getLayoutParams().height = dimensionInDp;
                                    userAddReport_img_pollutionPhoto.getLayoutParams().width = dimensionInDp;

                                    userAddReport_img_pollutionPhoto.requestLayout();

                                    userAddReport_img_pollutionPhoto.setOnClickListener(UserAddReport.this::takePhoto);
                                }else {
                                    if(currentDisplayingPhotoIndex == 0){
                                        userAddReport_img_pollutionPhoto.setImageURI(imageUri[currentDisplayingPhotoIndex]);
                                    }else{
                                        userAddReport_img_pollutionPhoto.setImageURI(imageUri[--currentDisplayingPhotoIndex]);
                                    }
                                }

                                prevNextandOtherBtnsDisplay();

                                setTxtPhotoAmount();
                                break;
                        }
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(drawable)
                .show();
    }

    public void setTxtPhotoAmount(){
        int num = photoIndex;
        userAddReport_txt_photoAmount.setText(Integer.toString(num) + "/5");
        userAddReport_txt_photoAmount.setTextColor(getResources().getColor(R.color.gray));
    }

    public void deletePhoto(View view) {
        displayAlert(R.string.delete_photo_title, R.string.empty_string, R.drawable.deleteicon2edit);
    }
}