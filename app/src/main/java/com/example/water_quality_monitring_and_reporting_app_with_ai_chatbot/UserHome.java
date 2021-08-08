package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

import com.jjoe64.graphview.GraphView;

public class UserHome extends AppCompatActivity {
    private static int i = 0;
    private static final String POLLUTION_LEVEL = "level";
    //private TextView pollutionLevel;
    private GraphView graphWQI;
    private IoTValues ioTValues;
    private LinearLayout userHome_linearlayout_graphDetails_hide, userHome_linearlayout_others;
    private TextView userHome_txt_clickToViewMore;
    private CardView userHome_cv_graph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //pollutionLevel = findViewById(R.id.pollutionlevel);
        graphWQI = findViewById(R.id.userHome_graph_WQI);
        userHome_linearlayout_graphDetails_hide = findViewById(R.id.userHome_linearlayout_graphDetails_hide);
        userHome_linearlayout_others = findViewById(R.id.userHome_linearlayout_others);
        userHome_txt_clickToViewMore = findViewById(R.id.userHome_txt_clickToViewMore);
        userHome_cv_graph = findViewById(R.id.userHome_cv_graph);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ApiUbidots().execute();
        //registerReceiver(pollutionLevelReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        Toast.makeText(this,"Start!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        new ApiUbidots().execute();
        //registerReceiver(pollutionLevelReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        Toast.makeText(this,"Restart!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        Toast.makeText(this,"Resume!",Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 10; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try{
                                new ApiUbidots().execute();
                            }catch(Exception e){
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    public void toOtherPages(View view) {
        Intent intent = new Intent();

        switch(view.getId()){
            case R.id.userHome_btn_bottomMenuReport :
            case R.id.userHome_btn_report:
                intent = new Intent(this, UserReportMenu.class);
                break;

            case R.id.userHome_btn_bottomMenuAIChat:
                intent = new Intent(this, UserAIChatting.class);
                break;

            case R.id.userHome_btn_WQIDetails:
                intent = new Intent(this, GraphDetails.class);
                break;
        }

        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void viewGraphDetails(View view) {
        TransitionManager.beginDelayedTransition(userHome_linearlayout_others, new AutoTransition());
        if(userHome_linearlayout_graphDetails_hide.getVisibility() == View.GONE){
            userHome_linearlayout_graphDetails_hide.setVisibility(View.VISIBLE);
            userHome_txt_clickToViewMore.setText(R.string.hide);
        }
        else{
            userHome_linearlayout_graphDetails_hide.setVisibility(View.GONE);
            userHome_txt_clickToViewMore.setText(R.string.view_more);
        }
        TransitionManager.beginDelayedTransition(userHome_cv_graph, new AutoTransition());
    }

    public class ApiUbidots extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = "BBFF-d0da8e6ab1cdee030aa24fe674d2a051330";

        private final String VARIABLE_ID_DO = "6108d1e334efb5000a66154f";
        private final String VARIABLE_ID_BOD = "6108d1e234efb5000b53ece6";
        private final String VARIABLE_ID_COD = "6108d1e3636012000c64ab6a";
        private final String VARIABLE_ID_NH3N = "6108d1e334efb5000b53ece7";
        private final String VARIABLE_ID_SS = "6108d1e3636012000db3fb3a";
        private final String VARIABLE_ID_pH = "6108d1e4636012000db3fb3b";

        TextView pollutionLevel;
        TextView userHome_txt_currentWQI;
        @Override
        protected void onPreExecute(){
            pollutionLevel = findViewById(R.id.pollutionlevel);
            userHome_txt_currentWQI = findViewById(R.id.userHome_txt_currentWQI);
        }

        @Override
        protected Value[] doInBackground(Integer... params) {

            ApiClient apiClient = new ApiClient(API_KEY);

//            if(i == 0){
//                DataSource newDevice = apiClient.createDataSource("Water Quality Monitoring");
//                newDevice.createVariable("DO");
//                newDevice.createVariable("BOD");
//                newDevice.createVariable("COD");
//                newDevice.createVariable("NH3N");
//                newDevice.createVariable("SS");
//                newDevice.createVariable("pH");
//
//                i++;
//            }

            Variable DO = apiClient.getVariable(VARIABLE_ID_DO);
            Variable BOD = apiClient.getVariable(VARIABLE_ID_BOD);
            Variable COD = apiClient.getVariable(VARIABLE_ID_COD);
            Variable NH3N = apiClient.getVariable(VARIABLE_ID_NH3N);
            Variable SS = apiClient.getVariable(VARIABLE_ID_SS);
            Variable pH = apiClient.getVariable(VARIABLE_ID_pH);

            Value[] valuesDO = DO.getValues();
            Value[] valuesBOD = BOD.getValues();
            Value[] valuesCOD = COD.getValues();
            Value[] valuesNH3N = NH3N.getValues();
            Value[] valuesSS = SS.getValues();
            Value[] valuespH = pH.getValues();

            ioTValues = new IoTValues(valuesDO, valuesBOD, valuesCOD, valuesNH3N, valuesSS, valuespH);

            return valuesDO;
        }


        @Override
        protected void onPostExecute(Value[] variableValues) {
            super.onPostExecute(variableValues);

            try{
                System.out.println(String.valueOf(variableValues[0].getValue()));
            }catch(Exception e){
                System.out.println(e.toString());
            }

            // Update your views here

            int listSize = 11;
            DataPoint[] dataPoints = new DataPoint[listSize]; // declare an array of DataPoint objects with the same size as your list
            int y = listSize - 1;

            IoTWQICalculation WQIcalc;
            Double calculatedWQI = 0.0;
            for (int i = 0; i < listSize; i++) {
                // add new DataPoint object to the array for each of your list entries
                Value[] DO = ioTValues.getValuesDO();
                WQIcalc = new IoTWQICalculation(
                        Double.parseDouble(String.valueOf(ioTValues.getValuesDO()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(ioTValues.getValuesBOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(ioTValues.getValuesCOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(ioTValues.getValuesNH3N()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(ioTValues.getValuesSS()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(ioTValues.getValuespH()[y - i].getValue()))
                );
                WQIcalc.calculateWQI();
                calculatedWQI = WQIcalc.getWQI();
                dataPoints[i] = new DataPoint(i, calculatedWQI); //calculated WQI
                //dataPoints[i] = new DataPoint(i, Double.parseDouble(String.valueOf(variableValues[y - i].getValue()))); //calculated WQI
            }

            userHome_txt_currentWQI.setText(String.format("%.2f", calculatedWQI));

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            graphWQI.removeAllSeries();
            graphWQI.addSeries(series);

            graphWQI.getViewport().setMinX(0);
            graphWQI.getViewport().setMaxX(listSize - 1);
            graphWQI.getViewport().setMinY(0.0);
            graphWQI.getViewport().setMaxY(100.0);

            graphWQI.getViewport().setYAxisBoundsManual(true);
            graphWQI.getViewport().setXAxisBoundsManual(true);

            graphWQI.getViewport().setScrollable(true); // enables horizontal scrolling
            graphWQI.getViewport().setScalable(true); // enables horizontal zooming and scrolling

//            GridLabelRenderer gridLabel = graphWQI.getGridLabelRenderer();
//            gridLabel.setHorizontalAxisTitle("WQI");
//            gridLabel.setVerticalAxisTitle("WQI");
        }
    }
}