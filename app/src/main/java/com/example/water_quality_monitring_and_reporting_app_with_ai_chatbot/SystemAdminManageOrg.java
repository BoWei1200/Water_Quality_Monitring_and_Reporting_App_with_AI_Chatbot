package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

public class SystemAdminManageOrg extends AppCompatActivity {

    EditText systemAdminManageOrg_eTxt_searchBar;
    RecyclerView systemAdminManageOrg_recycleV_orgList;

    private String orgID[];
    private String orgName[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin_manage_org);

        Toolbar toolbar = findViewById(R.id.systemAdminManageOrg_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        systemAdminManageOrg_eTxt_searchBar = findViewById(R.id.systemAdminManageOrg_eTxt_searchBar);

        systemAdminManageOrg_recycleV_orgList = findViewById(R.id.systemAdminManageOrg_recycleV_orgList);

        systemAdminManageOrg_eTxt_searchBar.addTextChangedListener(new TextWatcher() {
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

        displayRecyclerView();
    }

    protected void onRestart(){
        super.onRestart();
        displayRecyclerView();
    }

    private void displayRecyclerView() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = null;

        cursor = dbHelper.getAllOrg(systemAdminManageOrg_eTxt_searchBar.getText().toString());

        int i = 0, j = 0;

        int countOrg = (! (cursor == null)) ? cursor.getCount() : 0;

        orgID = new String[countOrg];
        orgName = new String[countOrg];

        if (cursor!= null) {
            do {
                orgID[i] = cursor.getString(cursor.getColumnIndex("orgID"));
                orgName[i] = cursor.getString(cursor.getColumnIndex("orgName"));

                i++;
            } while (cursor.moveToNext());
        }

        SystemAdminManageOrgRecycleVAdapter adapter = new SystemAdminManageOrgRecycleVAdapter(this, orgID, orgName);
        systemAdminManageOrg_recycleV_orgList.setAdapter(adapter);
        systemAdminManageOrg_recycleV_orgList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}