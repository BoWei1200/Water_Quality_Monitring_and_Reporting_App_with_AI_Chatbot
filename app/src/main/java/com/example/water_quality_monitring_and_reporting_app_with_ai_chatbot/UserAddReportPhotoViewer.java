package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;

public class UserAddReportPhotoViewer extends AppCompatActivity{
    private ImageView userAddReportPhotoViewer_img_imgToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add_report_photo_viewer);

        Bundle bundle = getIntent().getExtras();
        userAddReportPhotoViewer_img_imgToDisplay = findViewById(R.id.userAddReportPhotoViewer_img_imgToDisplay);
        userAddReportPhotoViewer_img_imgToDisplay.setImageURI(Uri.parse(bundle.getString("imageToDisplay")));

        userAddReportPhotoViewer_img_imgToDisplay.setAdjustViewBounds(true);

        userAddReportPhotoViewer_img_imgToDisplay.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}