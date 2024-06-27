package com.example.timetracker.fragment.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetracker.R;
import com.example.timetracker.activity.activity_execute.ActivityExecuteActivity;
import com.example.timetracker.activity.activity_manage.ActivityManageActivity;
import com.example.timetracker.adapter.GroupAdapter;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;
import com.example.timetracker.data.model.RecordWithActivity;
import com.example.timetracker.databinding.FragmentHomeBinding;
import com.example.timetracker.fragment.TaskExecuteFragment;
import com.example.timetracker.utils.FormatRunTime;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;
import java.util.Map;

import com.example.timetracker.data.model.Record;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private HomeViewModel homeViewModel;
    private GroupAdapter groupAdapter;
    FragmentHomeBinding binding;

    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.init(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setData(homeViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addFloatingActionButtonListener();


        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        // 流式布局
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);

        binding.groupRecyclerView.setLayoutManager(layoutManager);
        //  先从左到右排列，然后换行

        // 设置RecyclerView
        groupAdapter = new GroupAdapter(getContext());
        binding.groupRecyclerView.setAdapter(groupAdapter);
        registerForContextMenu(binding.groupRecyclerView);

        // 获取数据
        homeViewModel.getAllGroupsAndActivitiesAndRecordsLiveData().observe(getViewLifecycleOwner(), new Observer<List<GroupAndActivitiesAndRecords>>() {
            @Override
            public void onChanged(List<GroupAndActivitiesAndRecords> groupAndActivitiesAndRecordsList) {
                groupAdapter.setGroupAndActivitiesRecordsList(groupAndActivitiesAndRecordsList);
                groupAdapter.notifyDataSetChanged();
            }
        });

        homeViewModel.getRunningRecordWithActivityLiveData().observe(getViewLifecycleOwner(), new Observer<RecordWithActivity>() {
            @Override
            public void onChanged(RecordWithActivity recordWithActivity) {
                if (recordWithActivity == null || recordWithActivity.getRecord() == null || recordWithActivity.getActivity() == null) {
                    binding.runningActivityLayout.setVisibility(View.GONE);
                } else {
                    binding.runningActivityLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // 获取正在运行的活动
        homeViewModel.getExecuteTime().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                String formatString = FormatRunTime.format(aLong);
                binding.recordingTime.setText(formatString);
            }
        });

        binding.finishBtn.setOnClickListener(this);
        binding.runningRecordCardView.setOnClickListener(this);
    }

    private void addFloatingActionButtonListener() {
        binding.addActivityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // 跳转到添加活动页面
                Intent intent = new Intent(getContext(), ActivityManageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finishBtn) {
            homeViewModel.finishRecord();
        }
        if (v.getId() == R.id.runningRecordCardView) {
            Intent intent = new Intent(getContext(), ActivityExecuteActivity.class);
            startActivity(intent);
        }
    }


}