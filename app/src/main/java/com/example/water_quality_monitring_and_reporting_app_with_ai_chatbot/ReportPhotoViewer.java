package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ReportPhotoViewer extends AppCompatActivity{
    private ImageView reportPhotoViewer_img_imgToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_photo_viewer);

        Bundle bundle = getIntent().getExtras();
        reportPhotoViewer_img_imgToDisplay = findViewById(R.id.reportPhotoViewer_img_imgToDisplay);

        if(bundle != null)
            if(bundle.getString("passedActivity").equals("addReport")){
                reportPhotoViewer_img_imgToDisplay.setImageURI(Uri.parse(bundle.getString("imageToDisplay")));
            }else{
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height= displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                Picasso.get().load(Uri.parse(bundle.getString("imageToDisplay"))).resize(width,height).centerCrop().into(reportPhotoViewer_img_imgToDisplay);
            }

        reportPhotoViewer_img_imgToDisplay.setAdjustViewBounds(true);

        reportPhotoViewer_img_imgToDisplay.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}