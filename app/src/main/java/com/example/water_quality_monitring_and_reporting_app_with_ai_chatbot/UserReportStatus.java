package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class UserReportStatus extends AppCompatActivity{

    private LinearLayout userReportStatus_linearLayout_reportStatus, userReportStatus_linearLayout_previous, userReportStatus_linearLayout_next;

    private ConstraintLayout userReportStatus_constraintLayout_images;

    private CardView userReportStatus_cv_reportStatus;

    private ImageView userReportStatus_img_pollutionPhoto, userReportStatus_img_previous, userReportStatus_img_next,

            userReportStatus_img_arrowRightOrDown,

            userReportStatus_img_dotPending, userReportStatus_img_dotInvestigating, userReportStatus_img_dotResolving,
            userReportStatus_img_dotSecondInvestigating, userReportStatus_img_dotExamining, userReportStatus_img_dotResolved,

            userReportStatus_img_iconPending, userReportStatus_img_iconInvestigating, userReportStatus_img_iconResolving,
            userReportStatus_img_iconSecondInvestigating, userReportStatus_img_iconExamining, userReportStatus_img_iconResolved;


    private TextView userReportStatus_txt_reportID, userReportStatus_txt_reportDate, userReportStatus_txt_reportTime, userReportStatus_txt_reportAddress,
            userReportStatus_txt_reportLaLongitude, userReportStatus_txt_reportOrg, userReportStatus_txt_reportDuration, userReportStatus_txt_reportCause, userReportStatus_txt_reportDesc,

            userReportStatus_txt_pendingHeader, userReportStatus_txt_investigatingHeader, userReportStatus_txt_resolvingHeader,
            userReportStatus_txt_secondInvestigatingHeader, userReportStatus_txt_examiningHeader, userReportStatus_txt_resolvedHeader,

            userReportStatus_txt_pending, userReportStatus_txt_investigating, userReportStatus_txt_resolving, userReportStatus_txt_secondInvestigating,
            userReportStatus_txt_examining, userReportStatus_txt_resolved;


    private View userReportStatus_view_goToInvestigating, userReportStatus_view_goToResolving, userReportStatus_view_goToSecondInvestigating,
            userReportStatus_view_goToExamining, userReportStatus_view_goToResolved;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private String reportID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_status);

        userReportStatus_linearLayout_reportStatus = findViewById(R.id.userReportStatus_linearLayout_reportStatus);
        userReportStatus_linearLayout_previous = findViewById(R.id.userReportStatus_linearLayout_previous);
        userReportStatus_linearLayout_next = findViewById(R.id.userReportStatus_linearLayout_next);

        userReportStatus_constraintLayout_images = findViewById(R.id.userReportStatus_constraintLayout_images);

        userReportStatus_cv_reportStatus = findViewById(R.id.userReportStatus_cv_reportStatus);

        userReportStatus_img_pollutionPhoto = findViewById(R.id.userReportStatus_img_pollutionPhoto);
        userReportStatus_img_previous = findViewById(R.id.userReportStatus_img_previous);
        userReportStatus_img_next = findViewById(R.id.userReportStatus_img_next);

        userReportStatus_img_dotPending = findViewById(R.id.userReportStatus_img_dotPending);
        userReportStatus_img_dotInvestigating = findViewById(R.id.userReportStatus_img_dotInvestigating);
        userReportStatus_img_dotResolving = findViewById(R.id.userReportStatus_img_dotResolving);
        userReportStatus_img_dotSecondInvestigating = findViewById(R.id.userReportStatus_img_dotSecondInvestigating);
        userReportStatus_img_dotExamining = findViewById(R.id.userReportStatus_img_dotExamining);
        userReportStatus_img_dotResolved = findViewById(R.id.userReportStatus_img_dotResolved);

        userReportStatus_img_iconPending = findViewById(R.id.userReportStatus_img_iconPending);
        userReportStatus_img_iconInvestigating = findViewById(R.id.userReportStatus_img_iconInvestigating);
        userReportStatus_img_iconResolving = findViewById(R.id.userReportStatus_img_iconResolving);
        userReportStatus_img_iconSecondInvestigating = findViewById(R.id.userReportStatus_img_iconSecondInvestigating);
        userReportStatus_img_iconExamining = findViewById(R.id.userReportStatus_img_iconExamining);
        userReportStatus_img_iconResolved = findViewById(R.id.userReportStatus_img_iconResolved);

        userReportStatus_img_arrowRightOrDown = findViewById(R.id.userReportStatus_img_arrowRightOrDown);

        userReportStatus_txt_reportID = findViewById(R.id.userReportStatus_txt_reportID);
        userReportStatus_txt_reportDate = findViewById(R.id.userReportStatus_txt_reportDate);
        userReportStatus_txt_reportTime = findViewById(R.id.userReportStatus_txt_reportTime);
        userReportStatus_txt_reportAddress = findViewById(R.id.userReportStatus_txt_reportAddress);
        userReportStatus_txt_reportLaLongitude = findViewById(R.id.userReportStatus_txt_reportLaLongitude);
        userReportStatus_txt_reportOrg = findViewById(R.id.userReportStatus_txt_reportOrg);
        userReportStatus_txt_reportDuration = findViewById(R.id.userReportStatus_txt_reportDuration);
        userReportStatus_txt_reportCause = findViewById(R.id.userReportStatus_txt_reportCause);
        userReportStatus_txt_reportDesc = findViewById(R.id.userReportStatus_txt_reportDesc);

        userReportStatus_txt_pendingHeader = findViewById(R.id.userReportStatus_txt_pendingHeader);
        userReportStatus_txt_investigatingHeader = findViewById(R.id.userReportStatus_txt_investigatingHeader);
        userReportStatus_txt_resolvingHeader = findViewById(R.id.userReportStatus_txt_resolvingHeader);
        userReportStatus_txt_secondInvestigatingHeader=  findViewById(R.id.userReportStatus_txt_secondInvestigatingHeader);
        userReportStatus_txt_examiningHeader = findViewById(R.id.userReportStatus_txt_examiningHeader);
        userReportStatus_txt_resolvedHeader = findViewById(R.id.userReportStatus_txt_resolvedHeader);

        userReportStatus_txt_pending = findViewById(R.id.userReportStatus_txt_pending);
        userReportStatus_txt_investigating = findViewById(R.id.userReportStatus_txt_investigating);
        userReportStatus_txt_resolving = findViewById(R.id.userReportStatus_txt_resolving);
        userReportStatus_txt_secondInvestigating = findViewById(R.id.userReportStatus_txt_secondInvestigating);
        userReportStatus_txt_examining = findViewById(R.id.userReportStatus_txt_examining);
        userReportStatus_txt_resolved = findViewById(R.id.userReportStatus_txt_resolved);

        userReportStatus_view_goToInvestigating = findViewById(R.id.userReportStatus_view_goToInvestigating);
        userReportStatus_view_goToResolving = findViewById(R.id.userReportStatus_view_goToResolving);
        userReportStatus_view_goToSecondInvestigating = findViewById(R.id.userReportStatus_view_goToSecondInvestigating);
        userReportStatus_view_goToExamining = findViewById(R.id.userReportStatus_view_goToExamining);
        userReportStatus_view_goToResolved = findViewById(R.id.userReportStatus_view_goToResolved);

        Intent intent = getIntent();
        reportID = intent.getStringExtra("reportID");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorReportInfo = dbHelper.getReportInfoByReportID(reportID);

        userReportStatus_txt_reportID.setText("Report# " + reportID);
        userReportStatus_txt_reportDate.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDate")));
        userReportStatus_txt_reportTime.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportTime")));

        Cursor cursorReportLocation = dbHelper.getReportLocationByReportID(reportID);

        String reportAddress = cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportaddressLine")) + ", "+
                         cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportPostcode")) + ", "+
                         cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportCity")) + ", "+
                         cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportState"));

        userReportStatus_txt_reportAddress.setText(reportAddress);

        String reportLaLongitude = cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportLatitude")) + ", "+
                            cursorReportLocation.getString(cursorReportLocation.getColumnIndex("reportLongitude"));

        userReportStatus_txt_reportLaLongitude.setText(reportLaLongitude);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void viewImage(View view) {
        TransitionManager.beginDelayedTransition(userReportStatus_constraintLayout_images, new AutoTransition());
        if(userReportStatus_constraintLayout_images.getVisibility() == View.GONE){
            userReportStatus_constraintLayout_images.setVisibility(View.VISIBLE);
            userReportStatus_img_arrowRightOrDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down_icon));
        }
        else{
            userReportStatus_constraintLayout_images.setVisibility(View.GONE);
            userReportStatus_img_arrowRightOrDown.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_right_icon));
        }
        TransitionManager.beginDelayedTransition(userReportStatus_linearLayout_reportStatus, new AutoTransition());
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