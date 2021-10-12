package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

public class UserReportStatus extends AppCompatActivity{

    StorageReference storageReference;

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
            userReportStatus_txt_reportLaLongitude, userReportStatus_txt_reportOrg, userReportStatus_txt_reportDuration, userReportStatus_txt_reportCause,
            userReportStatus_txt_reportDesc, userReportStatus_txt_reportBadWQI,

            userReportStatus_txt_pendingHeader, userReportStatus_txt_investigatingHeader, userReportStatus_txt_resolvingHeader,
            userReportStatus_txt_secondInvestigatingHeader, userReportStatus_txt_examiningHeader, userReportStatus_txt_resolvedHeader,

            userReportStatus_txt_pending, userReportStatus_txt_investigating, userReportStatus_txt_resolving, userReportStatus_txt_secondInvestigating,
            userReportStatus_txt_examining, userReportStatus_txt_resolved;


    private View userReportStatus_view_goToInvestigating, userReportStatus_view_goToResolving, userReportStatus_view_goToSecondInvestigating,
            userReportStatus_view_goToExamining, userReportStatus_view_goToResolved;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private String reportID = "";

    private UserReportImage[] userReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report_status);

        Toolbar toolbar = findViewById(R.id.userReportStatus_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        userReportStatus_txt_reportBadWQI = findViewById(R.id.userReportStatus_txt_reportBadWQI);

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


        Cursor cursorGetOrgInfo = dbHelper.getOrgInfoByOrgID(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("orgID")));

        userReportStatus_txt_reportOrg.setText(cursorGetOrgInfo.getString(cursorGetOrgInfo.getColumnIndex("orgName")));

        String reportEstimatedSolveDuration = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportEstimatedSolveDuration"));
        userReportStatus_txt_reportDuration.setText(!(reportEstimatedSolveDuration==null) ? reportEstimatedSolveDuration : " - ");

        String reportPollutionCause = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportPollutionCause"));
        userReportStatus_txt_reportCause.setText(!(reportPollutionCause==null) ? reportPollutionCause : " - ");

        userReportStatus_txt_reportDesc.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDesc")));

        String reportBadWQI = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportBadWQI"));
        userReportStatus_txt_reportBadWQI.setText(!(reportBadWQI==null) ? String.format("%.2f", Double.parseDouble(reportBadWQI)) : "-");

        displayUploadedImageFromFirebase();

        setReportStatus(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportStatus")));
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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

    private void displayUploadedImageFromFirebase(){
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

                                Picasso.get().load(imageUri[finalI1]).resize(400,400).centerCrop().into(userReportStatus_img_pollutionPhoto);

                                if (finalI1 == cursorGetImageByReportID.getCount() - 1) {
                                    Picasso.get().load(imageUri[0]).resize(400,400).centerCrop().into(userReportStatus_img_pollutionPhoto);
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
        userReportStatus_img_pollutionPhoto.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        userReportStatus_img_pollutionPhoto.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

        userReportStatus_img_pollutionPhoto.requestLayout();

        userReportStatus_img_pollutionPhoto.setAdjustViewBounds(true);

        userReportStatus_img_pollutionPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);

        userReportStatus_img_pollutionPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(UserReportStatus.this, ReportPhotoViewer.class);
            intent.putExtra("imageToDisplay", imageUri[currentDisplayingPhotoIndex].toString());
            intent.putExtra("passedActivity", "fromWeb");
            startActivity(intent);
        });
        prevNextandOtherBtnsDisplay();
    }

    private void setReportStatus(String reportStatus) {
        int background = R.drawable.circle_img_resolved;
        if(!reportStatus.equals("Rejected")){
            switch(reportStatus){
                case "Resolved":
                    userReportStatus_img_dotResolved.setBackground(getResources().getDrawable(R.drawable.circle_img_resolved));
                    userReportStatus_img_iconResolved.setBackground(getResources().getDrawable(R.drawable.circle_img_resolved));
                    userReportStatus_txt_resolvedHeader.setTextColor(getResources().getColor(R.color.gray));
                    userReportStatus_txt_resolved.setTextColor(getResources().getColor(R.color.gray));

                case "Examining":
                    setResources(reportStatus, "Examining", R.drawable.circle_img_examining,
                            userReportStatus_img_dotExamining, userReportStatus_img_iconExamining,
                            userReportStatus_txt_examiningHeader, "Examined",
                            userReportStatus_txt_examining, "Your report has been examined!",
                            userReportStatus_view_goToResolved);

                case "Investigating2":
                    setResources(reportStatus, "Investigating2", R.drawable.circle_img_investigating,
                            userReportStatus_img_dotSecondInvestigating, userReportStatus_img_iconSecondInvestigating,
                            userReportStatus_txt_secondInvestigatingHeader, "Investigated",
                            userReportStatus_txt_secondInvestigating, "Your report has been proceeded second investigation!",
                            userReportStatus_view_goToExamining);

                case "Resolving":
                    setResources(reportStatus, "Resolving", R.drawable.circle_img_resolving,
                            userReportStatus_img_dotResolving, userReportStatus_img_iconResolving,
                            userReportStatus_txt_resolvingHeader, "Resolving",
                            userReportStatus_txt_resolving, "Your report has been resolved and under second investigation!",
                            userReportStatus_view_goToSecondInvestigating);

                case "Investigating1":
                    setResources(reportStatus, "Investigating1", R.drawable.circle_img_investigating,
                            userReportStatus_img_dotInvestigating, userReportStatus_img_iconInvestigating,
                            userReportStatus_txt_investigatingHeader, "Investigated",
                            userReportStatus_txt_investigating, "Your report has been proceeded first investigation!",
                            userReportStatus_view_goToResolving);

                case "Pending":
                    setResources(reportStatus, "Pending", R.drawable.circle_img_pending,
                            userReportStatus_img_dotPending, userReportStatus_img_iconPending,
                            userReportStatus_txt_pendingHeader, "Validated",
                            userReportStatus_txt_pending, "Your report has been validated!",
                            userReportStatus_view_goToInvestigating);
            }
        }else{
            userReportStatus_img_dotPending.setBackground(getResources().getDrawable(R.drawable.circle_img_rejected));
            userReportStatus_img_iconPending.setBackground(getResources().getDrawable(R.drawable.circle_img_rejected));
            userReportStatus_img_iconPending.setImageDrawable(getResources().getDrawable(R.drawable.ic_reportrejected_icon));

            userReportStatus_txt_pendingHeader.setText("Rejected");
            userReportStatus_txt_pending.setText("Your report has been rejected");
        }
    }

    public void setResources(String reportStatus, String reportStatusToCompare, int backgroundPassed,
                             ImageView dot, ImageView icon,
                             TextView txtHeader, String headerText,
                             TextView txt, String text,
                             View line){

        int background = backgroundPassed;

        if(!reportStatus.equals(reportStatusToCompare)){
            background = R.drawable.circle_img_resolved;
            icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_reportresolved_icon));
            txtHeader.setText(headerText);
            txt.setText(text);
        }

        dot.setBackground(getResources().getDrawable(background));
        icon.setBackground(getResources().getDrawable(background));
        txtHeader.setTextColor(getResources().getColor(R.color.gray));
        txt.setTextColor(getResources().getColor(R.color.gray));

    }

    public void toImageViewer(View view) {
    }

    public void prevNextandOtherBtnsDisplay(){

        if(imageUri.length - 1 > 0){
            if(currentDisplayingPhotoIndex > 0){
                userReportStatus_linearLayout_previous.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex < imageUri.length-1){
                userReportStatus_linearLayout_next.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex == 0){
                userReportStatus_linearLayout_previous.setVisibility(View.GONE);
            }
            if(currentDisplayingPhotoIndex == imageUri.length-1){
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
        Picasso.get().load(imageUri[--currentDisplayingPhotoIndex]).resize(400,400).centerCrop().into(userReportStatus_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        Picasso.get().load(imageUri[++currentDisplayingPhotoIndex]).resize(400,400).centerCrop().into(userReportStatus_img_pollutionPhoto);

        //Glide.with(this).load(userReportImage[++currentDisplayingPhotoIndex].getUrl()).into(userReportStatus_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void deleteReport(View view) {
        displayAlert(R.string.delete_selected_report, R.string.empty_string, R.drawable.warningiconedit);
    }

    public void displayAlert(int title, int msg, int drawable){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch(title){
                            case R.string.delete_selected_report:
                                DatabaseHelper dbHelper = new DatabaseHelper(UserReportStatus.this);
                                ArrayList<String> reportIDSelected = new ArrayList<>();
                                reportIDSelected.add(reportID);

                                if(dbHelper.deleteReport(reportIDSelected))
                                    displayToast("Selected report(s) deleted");

                                finish();
                                break;
                        }
                    }
                })

                .setNegativeButton(android.R.string.no, null)
                .setIcon(drawable)
                .show();
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}