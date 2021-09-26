package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsAdd extends AppCompatActivity {
    private TextView newsAdd_txt_errorTitle, newsAdd_txt_errorDesc, newsAdd_txt_errorImage;
    private String reportID;

    private String reportImgName[];
    ArrayList<String> imgNameSelected = new ArrayList<String>();

    EditText newsAdd_eTxt_title, newsAdd_eTxt_desc;
    RecyclerView newsAdd_recycleV_imgGallery;

    Boolean titleValid = false, descValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_add);

        Toolbar toolbar = findViewById(R.id.newsAdd_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        newsAdd_txt_errorTitle = findViewById(R.id.newsAdd_txt_errorTitle);
        newsAdd_txt_errorDesc = findViewById(R.id.newsAdd_txt_errorDesc);
        newsAdd_txt_errorImage = findViewById(R.id.newsAdd_txt_errorImage);

        newsAdd_eTxt_title = findViewById(R.id.newsAdd_eTxt_title);
        newsAdd_eTxt_desc = findViewById(R.id.newsAdd_eTxt_desc);
        newsAdd_recycleV_imgGallery = findViewById(R.id.newsAdd_recycleV_imgGallery);

        Intent intent = getIntent();
        reportID = intent.getStringExtra("reportID");

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursorReportInfo = dbHelper.getReportInfoByReportID(reportID);
        newsAdd_eTxt_desc.setText(cursorReportInfo.getString(cursorReportInfo.getColumnIndex("reportDesc")));

        Cursor cursorGetImg = dbHelper.getImageByReportID(reportID);
        int countReportImages = cursorGetImg.getCount();
        reportImgName = new String[countReportImages];

        loadMyReportFromDatabase();

        ImageGalleryRecycleVAdapter adapter = new ImageGalleryRecycleVAdapter(this, reportImgName, imgNameSelected);
        newsAdd_recycleV_imgGallery.setAdapter(adapter);
        newsAdd_recycleV_imgGallery.setLayoutManager(new LinearLayoutManager(this));

        txtValidation(newsAdd_eTxt_title, newsAdd_txt_errorTitle);
        txtValidation(newsAdd_eTxt_desc, newsAdd_txt_errorDesc);
    }

    public void txtValidation(EditText eTxt, TextView txtError){
        eTxt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String txtCheck = "";
                    txtCheck = eTxt.getText().toString();

                    if(txtCheck.isEmpty()){
                        txtError.setVisibility(View.VISIBLE);
                        setValidity(eTxt, false);
                    }
                    else{
                        txtError.setVisibility(View.GONE);
                        setValidity(eTxt, true);
                    }
                }catch(Exception e) {
                    System.out.println(e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setValidity(EditText eTxt, Boolean validity){
        switch (eTxt.getId()){
            case R.id.newsAdd_eTxt_title:
                titleValid = validity;
                break;

            case R.id.newsAdd_eTxt_desc:
                descValid = validity;
                break;
        }
    }

    private void loadMyReportFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getImageByReportID(reportID);

        int i = 0, j = 0;
        if (cursor.moveToFirst()) {
            do {
                reportImgName[i] = cursor.getString(cursor.getColumnIndex("reportImageFilePath"));
                i++;
            } while (cursor.moveToNext());
        }
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayToast(String message){
        Toast.makeText(NewsAdd.this,message,Toast.LENGTH_SHORT).show();
    }

    public void post(View view) {
        if(imgNameSelected.size() == 0){
            displayToast("Make sure information required filled in properly");
            return;
        }

        if(descValid && titleValid){

        }else{
            displayToast("Make sure information required filled in properly");
        }
         String selectedImg = "";
         for (int i = 0; i < imgNameSelected.size(); i++)
             selectedImg += imgNameSelected.get(i) + ", ";

        System.out.println(selectedImg);
    }
}