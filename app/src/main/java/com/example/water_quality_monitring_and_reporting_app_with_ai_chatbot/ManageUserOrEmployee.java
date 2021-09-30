package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

public class ManageUserOrEmployee extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserTypePreference = "";
    private String getUserIDPreference = "";

    RecyclerView manageUserOrEmployee_recycleV_userEmployeeList;

    private String userID[];
    private String userName[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_or_employee);

        Toolbar toolbar = findViewById(R.id.manageUserOrEmployee_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        getUserTypePreference = mPreferences.getString(userTypePreference, null);
        getUserIDPreference = mPreferences.getString(userIDPreference, null);

        manageUserOrEmployee_recycleV_userEmployeeList = findViewById(R.id.manageUserOrEmployee_recycleV_userEmployeeList);

        System.out.println("usertype" + getUserTypePreference);

        String toolbarTitle = (getUserTypePreference.equals("SAD")) ? "Manage User" :
                                (getUserTypePreference.equals("AD")) ? "Manage Employee" : "null";
        getSupportActionBar().setTitle(toolbarTitle);


        displayRecyclerView();
    }

    private void displayRecyclerView() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        Cursor cursorImg;

        switch (getUserTypePreference){
            case "SAD":
                cursor = dbHelper.getAllUser();
                break;

            case "AD":
                Cursor orgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                String orgID = orgInfo.getString(orgInfo.getColumnIndex("orgID"));
                cursor = dbHelper.getEmployeesByOrgID(orgID);
                break;
        }

        int i = 0, j = 0;

        int countMyReport = (! (cursor == null)) ? cursor.getCount() : 0;

        userID = new String[countMyReport];
        userName = new String[countMyReport];

        if (cursor!= null) {
            do {
                userID[i] = cursor.getString(cursor.getColumnIndex("userID"));
                userName[i] = cursor.getString(cursor.getColumnIndex("fName")) + " " + cursor.getString(cursor.getColumnIndex("lName"));

                i++;
            } while (cursor.moveToNext());
        }

        UserEmployeeRecycleVAdapter adapter = new UserEmployeeRecycleVAdapter(this, userID, userName);
        manageUserOrEmployee_recycleV_userEmployeeList.setAdapter(adapter);
        manageUserOrEmployee_recycleV_userEmployeeList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}