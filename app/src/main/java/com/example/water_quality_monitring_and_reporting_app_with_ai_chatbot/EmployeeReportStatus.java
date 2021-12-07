package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
            employeeReportStatus_txt_reportBadWQI, employeeReportStatus_txt_reportStatus,

            employeeReportStatus_txt_InvDocHeader, employeeReportStatus_txt_INDocURL,
            employeeReportStatus_txt_uploadedURL, employeeReportStatus_txt_resolvingDocURL, employeeReportStatus_txt_uploadedResolvingDocURL;

    private EditText employeeReportStatus_eTxt_reportDuration, employeeReportStatus_eTxt_reportCause;

    private ConstraintLayout employeeReportStatus_constraintLayout_images;

    private LinearLayout employeeReportStatus_linearLayout_previous, employeeReportStatus_linearLayout_next,
            employeeReportStatus_linearLayout_belowImages,
            employeeReportStatus_linearLayout_InvDoc, employeeReportStatus_linearLayout_uploadedURL,

            employeeReportStatus_linearLayout_resolvingDoc, employeeReportStatus_linearLayout_uploadedResolvingDocURL,

            employeeReportStatus_linearLayout_btns,
                    employeeReportStatus_linearLayout_btnSubmit, employeeReportStatus_linearLayout_btnApproveReject, employeeReportStatus_linearLayout_btnUpdate;


    private ImageView employeeReportStatus_img_pollutionPhoto, employeeReportStatus_img_arrowRightOrDown;

    private Button employeeReportStatus_btn_upload, employeeReportStatus_btn_uploadResolvingDoc, employeeReportStatus_btn_submit;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private String reportID = "";

    private UserReportImage[] userReportImage;

    private String reportStatus;

    private EmployeeReportFile employeeReportFile;

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
        employeeReportStatus_txt_reportBadWQI = findViewById(R.id.employeeReportStatus_txt_reportBadWQI);
        employeeReportStatus_txt_reportStatus = findViewById(R.id.employeeReportStatus_txt_reportStatus);
        employeeReportStatus_txt_InvDocHeader = findViewById(R.id.employeeReportStatus_txt_InvDocHeader);
        employeeReportStatus_txt_INDocURL = findViewById(R.id.employeeReportStatus_txt_INDocURL);
        employeeReportStatus_txt_uploadedURL = findViewById(R.id.employeeReportStatus_txt_uploadedURL);
        employeeReportStatus_txt_resolvingDocURL = findViewById(R.id.employeeReportStatus_txt_resolvingDocURL);
        employeeReportStatus_txt_uploadedResolvingDocURL = findViewById(R.id.employeeReportStatus_txt_uploadedResolvingDocURL);

        employeeReportStatus_eTxt_reportDuration = findViewById(R.id.employeeReportStatus_eTxt_reportDuration);
        employeeReportStatus_eTxt_reportCause = findViewById(R.id.employeeReportStatus_eTxt_reportCause);

        employeeReportStatus_constraintLayout_images = findViewById(R.id.employeeReportStatus_constraintLayout_images);

        employeeReportStatus_linearLayout_previous = findViewById(R.id.employeeReportStatus_linearLayout_previous);
        employeeReportStatus_linearLayout_next = findViewById(R.id.employeeReportStatus_linearLayout_next);
        employeeReportStatus_linearLayout_belowImages = findViewById(R.id.employeeReportStatus_linearLayout_belowImages);
        employeeReportStatus_linearLayout_InvDoc = findViewById(R.id.employeeReportStatus_linearLayout_InvDoc);
        employeeReportStatus_linearLayout_uploadedURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedURL);
        employeeReportStatus_linearLayout_resolvingDoc = findViewById(R.id.employeeReportStatus_linearLayout_resolvingDoc);
        employeeReportStatus_linearLayout_uploadedResolvingDocURL = findViewById(R.id.employeeReportStatus_linearLayout_uploadedResolvingDocURL);
        employeeReportStatus_linearLayout_btns = findViewById(R.id.employeeReportStatus_linearLayout_btns);
        employeeReportStatus_linearLayout_btnUpdate = findViewById(R.id.employeeReportStatus_linearLayout_btnUpdate);
        employeeReportStatus_linearLayout_btnSubmit = findViewById(R.id.employeeReportStatus_linearLayout_btnSubmit);
        employeeReportStatus_linearLayout_btnApproveReject = findViewById(R.id.employeeReportStatus_linearLayout_btnApproveReject);

        employeeReportStatus_img_pollutionPhoto = findViewById(R.id.employeeReportStatus_img_pollutionPhoto);
        employeeReportStatus_img_arrowRightOrDown = findViewById(R.id.employeeReportStatus_img_arrowRightOrDown);

        employeeReportStatus_btn_upload = findViewById(R.id.employeeReportStatus_btn_upload);
        employeeReportStatus_btn_uploadResolvingDoc = findViewById(R.id.employeeReportStatus_btn_uploadResolvingDoc);
        employeeReportStatus_btn_submit = findViewById(R.id.employeeReportStatus_btn_submit);

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
        employeeReportStatus_txt_reportDuration.setText(!(reportEstimatedSolveDuration==null) ? reportEstimatedSolveDuration + " day(s)" : " - ");

        String reportPollutionCause = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportPollutionCause"));
        employeeReportStatus_txt_reportCause.setText(!(reportPollutionCause==null) ? reportPollutionCause : " - ");

        employeeReportStatus_txt_reportDesc.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDesc")));

        String reportBadWQI = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportBadWQI"));
        employeeReportStatus_txt_reportBadWQI.setText(!(reportBadWQI==null) ? String.format("%.2f", Double.parseDouble(reportBadWQI)) : "-");

        reportStatus = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportStatus"));
        employeeReportStatus_txt_reportStatus.setText(reportStatus);

        displayUploadedImageFromFirebase();

        if(getUserTypePreference.equals("EX")){
            employeeReportStatus_linearLayout_btns.setVisibility(View.VISIBLE);

            if(reportStatus.equals("Pending")){
                employeeReportStatus_linearLayout_btnApproveReject.setVisibility(View.VISIBLE);
            }

            else if(reportStatus.equals("Investigating1")){
                Cursor cursorGetFirstInvestigationDocByReportID = dbHelper.getInvestigationDocByReportID(reportID);
                String firstDoc = cursorGetFirstInvestigationDocByReportID.getString(cursorGetFirstInvestigationDocByReportID.getColumnIndex("firstInvestigationDocPath"));

                if(firstDoc == null){
                    employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.VISIBLE);
                }else{
                    employeeReportStatus_txt_reportDuration.setVisibility(View.GONE);
                    employeeReportStatus_eTxt_reportDuration.setVisibility(View.VISIBLE);

                    employeeReportStatus_txt_reportCause.setVisibility(View.GONE);
                    employeeReportStatus_eTxt_reportCause.setVisibility(View.VISIBLE);

                    investigationDocDisplay("First Investigation Doc", "IN1");
                    employeeReportStatus_linearLayout_btnApproveReject.setVisibility(View.VISIBLE);
                }
            }

            else if (reportStatus.equals("Examining")){
                employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
                employeeReportStatus_txt_resolvingDocURL.setVisibility(View.VISIBLE);
                retrieveFile("RH");

                investigationDocDisplay("Second Investigation Doc", "IN2");
                employeeReportStatus_linearLayout_btnApproveReject.setVisibility(View.VISIBLE);
            }

            else if (reportStatus.equals("Resolving") || reportStatus.equals("Resolved") || reportStatus.equals("Rejected")){
                if(reportStatus.equals("Resolving") || reportStatus.equals("Resolved")){
                    if(reportStatus.equals("Resolving")){
                        investigationDocDisplay("First Investigation Doc", "IN1");
                    }else{
                        investigationDocDisplay("Second Investigation Doc", "IN2");
                    }

                    if(reportStatus.equals("Resolved")){
                        employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
                        employeeReportStatus_txt_resolvingDocURL.setVisibility(View.VISIBLE);
                        retrieveFile("RH");
                    }
                }
                else{
                    Cursor cursorGetFirstInvestigationDocByReportID = dbHelper.getInvestigationDocByReportID(reportID);
                    String firstDoc = cursorGetFirstInvestigationDocByReportID.getString(cursorGetFirstInvestigationDocByReportID.getColumnIndex("firstInvestigationDocPath"));

                    if(firstDoc != null){
                        investigationDocDisplay("First Investigation Doc", "IN1");
                    }
                }

                employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.VISIBLE);
            }
        }

        else if (getUserTypePreference.equals("IN")){
            employeeReportStatus_linearLayout_btns.setVisibility(View.VISIBLE);

            Cursor cursorGetInvestigationDocByReportID = dbHelper.getInvestigationDocByReportID(reportID);

            String doc = cursorGetInvestigationDocByReportID.getString(cursorGetInvestigationDocByReportID.getColumnIndex(
                    (reportStatus.equals("Investigating1")) ? "firstInvestigationDocPath" : "secondInvestigationDocPath"));

            employeeReportStatus_linearLayout_InvDoc.setVisibility(View.VISIBLE);

            employeeReportStatus_txt_InvDocHeader.setText(
                    (reportStatus.equals("Investigating1")) ? "First Investigation Doc" : "Second Investigation Doc");

            if(doc == null){
                employeeReportStatus_btn_upload.setVisibility(View.VISIBLE);
            }else{
                employeeReportStatus_txt_INDocURL.setVisibility(View.VISIBLE);
                employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.VISIBLE);
                retrieveFile((reportStatus.equals("Investigating1")) ? "IN1" : "IN2");
            }

            if(reportStatus.equals("Examining")){
                employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
                employeeReportStatus_txt_resolvingDocURL.setVisibility(View.VISIBLE);
                retrieveFile("RH");
            }
        }

        else{ //RH
            employeeReportStatus_linearLayout_btns.setVisibility(View.VISIBLE);
            if(reportStatus.equals("Resolving")){
                investigationDocDisplay("First Investigation Doc", "IN1");
                employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
                employeeReportStatus_btn_uploadResolvingDoc.setVisibility(View.VISIBLE);
            }

            else if(reportStatus.equals("Investigating2")){
                employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
                employeeReportStatus_txt_resolvingDocURL.setVisibility(View.VISIBLE);
                retrieveFile("RH");
                investigationDocDisplay("Second Investigation Doc", "IN2");
                employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.VISIBLE);
            }

            else{

            }
        }
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void investigationDocDisplay(String docHeader, String retrieveKey){
        employeeReportStatus_linearLayout_InvDoc.setVisibility(View.VISIBLE);
        employeeReportStatus_txt_InvDocHeader.setText(docHeader);
        employeeReportStatus_txt_INDocURL.setVisibility(View.VISIBLE);
        retrieveFile(retrieveKey);
    }

    public void upload(View view) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload"), 12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null){

            if(getUserTypePreference.equals("IN")){
                employeeReportStatus_btn_upload.setVisibility(View.GONE);
                employeeReportStatus_linearLayout_uploadedURL.setVisibility(View.VISIBLE);
                employeeReportStatus_txt_uploadedURL.setText(getFileName(data.getData()));

            }else{
                employeeReportStatus_btn_uploadResolvingDoc.setVisibility(View.GONE);
                employeeReportStatus_linearLayout_uploadedResolvingDocURL.setVisibility(View.VISIBLE);
                employeeReportStatus_txt_uploadedResolvingDocURL.setText(getFileName(data.getData()));
            }

            employeeReportStatus_linearLayout_btnSubmit.setVisibility(View.VISIBLE);

            employeeReportStatus_btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper dbHelper = new DatabaseHelper(EmployeeReportStatus.this);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                    DatabaseReference databaseReference = null;
                    String fileID = "";

                    if(getUserTypePreference.equals("IN")){

                        if(reportStatus.equals("Investigating1")){
                            Cursor cursorGetFileID = dbHelper.getInvestigationDocByReportID(reportID);
                            fileID = cursorGetFileID.getString(cursorGetFileID.getColumnIndex("investigationDocID"));

                            databaseReference = FirebaseDatabase.getInstance().getReference("reportFirstInvestigationFile");

                            if(dbHelper.updateFirstInvestigationDoc(getFileName(data.getData()), reportID)){
                                displayToast("Documentation updated successfully!");
                            }else{
                                displayToast("Problem in uploading document");
                            }
                        }
                        else{
                            Cursor cursorGetFileID = dbHelper.getInvestigationDocByReportID(reportID);
                            fileID = cursorGetFileID.getString(cursorGetFileID.getColumnIndex("investigationDocID"));

                            databaseReference = FirebaseDatabase.getInstance().getReference("reportSecondInvestigationFile");

                            if(dbHelper.updateSecondInvestigationDoc(getFileName(data.getData()), reportID)){
                                displayToast("Documentation updated successfully!");

                                if(dbHelper.updateReportStatusByReportID(reportID, "Examining")){

                                }
                            }else{
                                displayToast("Problem in uploading document");
                            }
                        }

                        String filename = getFileName(data.getData());
                        StorageReference reference = storageReference.child(filename);
                        DatabaseReference finalDatabaseReference = databaseReference;
                        String finalFileID = fileID;

                        reference.putFile(data.getData()).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isComplete()) ;
                                    Uri uri = uriTask.getResult();
                                    EmployeeReportFile employeeReportFile = new EmployeeReportFile(finalFileID, filename, uri.toString());

                                    finalDatabaseReference.child(finalDatabaseReference.push().getKey()).setValue(employeeReportFile);

                                    displayToast("File uploaded");
                                }
                            }
                        );
                    }
                    // RH click submit
                    else{
                        Cursor cursorGetFileID = dbHelper.getPollutionResolvingDocByReportID(reportID);
                        fileID = cursorGetFileID.getString(cursorGetFileID.getColumnIndex("reportDealingID"));

                        databaseReference = FirebaseDatabase.getInstance().getReference("reportPollutionResolvingFile");

                        if(dbHelper.updatePollutionResolvingDoc(getFileName(data.getData()), reportID)){
                            displayToast("Documentation updated successfully!");

                            if(dbHelper.updateReportStatusByReportID(reportID, "Investigating2")){

                            }
                        }else{
                            displayToast("Problem in uploading document");
                        }

                        String filename = getFileName(data.getData());
                        StorageReference reference = storageReference.child(filename);
                        DatabaseReference finalDatabaseReference = databaseReference;
                        String finalFileID = fileID;

                        reference.putFile(data.getData()).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override

                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while(!uriTask.isComplete()) ;
                                    Uri uri = uriTask.getResult();
                                    EmployeeReportFile employeeReportFile = new EmployeeReportFile(finalFileID, filename, uri.toString());

                                    finalDatabaseReference.child(finalDatabaseReference.push().getKey()).setValue(employeeReportFile);

                                    displayToast("File uploaded");
                                }
                            }
                        );
                    }

                    finish();
                }
            });
        }
    }

    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }

    public void removeFile(View view) {
        if(getUserTypePreference.equals("IN")){
            employeeReportStatus_btn_upload.setVisibility(View.VISIBLE);
            employeeReportStatus_linearLayout_uploadedURL.setVisibility(View.GONE);
        }
        else{
            employeeReportStatus_btn_uploadResolvingDoc.setVisibility(View.VISIBLE);
            employeeReportStatus_linearLayout_uploadedResolvingDocURL.setVisibility(View.GONE);
        }

        employeeReportStatus_linearLayout_btnSubmit.setVisibility(View.GONE);
    }

    public void approveOrReject(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String updatedStatus = "";
        boolean rejectWithInCompleteOrg = false;
        switch (view.getId()){
            case R.id.employeeReportStatus_btn_approve:

                if(reportStatus.equals("Pending"))
                    updatedStatus = "Investigating1";

                else if(reportStatus.equals("Investigating1") || reportStatus.equals("Resolving")){
                    updatedStatus = "Resolving";

                    Cursor cursorGetFirstInvestigationDocByReportID = dbHelper.getInvestigationDocByReportID(reportID);
                    String firstDoc = cursorGetFirstInvestigationDocByReportID.getString(cursorGetFirstInvestigationDocByReportID.getColumnIndex("firstInvestigationDocPath"));

                    if(firstDoc == null)
                        updatedStatus = "Investigating1";
                    else{
                        if(employeeReportStatus_eTxt_reportDuration.getText().toString().isEmpty() || employeeReportStatus_eTxt_reportCause.getText().toString().isEmpty()){
                            displayToast("Please fill in required information");
                            return;
                        }
                    }
                }

                else if (reportStatus.equals("Examining") || reportStatus.equals("Resolved"))
                    updatedStatus = "Resolved";

                else if (reportStatus.equals("Rejected"))
                    updatedStatus = "Investigating1";

                break;


            case R.id.employeeReportStatus_btn_reject:
                if(reportStatus.equals("Pending") || reportStatus.equals("Rejected"))
                    updatedStatus = "Rejected";

                else if(reportStatus.equals("Investigating1") || reportStatus.equals("Resolving"))
                    updatedStatus = "Rejected";

                else if (reportStatus.equals("Examining") || reportStatus.equals("Resolved")){
                    Cursor cursorOrgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                    String orgID = cursorOrgInfo.getString(cursorOrgInfo.getColumnIndex("orgID"));

                    Boolean reportHandlerExist = false, INTeamExist = false;

                    Cursor cursorReportInfo = dbHelper.getReportInfoByReportID(reportID);
                    String reportHandler = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportHandler"));
                    String INTeam = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportInvestigationTeam"));

                    Cursor checkReportHandler = dbHelper.getEmployeesByOrgID(orgID, reportHandler, "All");
                    reportHandlerExist = (checkReportHandler != null) ?
                                            (checkReportHandler.getCount() != 0 ? true : false)
                                            : false;

                    Cursor checkINTeam = dbHelper.getAvailableTeamMemByInvestigatorTeamID(INTeam);
                    INTeamExist = (checkINTeam != null) ?
                                    (checkINTeam.getCount() > 0 ? true : false)
                                    : false;

                    if(reportHandlerExist && INTeamExist){
                        updatedStatus = "Resolving";
                        dbHelper.updateSecondInvestigationDoc(null, reportID);
                    }
                    else{
                        rejectWithInCompleteOrg = true;
                        displayAlert();
                    }

                }

                break;
        }

        if(!rejectWithInCompleteOrg){
            if(dbHelper.updateReportStatusByReportID(reportID, updatedStatus)){
                displayToast("Report status updated successfully");

                if(updatedStatus.equals("Investigating1")){
                    Cursor cursorGetReportInfo = dbHelper.getReportInfoByReportID(reportID);

                    if(cursorGetReportInfo.getString(cursorGetReportInfo.getColumnIndex("reportInvestigationTeam")) == null){
                        String reportOrgID = cursorGetReportInfo.getString(cursorGetReportInfo.getColumnIndex("orgID"));

                        String selectedInvestigationTeamID = "";
                        Cursor cursorAvailableINTeamInOrg = dbHelper.getAvailableInvestigationTeamByOrgID(reportOrgID);

                        cursorAvailableINTeamInOrg.moveToFirst();

                        Boolean INTeamIDFound = false;

                        for (int i = 0; i < cursorAvailableINTeamInOrg.getCount(); i++){
                            if(cursorAvailableINTeamInOrg.getString(cursorAvailableINTeamInOrg.getColumnIndex("reportIsTaken")).equals("0")){
                                selectedInvestigationTeamID = cursorAvailableINTeamInOrg.getString(cursorAvailableINTeamInOrg.getColumnIndex("investigationTeamID"));
                                INTeamIDFound = true;
                                break;
                            }
                            cursorAvailableINTeamInOrg.moveToNext();
                        }

                        if(!INTeamIDFound){
                            dbHelper.resetAllAvailableINTeamReportIsTakenByOrgID(reportOrgID);
                            cursorAvailableINTeamInOrg.moveToFirst();
                            selectedInvestigationTeamID = cursorAvailableINTeamInOrg.getString(cursorAvailableINTeamInOrg.getColumnIndex("investigationTeamID"));
                        }

                        System.out.println("Selected IN TEAM: " + selectedInvestigationTeamID);

                        dbHelper.resetSelectedINTeamReportIsTaken(selectedInvestigationTeamID);


                        if(dbHelper.updateReportInvestigationTeamByReportID(reportID, selectedInvestigationTeamID))
                            System.out.println("Team ID assigned: " + selectedInvestigationTeamID);
                        else
                            System.out.println("Assigned failed");
                    }
                }

                else if(updatedStatus.equals("Resolving")){
                    Cursor cursorGetReportInfo = dbHelper.getReportInfoByReportID(reportID);

                    if(cursorGetReportInfo.getString(cursorGetReportInfo.getColumnIndex("reportHandler")) == null){
                        String reportOrgID = cursorGetReportInfo.getString(cursorGetReportInfo.getColumnIndex("orgID"));

                        String selectedReportHandlerID = "";
                        Cursor cursorAvailableReportHandlerInOrg = dbHelper.getAvailableReportHandlerByOrgID(reportOrgID);

                        cursorAvailableReportHandlerInOrg.moveToFirst();

                        Boolean ReportHandlerIDFound = false;

                        for (int i = 0; i < cursorAvailableReportHandlerInOrg.getCount(); i++){
                            if(cursorAvailableReportHandlerInOrg.getString(cursorAvailableReportHandlerInOrg.getColumnIndex("reportIsTaken")).equals("0")){
                                selectedReportHandlerID = cursorAvailableReportHandlerInOrg.getString(cursorAvailableReportHandlerInOrg.getColumnIndex("userID"));
                                ReportHandlerIDFound = true;
                                break;
                            }
                            cursorAvailableReportHandlerInOrg.moveToNext();
                        }

                        if(!ReportHandlerIDFound){
                            dbHelper.resetAllAvailableEmployeeReportIsTakenByOrgIDAndUsertype(reportOrgID, "RH");
                            cursorAvailableReportHandlerInOrg.moveToFirst();
                            selectedReportHandlerID = cursorAvailableReportHandlerInOrg.getString(cursorAvailableReportHandlerInOrg.getColumnIndex("userID"));
                        }

                        System.out.println("Selected report handler: " + selectedReportHandlerID);

                        dbHelper.resetSelectedReportHandlerReportIsTaken(selectedReportHandlerID);

                        if(dbHelper.updateReportHandlerByReportID(reportID, selectedReportHandlerID))
                            System.out.println("Report Handler ID assigned: " + selectedReportHandlerID);
                        else
                            System.out.println("Assigned failed");
                    }

                    dbHelper.updateReportDurationAndCauseByReportID(
                            employeeReportStatus_eTxt_reportDuration.getText().toString(),
                            employeeReportStatus_eTxt_reportCause.getText().toString(), reportID);
                }
            }
            finish();
        }

        //displayToast(updatedStatus);
    }

    public void displayAlert(){
        new AlertDialog.Builder(this)
                .setTitle("Still Proceed with Deletion?")
                .setMessage("** CAUTION: If you reject on this report, you will lose track of the report because the report handler / investigators in charge have been removed")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHelper dbHelper = new DatabaseHelper(EmployeeReportStatus.this);

                        if(dbHelper.updateReportStatusByReportID(reportID, "Rejected")){
                            displayToast("Report status updated successfully");
                            dbHelper.updateReportExaminerByReportID(reportID, "-");
                        }

                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.warningiconedit)
                .show();
    }

    public void update(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String statusSetText = "";
        boolean firstDocNotExist = false;

        if(getUserTypePreference.equals("EX")){
            if(reportStatus.equals("Investigating1"))
                statusSetText = "Pending";

            else if (reportStatus.equals("Resolving")){
                statusSetText = "Investigating1";
                employeeReportStatus_eTxt_reportDuration.setVisibility(View.VISIBLE);
                employeeReportStatus_eTxt_reportCause.setVisibility(View.VISIBLE);

                Cursor cursorReportInfo = dbHelper.getReportInfoByReportID(reportID);

                String reportEstimatedSolveDuration = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportEstimatedSolveDuration"));
                employeeReportStatus_eTxt_reportDuration.setText(reportEstimatedSolveDuration);

                String reportPollutionCause = cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportPollutionCause"));
                employeeReportStatus_eTxt_reportCause.setText(reportPollutionCause);

                employeeReportStatus_txt_reportDuration.setVisibility(View.GONE);
                employeeReportStatus_txt_reportCause.setVisibility(View.GONE);
            }

            else if (reportStatus.equals("Resolved"))
                statusSetText = "Examining";

            else if (reportStatus.equals("Rejected"))
                statusSetText = "Pending";

            employeeReportStatus_txt_reportStatus.setText(statusSetText);
            employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.GONE);
            employeeReportStatus_linearLayout_btnApproveReject.setVisibility(View.VISIBLE);
        }

        else if(getUserTypePreference.equals("IN")){
            employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.GONE);
            employeeReportStatus_btn_upload.setVisibility(View.VISIBLE);
        }

        else{
            if (reportStatus.equals("Investigating2"))
                statusSetText = "Resolving";

            employeeReportStatus_txt_reportStatus.setText(statusSetText);
            employeeReportStatus_linearLayout_resolvingDoc.setVisibility(View.VISIBLE);
            employeeReportStatus_linearLayout_btnUpdate.setVisibility(View.GONE);
            employeeReportStatus_btn_uploadResolvingDoc.setVisibility(View.VISIBLE);
        }
    }

    public void retrieveFile(String docType){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        DatabaseReference databaseReference = null;
        Cursor cursorGetDoc;

        String fileName = "";

        if(docType.equals("IN1")){
            databaseReference = FirebaseDatabase.getInstance().getReference("reportFirstInvestigationFile");
            cursorGetDoc = dbHelper.getInvestigationDocByReportID(reportID);
            fileName = cursorGetDoc.getString(cursorGetDoc.getColumnIndex("firstInvestigationDocPath"));
        }
        else if(docType.equals("IN2")){
            databaseReference = FirebaseDatabase.getInstance().getReference("reportSecondInvestigationFile");
            cursorGetDoc = dbHelper.getInvestigationDocByReportID(reportID);
            fileName = cursorGetDoc.getString(cursorGetDoc.getColumnIndex("secondInvestigationDocPath"));
        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference("reportPollutionResolvingFile");
            cursorGetDoc = dbHelper.getPollutionResolvingDocByReportID(reportID);
            fileName = cursorGetDoc.getString(cursorGetDoc.getColumnIndex("pollutionCleaningProcDocPath"));
        }

        if(fileName == null)
            return;

        String finalFileName = fileName;

        System.out.println("Filename"+ fileName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()){
                    try{
                        EmployeeReportFile employeeReportFileRead = ds.getValue(EmployeeReportFile.class);

                        if(employeeReportFileRead.getName().equals(finalFileName)){
                            System.out.println("FILE EQUAL" + employeeReportFileRead.getName());

                            //employeeReportFile = employeeReportFileRead;
                            String fileNameWithURL = "<u>"+ employeeReportFileRead.getName() +"</u>";

                            TextView txt = (docType.equals("IN1") ||docType.equals("IN2")) ? employeeReportStatus_txt_INDocURL :employeeReportStatus_txt_resolvingDocURL;

                            txt.setText(Html.fromHtml(fileNameWithURL));
                            txt.setTextColor(getResources().getColor(R.color.teal_700));
                            txt.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setType("application/*");
                                    intent.setType("application/pdf");
                                    intent.setData(Uri.parse(employeeReportFileRead.getUrl()));
                                    startActivity(intent);
                                }
                            });
                            break;
                        }
                    }catch(Exception e){
                        System.out.println("ERROR IN FETCHING: " + e.toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                System.out.println("ERROR IN CANCEL " + error.toString());
            }
        });

    }

    public void displayUploadedImageFromFirebase() {
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

            if(imageUri[currentDisplayingPhotoIndex] != null){
                intent.putExtra("imageToDisplay", imageUri[currentDisplayingPhotoIndex].toString());
                intent.putExtra("passedActivity", "fromWeb");
            }

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

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}