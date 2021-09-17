package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserHome extends AppCompatActivity{

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.fyp_hydroMyapp"; //any name
    private final String userIDPreference = "userID";

    private final String emailPreference = "email";
    private final String userTypePreference = "userType";
    private final String passwordPreference = "password";
    private final String apiPreference = "api";
    private final String scannedDeviceExistPreference = "scannedDeviceExist";
    private final String stopSensorPreference = "stopSensor";
    private final String currentRunningSensorPreference = "currentRunningSensor";

    private UserWaterSensor userWaterSensor;

    private ApiUbidots apiUbidots;

    private AsyncTask ubidotsAsyncTask;

    private GraphView graphWQI;
    private UserIoTValues userIoTValues;
    private LinearLayout userHome_linearlayout_graphDetails_hide, userHome_linearlayout_others, userHome_linearlayout_callToSetup;
    private TextView userHome_txt_clickToViewMore, userHome_txt_callToSetup;
    private CardView userHome_cv_graph;
    private UserIoTWQICalculation WQIcalc;
    private ImageView userHome_img_setting;
    private PopupMenu userHome_popupMenu_setting;

    private String getAPIPreference = "";
    private String getScannedDeviceExistPreference = "";
    private String getStopSensorPreference = "";
    private String getCurrentRunningSensorPreference = "";

    DateTimeFormatter currentTime = DateTimeFormatter.ofPattern("HH:mm");
    LocalDateTime nowTime = LocalDateTime.now();
    String currentTimeString = currentTime.format(nowTime);
    private LocalTime timePreviousNotification = LocalTime.parse(currentTimeString);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        apiUbidots = new ApiUbidots();

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String currentUserID = mPreferences.getString(userIDPreference, null);

        getAPIPreference = mPreferences.getString(apiPreference, "");
        getScannedDeviceExistPreference = mPreferences.getString(scannedDeviceExistPreference, "");
        getStopSensorPreference = mPreferences.getString(stopSensorPreference, "");
        getCurrentRunningSensorPreference = mPreferences.getString(currentRunningSensorPreference, "");

        graphWQI = findViewById(R.id.userHome_graph_WQI);
        userHome_linearlayout_graphDetails_hide = findViewById(R.id.userHome_linearlayout_graphDetails_hide);
        userHome_linearlayout_others = findViewById(R.id.userHome_linearlayout_others);
        userHome_linearlayout_callToSetup = findViewById(R.id.userHome_linearlayout_callToSetup);

        userHome_txt_clickToViewMore = findViewById(R.id.userHome_txt_clickToViewMore);
        userHome_txt_callToSetup = findViewById(R.id.userHome_txt_callToSetup);

        userHome_cv_graph = findViewById(R.id.userHome_cv_graph);
        userHome_img_setting = findViewById(R.id.userHome_img_setting);

        WQIcalc = new UserIoTWQICalculation();

        userHome_popupMenu_setting = new PopupMenu(this, userHome_img_setting);
        userHome_popupMenu_setting.getMenuInflater().inflate(R.menu.user_setting_menu, userHome_popupMenu_setting.getMenu());
        userHome_popupMenu_setting.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.userSettingMenu_logout:
                        SharedPreferences.Editor editor = mPreferences.edit();

                        editor.putString(stopSensorPreference, "1");
                        editor.clear();
                        editor.apply();

                        runApiUbidots(false);

                        Intent i = new Intent(UserHome.this, Login.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return true;
            }
        });

        if(getScannedDeviceExistPreference.equals("1")){
            try{
                userWaterSensor = new UserWaterSensor(this, MODE_PRIVATE, getAPIPreference);
                Thread userWaterSensorThread = new Thread(userWaterSensor);
                if(!getCurrentRunningSensorPreference.equals("1")){ // if current running thread is not stopped (stop == false), cant start()
                    userWaterSensorThread.start();

                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString(currentRunningSensorPreference, "1");
                    editor.commit();
                }
            }catch(Exception e){
                System.out.println(e.toString());
                System.out.println("cannot run thread!");
            }
        }

        if(getAPIPreference.equals("")){
            userHome_txt_clickToViewMore.setVisibility(View.GONE);
            userHome_linearlayout_callToSetup.setVisibility(View.VISIBLE);
            graphWQI.setVisibility(View.GONE);
        }
        else if(getScannedDeviceExistPreference.equals("")){
            userHome_txt_clickToViewMore.setVisibility(View.GONE);
            userHome_txt_callToSetup.setText("Oops! You haven't set up your water sensor");
            userHome_linearlayout_callToSetup.setVisibility(View.VISIBLE);
            graphWQI.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        runApiUbidots(true);

        Toast.makeText(this,"Start!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart(){
        super.onRestart();

        runApiUbidots(true);

        Toast.makeText(this,"Restart!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this,"Resume!",Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {

            @Override
            public void run() {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("RunApiUbidots");
                            runApiUbidots(true);
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        System.out.println("App completely terminated");
    }

    public void runApiUbidots(Boolean run){
        if(!getAPIPreference.equals(null) && !getScannedDeviceExistPreference.equals(null)){
            if(!getAPIPreference.equals("") && !getScannedDeviceExistPreference.equals("")){
                if(run){
                    ubidotsAsyncTask = new ApiUbidots().execute();
                }else{
                    ubidotsAsyncTask.cancel(true);
                }
            }
        }
    }
    
    public void createNotification(int drawableIcon, String title, String content){
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "hydroMyNotification")
                .setSmallIcon(drawableIcon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(UserHome.this);
        managerCompat.notify(1, builder.build());

        Intent intent = new Intent(this, UserHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("hydroMyNotification", "hydroMyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
                intent = new Intent(this, UserGraphDetails.class);
                intent.putExtra("WQIIndices", WQIcalc);
                break;

            case R.id.userHome_btn_setUpNow:
                if(getAPIPreference.equals("")){
                    intent = new Intent(this, UserUbidotsAccSetup.class);
                }else{
                    intent = new Intent(this, UserUbidotsScanDevice.class);
                }
                break;
        }

        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void viewGraphDetails(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(!getAPIPreference.equals("")){
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
        }
    }

    public void settings(View view) {
        userHome_popupMenu_setting.show();
    }

    public void refresh(View view) {
        startActivity(getIntent());
        finish();
    }

    public class ApiUbidots extends AsyncTask<Integer, Void, Value[]> {
        private String API_KEY = ""; //BBFF-d0da8e6ab1cdee030aa24fe674d2a051330

        TextView pollutionLevel;
        TextView userHome_txt_currentWQI;
        @Override
        protected void onPreExecute(){
            pollutionLevel = findViewById(R.id.pollutionlevel);
            userHome_txt_currentWQI = findViewById(R.id.userHome_txt_currentWQI);

            String getuserIDPreference = mPreferences.getString(userIDPreference, null);

            DatabaseHelper dbHelper = new DatabaseHelper(UserHome.this);

            API_KEY = dbHelper.getAPIKey(getuserIDPreference);

            System.out.println("Running onPreExecute ...");

            //if network disable
        }

        @Override
        protected Value[] doInBackground(Integer... params) {
            System.out.println("Running doInBackGround ...");

            Value[] valuesDO = new Value[0];
            ApiClient apiClient = new ApiClient(API_KEY);

            try{
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

                valuesDO = DO.getValues();
                Value[] valuesBOD = BOD.getValues();
                Value[] valuesCOD = COD.getValues();
                Value[] valuesNH3N = NH3N.getValues();
                Value[] valuesSS = SS.getValues();
                Value[] valuespH = pH.getValues();

                userIoTValues = new UserIoTValues(valuesDO, valuesBOD, valuesCOD, valuesNH3N, valuesSS, valuespH);
            }catch(Exception e){

            }

            return valuesDO;
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Value[] variableValues) {
            super.onPostExecute(variableValues);

            int listSize = 30;
            DataPoint[] dataPoints = new DataPoint[listSize];
            int y = listSize - 1;

            double calculatedWQI = 0.0;
            for (int i = 0; i < listSize; i++) {
                // add new DataPoint object to the array for each of your list entries
                try{
                    WQIcalc = new UserIoTWQICalculation(
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesDO()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesBOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesCOD()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesNH3N()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuesSS()[y - i].getValue())),
                        Double.parseDouble(String.valueOf(userIoTValues.getValuespH()[y - i].getValue()))
                    );
                }catch(Exception e){
                    System.out.println("ERROR IN CALCULATING");
                }

                WQIcalc.calculateWQI();
                calculatedWQI = WQIcalc.getWQI();

                String status = "";

                if(calculatedWQI < 31.0){
                    status = "heavily polluted";
                }else{
                    status = "polluted";
                }

                if(!status.equals("")){
                    DateTimeFormatter currentTime = DateTimeFormatter.ofPattern("HH:mm");
                    LocalDateTime nowTime = LocalDateTime.now();
                    String currentTimeString = currentTime.format(nowTime);

                    LocalTime currentTimeParse = LocalTime.parse(currentTimeString);

                    //System.out.println(currentTimeParse + " " + timePreviousNotification);
                    int diffTime = currentTimeParse.compareTo(timePreviousNotification);
                    //System.out.println("difference" + diffTime);

                    //if(diffTime > 30){
                        timePreviousNotification = currentTimeParse;
                        int drawable = (status.equals("heavily polluted")) ? R.drawable.warningiconedit : R.drawable.warningiconedit;
                        String notificationTitle = "Water from your faucet is " + status + "!";
                        String notificationText = "Detected WQI: " + calculatedWQI;
                        createNotification(drawable, notificationTitle, notificationText);
                        //System.out.println("Notification created");

                    //}
                }

                dataPoints[i] = new DataPoint(i, calculatedWQI); //calculated WQI
            }

            userHome_txt_currentWQI.setText(String.format("%.2f", calculatedWQI));

            System.out.println(userHome_txt_currentWQI.getText() + "  from txt");

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

            Value[] DO = userIoTValues.getValuesDO();

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
        }
    }
}