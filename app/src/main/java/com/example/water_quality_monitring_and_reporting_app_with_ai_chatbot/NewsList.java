package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NewsList extends AppCompatActivity {
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userType";
    private final String userTypePreference = "userType";
    String getUserIDPreference = "";
    String getUserTypePreference = "";
    String orgID = "";

    private RecyclerView newsList_recycleV_newsList;

    private TextView newsList_txt_tabAllNews, newsList_txt_tabMyNews;

    private TextView currentlyActiveTab;

    private String newsID[];
    private String newsImageName[];
    private String newsDate[];
    private String newsTime[];
    private String newsTitle[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);

        Toolbar toolbar = findViewById(R.id.newsList_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(
                (getUserTypePreference.equals("SAD")) || (getUserTypePreference.equals("AD")) ? "News Management" : "News on Water Pollution");

        newsList_recycleV_newsList = findViewById(R.id.newsList_recycleV_newsList);

        newsList_txt_tabAllNews = findViewById(R.id.newsList_txt_tabAllNews);
        newsList_txt_tabMyNews = findViewById(R.id.newsList_txt_tabMyNews);

        if((getUserTypePreference.equals("SAD")) || (getUserTypePreference.equals("AD"))){

        }
        else{

        }

        currentlyActiveTab = newsList_txt_tabAllNews;
        //can be accessed by all kinds of user to view ALL news published.
        //admin will be having two tabs: ALL news, MY news (can manage their news)
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        //get all report can be posted in organization
        Cursor cursorGetAllNews = dbHelper.getAllNews();
        int countMyReport = (! (cursorGetAllNews==null)) ? cursorGetAllNews.getCount() : 0;

        if(countMyReport != 0){
            newsID = new String[countMyReport];
            newsImageName = new String[countMyReport];
            newsDate = new String[countMyReport];
            newsTime = new String[countMyReport];
            newsTitle = new String[countMyReport];

            displayRecyclerView(currentlyActiveTab);
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
        Toast.makeText(NewsList.this,message,Toast.LENGTH_SHORT).show();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.newsList_img_addPost:
                intent = new Intent(this, AdminReportListToPost.class);
                break;
        }

        startActivity(intent);
    }

    private void displayRecyclerView(TextView tab) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor;
        Cursor cursorImg;
        if(tab.getText().toString().equals("All News")){
            cursor = dbHelper.getAllNews();
        }else{
            cursor = dbHelper.getNewsByUserID(getUserIDPreference);
        }

        int i = 0, j = 0;
        if (cursor.moveToFirst()) {
            do {
                newsID[i] = cursor.getString(cursor.getColumnIndex("newsID"));
                cursorImg = dbHelper.getImageByNewsID(newsID[i]);

                newsImageName[i] = cursorImg.getString(cursorImg.getColumnIndex("newsImageName"));
                newsTitle[i] = cursor.getString(cursor.getColumnIndex("newsTitle"));
                newsDate[i] = cursor.getString(cursor.getColumnIndex("newsDate"));
                newsTime[i] = cursor.getString(cursor.getColumnIndex("newsTime"));

                i++;
            } while (cursor.moveToNext());

            NewsListRecycleVAdapter adapter = new NewsListRecycleVAdapter(this, newsID, newsImageName, newsDate, newsTime, newsTitle);
            newsList_recycleV_newsList.setAdapter(adapter);
            newsList_recycleV_newsList.setLayoutManager(new LinearLayoutManager(this));
        }
    }


    public void toWhichTab(View view) {
        setCurrentlyActiveTab(view.getId());
        displayRecyclerView(currentlyActiveTab);
    }

    public void setCurrentlyActiveTab(int txtID){
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.gray));
        currentlyActiveTab.setBackgroundColor(Color.WHITE);

        currentlyActiveTab = findViewById(txtID);
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.tab_text_color));
        currentlyActiveTab.setBackground(getResources().getDrawable(R.color.tab_background));

        //change the recycler view
    }
}