package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserGraphDetails extends AppCompatActivity {
    private UserIoTWQICalculation WQIcalc;

    private TextView graphDetails_txt_WQI, graphDetails_txt_WQIper100, graphDetails_txt_WQIStatus,
            graphDetails_txt_DO, graphDetails_txt_BODval, graphDetails_txt_CODval, graphDetails_txt_NH3Nval, graphDetails_txt_SSval, graphDetails_txt_pHval;

    private CardView graphDetails_cv_graph;

    private LinearLayout graphDetails_linearLayout_WQIDesc, graphDetails_linearlayout_others;

    private Button graphDetails_btn_aboutMyWQI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_graph_details);

        Toolbar toolbar = findViewById(R.id.graphDetails_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        graphDetails_txt_WQI = findViewById(R.id.graphDetails_txt_WQI);
        graphDetails_txt_WQIper100 = findViewById(R.id.graphDetails_txt_WQIper100);
        graphDetails_txt_WQIStatus = findViewById(R.id.graphDetails_txt_WQIStatus);

        graphDetails_txt_DO = findViewById(R.id.graphDetails_txt_DOval);
        graphDetails_txt_BODval = findViewById(R.id.graphDetails_txt_BODval);
        graphDetails_txt_CODval = findViewById(R.id.graphDetails_txt_CODval);
        graphDetails_txt_NH3Nval = findViewById(R.id.graphDetails_txt_NH3Nval);
        graphDetails_txt_SSval = findViewById(R.id.graphDetails_txt_SSval);
        graphDetails_txt_pHval = findViewById(R.id.graphDetails_txt_pHval);

        graphDetails_cv_graph = findViewById(R.id.graphDetails_cv_graph);

        graphDetails_linearLayout_WQIDesc = findViewById(R.id.graphDetails_linearLayout_WQIDesc);
        graphDetails_linearlayout_others = findViewById(R.id.graphDetails_linearlayout_others);

        graphDetails_btn_aboutMyWQI = findViewById(R.id.graphDetails_btn_aboutMyWQI);

        WQIcalc = (UserIoTWQICalculation) getIntent().getSerializableExtra("WQIIndices");
        graphDetails_txt_WQI.setText(String.format("%.2f", WQIcalc.getWQI()));

        graphDetails_txt_DO.setText(String.format("%.2f", WQIcalc.getDO()));
        graphDetails_txt_BODval.setText(String.format("%.2f", WQIcalc.getBOD()));
        graphDetails_txt_CODval.setText(String.format("%.2f", WQIcalc.getCOD()));
        graphDetails_txt_NH3Nval.setText(String.format("%.2f", WQIcalc.getNH3N()));
        graphDetails_txt_SSval.setText(String.format("%.2f", WQIcalc.getSS()));
        graphDetails_txt_pHval.setText(String.format("%.2f", WQIcalc.getpH()));

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
        graphDetails_btn_aboutMyWQI.setBackgroundColor(color);
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

    public void toOtherPages(View view) {
        Intent intent = new Intent(this, UserWQIDetails.class);
        switch(view.getId()){
            case R.id.graphDetails_btn_aboutMyWQI:
                intent.putExtra("WQIInfoIndicator", "WQI");
                break;

            case R.id.graphDetails_cv_DO:
                intent.putExtra("WQIInfoIndicator", "DO");
                break;

            case R.id.graphDetails_cv_BOD:
                intent.putExtra("WQIInfoIndicator", "BOD");
                break;

            case R.id.graphDetails_cv_COD:
                intent.putExtra("WQIInfoIndicator", "COD");
                break;

            case R.id.graphDetails_cv_NH3N:
                intent.putExtra("WQIInfoIndicator", "NH3N");
                break;

            case R.id.graphDetails_cv_SS:
                intent.putExtra("WQIInfoIndicator", "SS");
                break;

            case R.id.graphDetails_cv_pH:
                intent.putExtra("WQIInfoIndicator", "pH");
                break;
        }
        startActivity(intent);
    }
}