package com.example.canteenapp.ui.your_orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YourOrdersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YourOrdersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}