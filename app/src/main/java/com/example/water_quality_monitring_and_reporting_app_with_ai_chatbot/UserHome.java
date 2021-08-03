package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView pollutionLevel;
    private GraphView graphWQI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        pollutionLevel = findViewById(R.id.pollutionlevel);
        graphWQI = findViewById(R.id.userHome_graph_WQI);


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
                for (int i = 0; i < 100; i++) {
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

    public void toReportMenu(View view) {
        Intent intent = new Intent(this, UserReportMenu.class);
        startActivity(intent);
        finish();
    }

    public void toHome(View view) {

    }

    public class ApiUbidots extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = "BBFF-d0da8e6ab1cdee030aa24fe674d2a051330";
        private final String VARIABLE_ID = "60f2b0be4763e74e29fcc3aa";

        @Override
        protected Value[] doInBackground(Integer... params) {
            ApiClient apiClient = new ApiClient(API_KEY);

            if(i == 0){
                DataSource newDevice = apiClient.createDataSource("Water Quality Monitoring");
                newDevice.createVariable("DO");
                newDevice.createVariable("BOD");
                newDevice.createVariable("COD");
                newDevice.createVariable("NH3N");
                newDevice.createVariable("SS");
                newDevice.createVariable("pH");

                i++;
            }
            Variable pollutionLevel = apiClient.getVariable(VARIABLE_ID);

            Value[] variableValues = pollutionLevel.getValues();

            return variableValues;
        }

        @Override
        protected void onPostExecute(Value[] variableValues) {
            pollutionLevel.setText(String.valueOf(variableValues[0].getValue()));
            // Update your views here


            System.out.println("\n\n\nrunning\n\n\n");

            int listSize = 30;
            DataPoint[] dataPoints = new DataPoint[listSize]; // declare an array of DataPoint objects with the same size as your list
            int y = listSize - 1;
            for (int i = 0; i < listSize; i++) {
                // add new DataPoint object to the array for each of your list entries
                dataPoints[i] = new DataPoint(i, Double.parseDouble(String.valueOf(variableValues[y - i].getValue()))); // not sure but I think the second argument should be of type double
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
            graphWQI.removeAllSeries();
            graphWQI.addSeries(series);

            graphWQI.getViewport().setMinX(0);
            graphWQI.getViewport().setMaxX(listSize);
            //graphWQI.getViewport().setMinY(2.0);
            //graphWQI.getViewport().setMaxY(15.0);

            graphWQI.getViewport().setYAxisBoundsManual(true);
            graphWQI.getViewport().setXAxisBoundsManual(true);
        }
    }
}