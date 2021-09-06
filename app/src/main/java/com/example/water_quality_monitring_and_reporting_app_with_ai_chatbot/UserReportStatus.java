package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UserReportStatus extends AppCompatActivity {

    private LinearLayout userReportStatus_linearLayout_previous, userReportStatus_linearLayout_next;

    private ImageView userReportStatus_img_pollutionPhoto, userReportStatus_img_previous,
            userReportStatus_img_next;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_status);

        userReportStatus_linearLayout_previous = findViewById(R.id.userReportStatus_linearLayout_previous);
        userReportStatus_linearLayout_next = findViewById(R.id.userReportStatus_linearLayout_next);

        userReportStatus_img_pollutionPhoto = findViewById(R.id.userReportStatus_img_pollutionPhoto);
        userReportStatus_img_previous = findViewById(R.id.userReportStatus_img_previous);
        userReportStatus_img_next = findViewById(R.id.userReportStatus_img_next);

    }

    public void viewImage(View view) {
    }

    public void toImageViewer(View view) {
    }

    public void prevNextandOtherBtnsDisplay(){
        if(photoIndex-1 > 0){
            if(currentDisplayingPhotoIndex > 0){
                userReportStatus_linearLayout_previous.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex < photoIndex-1){
                userReportStatus_linearLayout_next.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex == 0){
                userReportStatus_linearLayout_previous.setVisibility(View.GONE);
            }
            if(currentDisplayingPhotoIndex == photoIndex-1){
                userReportStatus_linearLayout_next.setVisibility(View.GONE);
            }
        }else{
            userReportStatus_linearLayout_previous.setVisibility(View.GONE);
            userReportStatus_linearLayout_next.setVisibility(View.GONE);
        }

        userReportStatus_linearLayout_previous.bringToFront();
        userReportStatus_linearLayout_next.bringToFront();
    }

    public void viewPrevious(View view) {
        userReportStatus_img_pollutionPhoto.setImageURI(imageUri[--currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        userReportStatus_img_pollutionPhoto.setImageURI(imageUri[++currentDisplayingPhotoIndex]);
        prevNextandOtherBtnsDisplay();
    }
}