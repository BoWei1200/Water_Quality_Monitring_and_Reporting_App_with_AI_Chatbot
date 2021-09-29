package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class NewsDetail extends AppCompatActivity {
    private TextView newsDetail_txt_title, newsDetail_txt_desc;

    private ConstraintLayout newsDetail_constraintLayout_images;
    private ImageView newsDetail_img_pollutionPhoto;
    private LinearLayout newsDetail_linearLayout_previous, newsDetail_linearLayout_next;

    private Uri[] imageUri;  private int photoIndex = 0; private int currentDisplayingPhotoIndex = 0;

    private String reportID = "";

    private UserReportImage[] userReportImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent = getIntent();
        String newsID = intent.getStringExtra("newsID");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorNewsInfo = dbHelper.getNewInfoByNewsID(newsID);


        Toolbar toolbar = findViewById(R.id.newsDetail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newsDetail_txt_title = findViewById(R.id.newsDetail_txt_title);
        newsDetail_txt_desc = findViewById(R.id.newsDetail_txt_desc);

        newsDetail_constraintLayout_images = findViewById(R.id.newsDetail_constraintLayout_images);
        newsDetail_img_pollutionPhoto = findViewById(R.id.newsDetail_img_pollutionPhoto);
        newsDetail_linearLayout_previous = findViewById(R.id.newsDetail_linearLayout_previous);
        newsDetail_linearLayout_next = findViewById(R.id.newsDetail_linearLayout_next);


        newsDetail_txt_title.setText(cursorNewsInfo.getString(cursorNewsInfo.getColumnIndex("newsTitle")));
        newsDetail_txt_desc.setText(cursorNewsInfo.getString(cursorNewsInfo.getColumnIndex("newsDesc")));

        displayUploadedImageFromFirebase(newsID);
    }

    public void displayUploadedImageFromFirebase(String newsID) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorGetImageByReportID = dbHelper.getImageByNewsID(newsID);

        imageUri = new Uri[cursorGetImageByReportID.getCount()];
        userReportImage = new UserReportImage[cursorGetImageByReportID.getCount()];
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("reportFromUserImage");

        System.out.println("CURSOR COUNT" + cursorGetImageByReportID.getCount());

        int finalI = 0;
        for(cursorGetImageByReportID.moveToFirst(); !cursorGetImageByReportID.isAfterLast(); cursorGetImageByReportID.moveToNext()){
            System.out.println("LOOP FOR " + finalI );

            int finalI1 = finalI;
            String imgName = cursorGetImageByReportID.getString(cursorGetImageByReportID.getColumnIndex("newsImageName"));

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

                                Picasso.get().load(imageUri[finalI1]).fit().centerCrop().into(newsDetail_img_pollutionPhoto);

                                if (finalI1 == cursorGetImageByReportID.getCount() - 1) {
                                    Picasso.get().load(imageUri[0]).fit().centerCrop().into(newsDetail_img_pollutionPhoto);
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

            System.out.println("IMG URL FROM DB" + cursorGetImageByReportID.getString(cursorGetImageByReportID.getColumnIndex("newsImageName")));
        }
        currentDisplayingPhotoIndex = 0;

        newsDetail_img_pollutionPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(NewsDetail.this, ReportPhotoViewer.class);
            intent.putExtra("imageToDisplay", imageUri[currentDisplayingPhotoIndex].toString());
            intent.putExtra("passedActivity", "fromWeb");
            startActivity(intent);
        });
        prevNextandOtherBtnsDisplay();
    }

    public void prevNextandOtherBtnsDisplay(){

        if(imageUri.length - 1 > 0){
            if(currentDisplayingPhotoIndex > 0){
                newsDetail_linearLayout_previous.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex < imageUri.length-1){
                newsDetail_linearLayout_next.setVisibility(View.VISIBLE);
            }
            if(currentDisplayingPhotoIndex == 0){
                newsDetail_linearLayout_previous.setVisibility(View.GONE);
            }
            if(currentDisplayingPhotoIndex == imageUri.length-1){
                newsDetail_linearLayout_next.setVisibility(View.GONE);
            }
        }else{
            newsDetail_linearLayout_previous.setVisibility(View.GONE);
            newsDetail_linearLayout_next.setVisibility(View.GONE);
        }

        newsDetail_linearLayout_previous.bringToFront();
        newsDetail_linearLayout_next.bringToFront();
    }

    public void viewPrevious(View view) {
        Picasso.get().load(imageUri[--currentDisplayingPhotoIndex]).fit().centerCrop().into(newsDetail_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void viewNext(View view) {
        Picasso.get().load(imageUri[++currentDisplayingPhotoIndex]).fit().centerCrop().into(newsDetail_img_pollutionPhoto);
        prevNextandOtherBtnsDisplay();
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}