package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GraphDetails extends AppCompatActivity {
    private IoTWQICalculation WQIcalc;

    private TextView graphDetails_txt_WQI, graphDetails_txt_WQIper100, graphDetails_txt_WQIStatus,
            graphDetails_txt_DO;

    private CardView graphDetails_cv_graph;

    private LinearLayout graphDetails_linearLayout_WQIDesc, graphDetails_linearlayout_others;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_details);

        Toolbar toolbar = findViewById(R.id.graphDetails_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        graphDetails_txt_WQI = findViewById(R.id.graphDetails_txt_WQI);
        graphDetails_txt_WQIper100 = findViewById(R.id.graphDetails_txt_WQIper100);
        graphDetails_txt_WQIStatus = findViewById(R.id.graphDetails_txt_WQIStatus);
        graphDetails_txt_DO = findViewById(R.id.graphDetails_txt_DOval);

        graphDetails_cv_graph = findViewById(R.id.graphDetails_cv_graph);

        graphDetails_linearLayout_WQIDesc = findViewById(R.id.graphDetails_linearLayout_WQIDesc);
        graphDetails_linearlayout_others = findViewById(R.id.graphDetails_linearlayout_others);

        WQIcalc = (IoTWQICalculation) getIntent().getSerializableExtra("WQIIndices");
        graphDetails_txt_WQI.setText(String.format("%.2f", WQIcalc.getWQI()));

        graphDetails_txt_DO.setText(String.format("%.2f", WQIcalc.getDO()));
        setWQITextColorAndStatus();
    }

    private void setWQITextColorAndStatus() {
        int color = getResources().getColor(R.color.black);
        String status = "";

        if(WQIcalc.getWQI() > 92.6){
            color = getResources().getColor(R.color.WQIBlue);
            status = getString(R.string.WQIStatusVeryClean);
        }else if(WQIcalc.getWQI() > 76.4){
            color = getResources().getColor(R.color.WQIGreen);
            status = getString(R.string.WQIStatusClean);
        }else if(WQIcalc.getWQI() > 51.8){
            color = getResources().getColor(R.color.WQIYellow);
            status = getString(R.string.WQIStatusNeutral);
        }else if(WQIcalc.getWQI() > 30.9){
            color = getResources().getColor(R.color.WQIOrange);
            status = getString(R.string.WQIStatusPolluted);
        }else{
            color = getResources().getColor(R.color.WQIRed);
            status = getString(R.string.WQIStatusHeavilyPolluted);
        }

        graphDetails_txt_WQI.setTextColor(color);
        graphDetails_txt_WQIper100.setTextColor(color);
        graphDetails_txt_WQIStatus.setText(status);
        graphDetails_txt_WQIStatus.setTextColor(color);
    }

    @Override //when back button clicked
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void viewWQIDesc(View view) {

        TransitionManager.beginDelayedTransition(graphDetails_linearlayout_others, new AutoTransition());
        if(graphDetails_linearLayout_WQIDesc.getVisibility() == View.GONE){
            graphDetails_linearLayout_WQIDesc.setVisibility(View.VISIBLE);
        }
        else{
            graphDetails_linearLayout_WQIDesc.setVisibility(View.GONE);
        }
        TransitionManager.beginDelayedTransition(graphDetails_cv_graph, new AutoTransition());
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}