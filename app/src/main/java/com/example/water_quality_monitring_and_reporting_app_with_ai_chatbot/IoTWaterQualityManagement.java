package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import android.os.AsyncTask;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

public class IoTWaterQualityManagement {

    public class ApiUbidots extends AsyncTask<Integer, Void, Value[]> {
        private final String API_KEY = "BBFF-d0da8e6ab1cdee030aa24fe674d2a051330";
        private final String VARIABLE_ID = "60f2b0be4763e74e29fcc3aa";

        @Override
        protected Value[] doInBackground(Integer... params) {
            ApiClient apiClient = new ApiClient(API_KEY);
            Variable pollutionLevel = apiClient.getVariable(VARIABLE_ID);

            Value[] variableValues = pollutionLevel.getValues();

            return variableValues;
        }

        @Override
        protected void onPostExecute(Value[] variableValues) {
            //mBatteryLevel.setText(String.valueOf(variableValues[0].getValue()));

        }
    }
}
