package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ExaminerExamination extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView examinerExamination_txt_tabPending, examinerExamination_txt_tabCompleted;

    private TextView currentlyActiveTab;

    private Spinner examinerExamination_spinner_filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examiner_examination);

        Toolbar toolbar = findViewById(R.id.examinerExamination_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        examinerExamination_txt_tabPending = findViewById(R.id.examinerExamination_txt_tabPending);
        examinerExamination_txt_tabCompleted = findViewById(R.id.examinerExamination_txt_tabCompleted);

        examinerExamination_spinner_filter = findViewById(R.id.examinerExamination_spinner_filter);

        currentlyActiveTab = examinerExamination_txt_tabPending;


        if (examinerExamination_spinner_filter != null) {
            examinerExamination_spinner_filter.setOnItemSelectedListener(this);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.filter_examination, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner.
            if (examinerExamination_spinner_filter != null) {
                examinerExamination_spinner_filter.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {}

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toWhichTab(View view) {
        setCurrentlyActiveTab(view.getId());
    }

    public void setCurrentlyActiveTab(int txtID){
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.gray));
        currentlyActiveTab.setBackgroundColor(Color.WHITE);

        currentlyActiveTab = findViewById(txtID);
        currentlyActiveTab.setTextColor(getResources().getColor(R.color.tab_text_color));
        currentlyActiveTab.setBackground(getResources().getDrawable(R.color.tab_background));

        //change the recycler view
    }

    public void displayToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}