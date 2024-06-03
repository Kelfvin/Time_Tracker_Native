package com.example.timetracker.fragment.record;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class RecordFragementViewModel extends ViewModel {

    private MutableLiveData<Calendar> selectedDate;

    public RecordFragementViewModel() {
        super();
        selectedDate = new MutableLiveData<>();
        selectedDate.setValue(Calendar.getInstance());
    }

    public void setSelectedDate(Calendar selectedDate) {
        this.selectedDate.postValue(selectedDate);
    }

    public LiveData<Calendar> getSelectedDate() {
        return selectedDate;
    }
}
