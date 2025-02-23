package com.example.hbv601G.ui.archived;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ArchivedViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public ArchivedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is archived fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
