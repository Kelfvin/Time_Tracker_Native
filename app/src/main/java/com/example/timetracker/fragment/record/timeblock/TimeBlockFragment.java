package com.example.timetracker;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timetracker.databinding.FragmentRecordBinding;
import com.example.timetracker.databinding.FragmentTimeBlockBinding;

import java.util.ArrayList;
import java.util.List;


public class TimeBlockFragment extends Fragment {
    private FragmentTimeBlockBinding binding;

    public TimeBlockFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_block, container, false);
        initTimeLine();
        return binding.getRoot();
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
}