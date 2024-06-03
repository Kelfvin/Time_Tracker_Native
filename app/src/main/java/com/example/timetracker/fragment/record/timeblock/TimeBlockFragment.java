package com.example.timetracker.fragment.record.timeblock;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timetracker.R;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.databinding.FragmentTimeBlockBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TimeBlockFragment extends Fragment implements ActivityButtonAdapter.OnActivityClickListener{
    private FragmentTimeBlockBinding binding;
    private TimeBlockViewModel timeBlockViewModel;
    private GroupButtonAdapter groupButtonAdapter;




    public TimeBlockFragment() {

    }



    public void setSelectDate(Calendar date){
        timeBlockViewModel.setSelectDate(date);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeBlockViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication()).create(TimeBlockViewModel.class);
        timeBlockViewModel.init(getActivity().getApplication() );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_block, container, false);
        binding.setLifecycleOwner(this);
        initTimeLine();
        initActivityButtonList();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTimeBlockGrid();


    }

    private void initTimeBlockGrid(){
        binding.timeBlockGrid.setOnTimeSelectedListener(timeBlockViewModel);

        timeBlockViewModel.getActivitiesAndRecordsLiveData().observe(getViewLifecycleOwner(), new Observer<List<ActivityAndRecords>>() {
            @Override
            public void onChanged(List<ActivityAndRecords> activityAndRecords) {
                binding.timeBlockGrid.setActivityAndRecordsList(activityAndRecords);
                binding.timeBlockGrid.invalidate();
            }
        });
    }

    private void initActivityButtonList(){
        groupButtonAdapter = new GroupButtonAdapter();
        groupButtonAdapter.setOnActivityClickListener(this);
        RecyclerView recyclerView = binding.activityButtonList;

        RecyclerView.LayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(groupButtonAdapter);

        // 获取所有活动
        timeBlockViewModel.getAllGroupsAndActivitiesLiveData().observe(getViewLifecycleOwner(), new Observer<List<GroupAndActivities>>() {
            @Override
            public void onChanged(List<GroupAndActivities> groupAndActivities) {

                for (GroupAndActivities groupAndActivities1 : groupAndActivities) {
                    Log.d("GroupAndActivities", groupAndActivities1.getGroup().getName());
                }
                groupButtonAdapter.setGroupAndActivitiesList(groupAndActivities);
                groupButtonAdapter.notifyDataSetChanged();
            }
        });



    }



    private void  initTimeLine(){
        // 添加0-23小时的时间线
        List<String> timeLine = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            timeLine.add(String.valueOf(i));
        }

        LinearLayout listView = binding.timeBlockTimeLine;
        // 创建TextView动态添加到LinearLayout中
        for (String time : timeLine) {
            TextView textView = new TextView(getContext());
            textView.setText(time);
            // 权重为1
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            listView.addView(textView);
        }
    }

    @Override
    public void onActivityClick(Activity activity) {
        timeBlockViewModel.onActivityClick(activity);
        // 清空时间
        binding.timeBlockGrid.clearSelectedCells();
    }

}