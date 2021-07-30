package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.ui.userreport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserReportViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserReportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}