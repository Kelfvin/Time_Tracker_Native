package com.example.timetracker.fragment.record;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class RecordFragementViewModel extends ViewModel {

    private MutableLiveData<Calendar> selectedDate;
    public static final int GRID_VIEW_MODE = 1;
    public static final int LIST_VIEW_MODE = 2;
    private int viewMode = GRID_VIEW_MODE ;

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

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int gridViewMode) {
        this.viewMode = gridViewMode;
    }
}
