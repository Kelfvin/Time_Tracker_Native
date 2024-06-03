package com.example.timetracker.fragment.record;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetracker.R;
import com.example.timetracker.databinding.FragmentRecordBinding;
import com.example.timetracker.fragment.record.timeblock.TimeBlockFragment;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;


public class RecordFragment extends Fragment {

    private FragmentRecordBinding binding;
    private RecordFragementViewModel recordFragementViewModel;
    private TimeBlockFragment timeBlockFragment;


    public RecordFragment() {
        // Required empty public constructor
    }

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordFragementViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(RecordFragementViewModel.class);
        timeBlockFragment = new TimeBlockFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false);
        binding.setLifecycleOwner(this);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.recordFragmentContainer, timeBlockFragment);
        fragmentTransaction.commit();
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {

            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
                Log.d("CalendarView", "onCalendarOutOfRange: " + calendar.toString());
            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Log.d("CalendarView", "onCalendarSelect: " + calendar.toString() + " isClick: " + isClick);
                // 转换为Calendar格式
                java.util.Calendar calendar1 = java.util.Calendar.getInstance();
                calendar1.set(calendar.getYear(), calendar.getMonth() - 1, calendar.getDay());
                recordFragementViewModel.setSelectedDate(calendar1);
                Log.d("CalendarView", "onCalendarSelect: " + recordFragementViewModel.getSelectedDate().toString());

                timeBlockFragment.setSelectDate(calendar1);
            }
        });
    }
}