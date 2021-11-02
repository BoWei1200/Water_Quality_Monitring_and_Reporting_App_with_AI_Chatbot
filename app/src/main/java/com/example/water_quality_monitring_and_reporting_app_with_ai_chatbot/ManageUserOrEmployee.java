package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ManageUserOrEmployee extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";
    private final String userTypePreference = "userType";

    private String getUserTypePreference = "";
    private String getUserIDPreference = "";

    LinearLayout manageUserOrEmployee_linearLayout_searchEngine;
    EditText manageUserOrEmployee_eTxt_searchBar;
    Spinner manageUserOrEmployee_spinner_filter;

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

        manageUserOrEmployee_linearLayout_searchEngine = findViewById(R.id.manageUserOrEmployee_linearLayout_searchEngine);

        manageUserOrEmployee_eTxt_searchBar = findViewById(R.id.manageUserOrEmployee_eTxt_searchBar);
        manageUserOrEmployee_spinner_filter = findViewById(R.id.manageUserOrEmployee_spinner_filter);

        manageUserOrEmployee_recycleV_userEmployeeList = findViewById(R.id.manageUserOrEmployee_recycleV_userEmployeeList);

        String toolbarTitle = (getUserTypePreference.equals("SAD")) ? "Manage User" :
                                (getUserTypePreference.equals("AD")) ? "Manage Employee" : "null";

       // manageUserOrEmployee_spinner_filter.setVisibility(getUserTypePreference.equals("SAD") ? View.VISIBLE : View.GONE);

        getSupportActionBar().setTitle(toolbarTitle);

        manageUserOrEmployee_eTxt_searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayRecyclerView();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (manageUserOrEmployee_spinner_filter != null) {
            manageUserOrEmployee_spinner_filter.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    (getUserTypePreference.equals("SAD")) ? R.array.filter_user : R.array.filter_employee, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (manageUserOrEmployee_spinner_filter != null) {
                manageUserOrEmployee_spinner_filter.setAdapter(adapter);
            }
        }

        displayRecyclerView();
    }

    protected void onRestart(){
        super.onRestart();
        displayRecyclerView();
    }

    private void displayRecyclerView() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = null;
        Cursor cursorImg;

        switch (getUserTypePreference){
            case "SAD":
                cursor = dbHelper.getAllUser(
                        manageUserOrEmployee_eTxt_searchBar.getText().toString(),
                        manageUserOrEmployee_spinner_filter.getSelectedItem().toString()
                );
                break;

            case "AD":
                Cursor orgInfo = dbHelper.getOrgInfoByUserID(getUserIDPreference);
                String orgID = orgInfo.getString(orgInfo.getColumnIndex("orgID"));
                cursor = dbHelper.getEmployeesByOrgID(
                        orgID, manageUserOrEmployee_eTxt_searchBar.getText().toString(),
                        manageUserOrEmployee_spinner_filter.getSelectedItem().toString());
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        manageUserOrEmployee_eTxt_searchBar.setText("");
        displayRecyclerView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}