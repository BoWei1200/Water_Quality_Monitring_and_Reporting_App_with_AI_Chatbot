package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class EmployeeReportStatus extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp";
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserIDPreference = "";
    private String getUserTypePreference = "";

    private TextView employeeReportStatus_txt_reportID, employeeReportStatus_txt_reportDate, employeeReportStatus_txt_reportTime,
            employeeReportStatus_txt_reportAddress, employeeReportStatus_txt_reportLaLongitude, employeeReportStatus_txt_reportOrg,
            employeeReportStatus_txt_reportDuration, employeeReportStatus_txt_reportCause, employeeReportStatus_txt_reportDesc,
            employeeReportStatus_txt_reportStatus,

            employeeReportStatus_txt_InvDocHeader, employeeReportStatus_txt_INDocURL,
            employeeReportStatus_txt_uploadedURL, employeeReportStatus_txt_resolvingDocURL;

    private ConstraintLayout employeeReportStatus_constraintLayout_images;

    private LinearLayout employeeReportStatus_linearLayout_previous, employeeReportStatus_linearLayout_next,
            employeeReportStatus_linearLayout_belowImages,
            employeeReportStatus_linearLayout_InvDoc, employeeReportStatus_linearLayout_uploadedURL,

            employeeReportStatus_linearLayout_resolvingDoc, employeeReportStatus_linearLayout_uploadedResolvingDocURL,

            employeeReportStatus_linearLayout_btns,
                    employeeReportStatus_linearLayout_btnSubmit, employeeReportStatus_linearLayout_btnApproveReject;


    private ImageView employeeReportStatus_img_pollutionPhoto, employeeReportStatus_img_arrowRightOrDown;

    private Button employeeReportStatus_btn_upload, employeeReportStatus_btn_uploadResolvingDoc;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private String reportID = "";

    private UserReportImage[] userReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_report_status);

        Toolbar toolbar = findViewById(R.id.employeeReportStatus_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        employeeReportStatus_txt_reportID = findViewById(R.id.employeeReportStatus_txt_reportID);
        employeeReportStatus_txt_reportDate = findViewById(R.id.employeeReportStatus_txt_reportDate);
        employeeReportStatus_txt_reportTime = findViewById(R.id.employeeReportStatus_txt_reportTime);
        employeeReportStatus_txt_reportAddress = findViewById(R.id.employeeReportStatus_txt_reportAddress);
        employeeReportStatus_txt_reportLaLongitude = findViewById(R.id.employeeReportStatus_txt_reportLaLongitude);
        employeeReportStatus_txt_reportOrg = findViewById(R.id.employeeReportStatus_txt_reportOrg);
        employeeReportStatus_txt_reportDuration = findViewById(R.id.employeeReportStatus_txt_reportDuration);
        employeeReportStatus_txt_reportCause = findViewById(R.id.employeeReportStatus_txt_reportCause);
        employeeReportStatus_txt_reportDesc = findViewById(R.id.employeeReportStatus_txt_reportDesc);
        employeeReportStatus_txt_reportStatus = findViewById(R.id.employeeReportStatus_txt_reportStatus);
        employeeReportStatus_txt_InvDocHeader = findViewById(R.id.employeeReportStatus_txt_InvDocHeader);
        employeeReportStatus_txt_INDocURL = findViewById(R.id.employeeReportStatus_txt_INDocURL);
        employeeReportStatus_txt_uploadedURL = findViewById(R.id.employeeReportStatus_txt_uploadedURL);
        employeeReportStatus_txt_resolvingDocURL = findViewById(R.id.employeeReportStatus_txt_resolvingDocURL);

        employeeReportStatus_constraintLayout_images = findViewById(R.id.employeeReportStatus_constraintLayout_images);

        employeeReportStatus_linearLayout_previous = findViewById(R.id.employeeReportStatus_linearLayout_previous);
        employeeReportStatus_linearLayout_next = findViewById(R.id.employeeReportStatus_linearLayout_next);
        employeeReportStatus_linearLayout_belowImages = findViewById(R.id.employeeReportStatus_linearLayout_belowImages);
        employeeReportStatus_linearLayout_InvDoc = findViewById(R.id.employeeReportStatus_linearLayout_InvDoc);
        employeeReportStatus_linearLayout_uploadedURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedURL);
        employeeReportStatus_linearLayout_resolvingDoc = findViewById(R.id.employeeReportStatus_linearLayout_resolvingDoc);
        employeeReportStatus_linearLayout_uploadedResolvingDocURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedResolvingDocURL);
        employeeReportStatus_linearLayout_btns = findViewById(R.id.employeeReportStatus_linearLayout_btns);
        employeeReportStatus_linearLayout_btnSubmit = findViewById(R.id.employeeReportStatus_linearLayout_btnSubmit);
        employeeReportStatus_linearLayout_btnApproveReject = findViewById(R.id.employeeReportStatus_linearLayout_btnApproveReject);

        employeeReportStatus_img_pollutionPhoto = findViewById(R.id.employeeReportStatus_img_pollutionPhoto);
        employeeReportStatus_img_arrowRightOrDown = findViewById(R.id.employeeReportStatus_img_arrowRightOrDown);

        employeeReportStatus_btn_upload = findViewById(R.id.employeeReportStatus_btn_upload);
        employeeReportStatus_btn_uploadResolvingDoc = findViewById(R.id.employeeReportStatus_btn_uploadResolvingDoc);

        Intent intent = getIntent();
        reportID = intent.getStringExtra("reportID");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorReportInfo = dbHelper.getReportInfoByReportID(reportID);

        employeeReportStatus_txt_reportID.setText("Report# " + reportID);
        employeeReportStatus_txt_reportDate.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDate")));
        employeeReportStatus_txt_reportTime.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportTime")));

        Cursor cursorReportLocation = dbHelper.getReportLocationByReportID(reportID);

        String reportAddress = cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportaddressLine")) + ", "+
                cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportPostcode")) + ", "+
                cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportCity")) + ", "+
                cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportState"));

        employeeReportStatus_txt_reportAddress.setText(reportAddress);

        String reportLaLongitude = cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportLatitude")) + ", "+
                cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportLongitude"));

        employeeReportStatus_txt_reportLaLongitude.setText(reportLaLongitude);


        Cursor cursorGetOrgInfo = dbHelper.getOrgInfoByOrgID(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("orgID")));

        employeeReportStatus_txt_reportOrg.setText(cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName")));

        String reportEstimatedSolveDuration = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportEstimatedSolveDuration"));
        employeeReportStatus_txt_reportDuration.setText(!(reportEstimatedSolveDuration==null) ? reportEstimatedSolveDuration : " - ");

        String reportPollutionCause = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportPollutionCause"));
        employeeReportStatus_txt_reportCause.setText(!(reportPollutionCause==null) ? reportPollutionCause : " - ");

        employeeReportStatus_txt_reportDesc.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDesc")));

        String reportStatus = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportStatus"));
        employeeReportStatus_txt_reportStatus.setText(reportStatus);

        displayUploadedImageFromFirebase();


        if(getUserTypePreference.equals("EX")){
            if(reportStatus.equals("Pending")){
                employeeReportStatus_linearLayout_btns.setVisibility(View.VISIBLE);
                employeeReportStatus_linearLayout_btnApproveReject.setVisibility(View.VISIBLE);
            }

            else if(reportStatus.equals("")){

            }
        }
        else if (getUserTypePreference.equals("IN")){

        }
        else{

        }
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayUploadedImageFromFirebase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorGetImageByReportID = dbHelper.getImageByReportID(reportID);

        imageUri = new Uri[cursorGetImageByReportID.getCount()];
        userReportImage = new UserReportImage[cursorGetImageByReportID.getCount()];
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reportFromUserImage");

        System.out.println("CURSOR COUNT" + cursorGetImageByReportID.getCount());

        int finalI = 0;
        for(cursorGetImageByReportID.moveToFirst(); !cursorGetImageByReportID.isAfterLast(); cursorGetImageByReportID.moveToNext()){
            System.out.println("LOOP FOR " + finalI );

            int finalI1 = finalI;
            String imgName = cursorGetImageByReportID.getString(cursorGetImageByReportID.getColumnIndex("reportImageFilePath"));

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    for(DataSnapshot ds : snapshot.getChildren()){
                        try{
                            UserReportImage userReportImageRead = ds.getValue(UserReportImage.class);
                            if(userReportImageRead.getName().equals(imgName)){
                                userReportImage[finalI1] = ds.getValue(UserReportImage.class);
                                System.out.println("IMG URL " + userReportImage[finalI1].getUrl());
                                imageUri[finalI1] = Uri.parse(userReportImage[finalI1].getUrl());

                                Picasso.get().load(imageUri[finalI1]).resize(400,400).centerCrop().into(employeeReportStatus_img_pollutionPhoto);

                                if (finalI1 == cursorGetImageByReportID.getCount() - 1) {
                                    Picasso.get().load(imageUri[0]).resize(400,400).centerCrop().into(employeeReportStatus_img_pollutionPhoto);
                                }
                                break;
                            }
                        }catch(Exception e){
                            System.out.println("ERROR IN FETCHING: " + e.toString());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            finalI++;

            System.out.println("IMG URL FROM DB" + cursorGetImageByReportID.getString(cursorGetImageByReportID.getColumnIndex("reportImageFilePath")));
        }
        currentDisplayingPhotoIndex = 0;
        employeeReportStatus_img_pollutionPhoto.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        employeeReportStatus_img_pollutionPhoto.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        employeeReportStatus_img_pollutionPhoto.requestLayout();

        employeeReportStatus_img_pollutionPhoto.setAdjustViewBounds(true);

        employeeReportStatus_img_pollutionPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        employeeReportStatus_img_pollutionPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeReportStatus.this, ReportPhotoViewer.class);
            intent.putExtra("imageToDisplay", imageUri[currentDisplayingPhotoIndex].toString());
            intent.putExtra("passedActivity", "fromWeb");
            startActivity(intent);
        });
        prevNextandOtherBtnsDisplay();
    }

    public void prevNextandOtherBtnsDisplay(){

        if(imageUri.length - 1 > 0){
            if(currentDisplayingPhotoIndex > 0){
                employeeReportStatus_linearLayout_previous.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex < imageUri.length-1){
                employeeReportStatus_linearLayout_next.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex == 0){
                employeeReportStatus_linearLayout_previous.setVisibility(View.GONE);
            }
            if(currentDisplayingPhotoIndex == imageUri.length-1){
                employeeReportStatus_linearLayout_next.setVisibility(View.GONE);
            }
        }else{
            employeeReportStatus_linearLayout_previous.setVisibility(View.GONE);
            employeeReportStatus_linearLayout_next.setVisibility(View.GONE);
        }

        employeeReportStatus_linearLayout_previous.bringToFront();
        employeeReportStatus_linearLayout_next.bringToFront();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void viewImage(View view) {
        TransitionManager.beginDelayedTransition(employeeReportStatus_constraintLayout_images, new AutoTransition());
        if(employeeReportStatus_constraintLayout_images.getVisibility() == View.GONE){
            employeeReportStatus_constraintLayout_images.setVisibility(View.VISIBLE);
            employeeReportStatus_img_arrowRightOrDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_icon));
        }
        else{
            employeeReportStatus_constraintLayout_images.setVisibility(View.GONE);
            employeeReportStatus_img_arrowRightOrDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right_icon));
        }
        TransitionManager.beginDelayedTransition(employeeReportStatus_linearLayout_belowImages, new AutoTransition());
    }

    public void toImageViewer(View view) {
    }

    public void viewPrevious(View view) {
        Picasso.get().load(imageUri[--currentDisplayingPhotoIndex]).resize(400,400).centerCrop().into(employeeReportStatus_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        Picasso.get().load(imageUri[++currentDisplayingPhotoIndex]).resize(400,400).centerCrop().into(employeeReportStatus_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void upload(View view) {
    }

    public void removeFile(View view) {
    }

    public void submit(View view) {
    }

    public void approveOrReject(View view) {
        switch (view.getId()){
            case R.id.employeeReportStatus_btn_approve:
                break;

            case R.id.employeeReportStatus_btn_reject:
                break;
        }
    }
}