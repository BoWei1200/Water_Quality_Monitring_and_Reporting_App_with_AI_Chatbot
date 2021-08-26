package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.MenuItem;
import android.view.View;

import android.os.Bundle;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ubidots.ApiClient;
import com.ubidots.DataSource;
import com.ubidots.Value;
import com.ubidots.Variable;

import com.jjoe64.graphview.GraphView;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserHome extends AppCompatActivity{
    private static int i = 0;
    private static final String POLLUTION_LEVEL = "level";
    //private TextView pollutionLevel;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String emailPreference = "NRIC";
    private final String userTypePreference = "userType";
    private final String passwordPreference = "password";
    private final String ubidotsThreadStopPreference = "ubidotsThreadStop";

    private UserWaterSensor userWaterSensor;
    //private ThreadUbidots threadUbidots;
    private ApiUbidots apiUbidots;
    private Boolean ubidotsExecuteStop = false;

    private AsyncTask ubidotsAsyncTask;

    private GraphView graphWQI;
    private UserIoTValues userIoTValues;
    private LinearLayout userHome_linearlayout_graphDetails_hide, userHome_linearlayout_others;
    private TextView userHome_txt_clickToViewMore;
    private CardView userHome_cv_graph;
    private UserIoTWQICalculation WQIcalc;
    private ImageView userHome_img_setting;
    private PopupMenu userHome_popupMenu_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        apiUbidots = new ApiUbidots();
        //threadUbidots = new ThreadUbidots();

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        try{
            userWaterSensor.start();
        }catch(Exception e){
            System.out.println(e.toString());
        }

        //pollutionLevel = findViewById(R.id.pollutionlevel);
        graphWQI = findViewById(R.id.userHome_graph_WQI);
        userHome_linearlayout_graphDetails_hide = findViewById(R.id.userHome_linearlayout_graphDetails_hide);
        userHome_linearlayout_others = findViewById(R.id.userHome_linearlayout_others);
        userHome_txt_clickToViewMore = findViewById(R.id.userHome_txt_clickToViewMore);
        userHome_cv_graph = findViewById(R.id.userHome_cv_graph);
        userHome_img_setting = findViewById(R.id.userHome_img_setting);

        WQIcalc = new UserIoTWQICalculation();

        userHome_popupMenu_setting = new PopupMenu(this, userHome_img_setting);
        userHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, userHome_popupMenu_setting.getMenu());
        userHome_popupMenu_setting.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.userSettingMenu_logout:
                        // calling method to edit values in shared prefs.
                        SharedPreferences.Editor editor = mPreferences.edit();

                        // below line will clear
                        // the data in shared prefs.
                        editor.clear();
                        //userWaterSensor.stopThread();

                        //ubidotsExecuteStop = true;
                        //threadUbidots.stopThread();
                        //threadUbidots.interrupt();

//                        ubidotsExecuteStop = true;
//                        try{
//                            apiUbidots.cancel(true);
//                        }catch(Exception e){
//
//                        }

                        ubidotsAsyncTask.cancel(true);

                        // below line will apply empty
                        // data to shared prefs.
                        editor.apply();

                        // starting mainactivity after
                        // clearing values in shared preferences.
                        Intent i = new Intent(UserHome.this, Login.class);
                        startActivity(i);
                        finish();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        try{
//            if(!ubidotsExecuteStop){
//                apiUbidots.execute();
//            }
//        }catch(Exception e){
//            //apiUbidots.cancel(true);
//            System.out.println(e.toString());
//        }


//        new ApiUbidots().execute();
//        if(!threadUbidots.isAlive())
//            threadUbidots.start();

        ubidotsAsyncTask = new ApiUbidots().execute();

        //registerReceiver(pollutionLevelReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        Toast.makeText(this,"Start!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

//        try{
//            if(!ubidotsExecuteStop){
//                apiUbidots.execute();
//            }
//        }catch(Exception e){
//            //apiUbidots.cancel(true);
//            System.out.println(e.toString());
//        }

//        if(!threadUbidots.isAlive())
//            threadUbidots.start();

        ubidotsAsyncTask = new ApiUbidots().execute();

        //registerReceiver(pollutionLevelReceiver, new IntentFilter(Intent.ACTION_ATTACH_DATA));
        Toast.makeText(this,"Restart!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this,"Resume!",Toast.LENGTH_SHORT).show();

        //threadUbidots.start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                //while(!ubidotsExecuteStop){
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//                            try{
//                                apiUbidots.execute();
//                            }catch(Exception e){
//                                apiUbidots.cancel(true);
//                                System.out.println("In thread: " + e.toString());
//                            }

                            ubidotsAsyncTask = new ApiUbidots().execute();
                        }
                    });

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                    }
                //}
            }
        }).start();
    }

//    private class ThreadUbidots extends Thread{
//        private final AtomicBoolean ubidotsExecuteStop = new AtomicBoolean(false);
//
//        @Override
//        public void run() {
//            // we add 100 new entries
//            System.out.println("Thread stop: " + ubidotsExecuteStop);
//            while(!ubidotsExecuteStop.get()){
//                System.out.println(ubidotsExecuteStop.get());
//                try{
//                    new ApiUbidots().execute();
//                }catch(Exception e){
//                    System.out.println(e.toString());
//                }
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                }
//            }
//        }
//
//        public void stopThread(){
//            ubidotsExecuteStop.set(true);
//        }
//    }

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

                intent = new Intent(this, UserGraphDetails.class);
                intent.putExtra("WQIIndices", WQIcalc);
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

    public void settings(View view) {
        userHome_popupMenu_setting.show();
    }

    public class ApiUbidots extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = "BBFF-d0da8e6ab1cdee030aa24fe674d2a051330";

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

            DataSource[] devices = apiClient.getDataSources();

            DataSource device = null;

            for (int i = 0; i < devices.length; i++){
                if(devices[i].getName().equals("Water Quality Monitoring")){
                    device = devices[i];
                    break;
                }
            }

            Variable[] variables = device.getVariables();

            Variable BOD = null;
            Variable COD = null;
            Variable DO = null;
            Variable NH3N = null;
            Variable pH = null;
            Variable SS = null;

            for (int j=0; j < variables.length; j++){
                if(variables[j].getName().equals("bod"))
                    BOD = variables[j];

                if(variables[j].getName().equals("cod"))
                    COD = variables[j];

                if(variables[j].getName().equals("do"))
                    DO = variables[j];

                if(variables[j].getName().equals("nh3n"))
                    NH3N = variables[j];

                if(variables[j].getName().equals("ph"))
                    pH = variables[j];

                if(variables[j].getName().equals("ss"))
                    SS = variables[j];
            }

            Value[] valuesDO = DO.getValues();
            Value[] valuesBOD = BOD.getValues();
            Value[] valuesCOD = COD.getValues();
            Value[] valuesNH3N = NH3N.getValues();
            Value[] valuesSS = SS.getValues();
            Value[] valuespH = pH.getValues();

            userIoTValues = new UserIoTValues(valuesDO, valuesBOD, valuesCOD, valuesNH3N, valuesSS, valuespH);

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

            double calculatedWQI = 0.0;
            for (int i = 0; i < listSize; i++) {
                // add new DataPoint object to the array for each of your list entries
                Value[] DO = userIoTValues.getValuesDO();
                WQIcalc = new UserIoTWQICalculation(
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesDO()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesBOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesCOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesNH3N()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesSS()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuespH()[y - i].getValue()))
                );
                WQIcalc.calculateWQI();
                calculatedWQI = WQIcalc.getWQI();
                dataPoints[i] = new DataPoint(i, calculatedWQI); //calculated WQI
                //dataPoints[i] = new DataPoint(i, Double.parseDouble(String.valueOf(variableValues[y - i].getValue()))); //calculated WQI
            }

            userHome_txt_currentWQI.setText(String.format("%.2f", calculatedWQI));


            System.out.println(userHome_txt_currentWQI.getText() + "  from txt");
//            if(!Double.toString(calculatedWQI).isEmpty())
//            if(userHome_txt_currentWQI.getText().equals("--.--")){
//                finish();
//                startActivity(getIntent());
//            }

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