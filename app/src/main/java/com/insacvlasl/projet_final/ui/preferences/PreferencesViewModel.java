package com.insacvlasl.projet_final.ui.preferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PreferencesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PreferencesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is preferences fragment. No thing for now, but some awesome feature will appear in next patch.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}