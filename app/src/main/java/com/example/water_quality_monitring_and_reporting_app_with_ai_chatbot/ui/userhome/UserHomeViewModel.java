package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot.ui.userhome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserHomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserHomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}