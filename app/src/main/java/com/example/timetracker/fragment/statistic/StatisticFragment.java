package com.example.timetracker.fragment.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timetracker.R;
import com.example.timetracker.databinding.FragmentStatisticBinding;
import com.example.timetracker.utils.DateTimeUtils;
import com.example.timetracker.utils.FormatRunTime;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticFragment extends Fragment {

    FragmentStatisticBinding binding;
    private StatisticViewModel mViewModel;

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(StatisticViewModel.class);
        mViewModel.init(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupPieChart();
        loadPieChartData();
        setupHeader();
        setupStatisticsList();
        setupNoDataInfo();
        return view;
    }


    private void setupNoDataInfo() {
        mViewModel.getActivitiesAndRecords().observe(getViewLifecycleOwner(), activityAndRecords -> {
            if (activityAndRecords.size() == 0) {
                binding.pieChartCardView.setVisibility(View.GONE);
                binding.statisticListCardView.setVisibility(View.GONE);
                binding.noRecordTextView.setVisibility(View.VISIBLE);

            } else {
                binding.pieChartCardView.setVisibility(View.VISIBLE);
                binding.statisticListCardView.setVisibility(View.VISIBLE);
                binding.noRecordTextView.setVisibility(View.GONE);
            }
        });
    }

    private void setupStatisticsList() {
        // 初始化列表
        binding.statisticRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StatisticListItemAdapter adapter = new StatisticListItemAdapter();
        binding.statisticRecyclerView.setAdapter(adapter);
        mViewModel.getActivitiesAndRecords().observe(getViewLifecycleOwner(), adapter::setActivityAndRecordsList);
    }

    private void updateDateText(){
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(mViewModel.getStartTime());
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(mViewModel.getEndTime());
        binding.selectDateTextView.setText(
        DateTimeUtils.format(start) + " - " + DateTimeUtils.format(end)
        );
    }

    private void setupHeader() {
        // 初始化前进后退按钮
        binding.nextRangeImageButton.setOnClickListener(v -> {
            mViewModel.next();
            // 更新时间
            updateDateText();
        });

        binding.previousRangeImageButton.setOnClickListener(v -> {
            mViewModel.previous();
            // 更新时间
            updateDateText();
        });

        // 初始化时间段选择按钮
        binding.rangeModeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.dayRadioButton) {
                mViewModel.setMODE(StatisticViewModel.MODE_DAY);
            } else if (checkedId == R.id.weekRadioButton) {
                mViewModel.setMODE(StatisticViewModel.MODE_WEEK);
            } else if (checkedId == R.id.monthRadioButton) {
                mViewModel.setMODE(StatisticViewModel.MODE_MONTH);
            } else if (checkedId == R.id.yearRadioButton) {
                mViewModel.setMODE(StatisticViewModel.MODE_YEAR);
            }

            // 更新时间
            updateDateText();
        });


    }

    private void setupPieChart() {
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleRadius(61f);
        binding.pieChart.setHoleRadius(58f);

        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.setEntryLabelTextSize(12);
        binding.pieChart.setEntryLabelColor(Color.WHITE);
        binding.pieChart.getDescription().setEnabled(false);

        // label 显示的样式


        Legend l = binding.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {

        mViewModel.getActivitiesAndRecords().observe(
                getViewLifecycleOwner(),
                activitiesAndRecords -> {
                    List<PieEntry> entries = new ArrayList<>();
                    for (int i = 0; i < activitiesAndRecords.size(); i++) {
                        entries.add(new PieEntry(activitiesAndRecords.get(i).getTotalTimeCost(), activitiesAndRecords.get(i).getActivity().getName()));
                    }

                    ArrayList<Integer> colors = new ArrayList<>();
                    for (int i = 0; i < activitiesAndRecords.size(); i++) {
                        colors.add(
                                Color.parseColor(activitiesAndRecords.get(i).getActivity().getColor()));
                    }


                    PieDataSet dataSet = new PieDataSet(entries, null);
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(12f);
                    data.setValueTextColor(Color.WHITE);
                    // 不显示百分比
                    data.setDrawValues(false);

                    binding.pieChart.setData(data);
                    binding.pieChart.invalidate(); // refresh

                    // 设置中心文字
                    binding.pieChart.setOnChartValueSelectedListener(
                            new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {


                                    PieEntry pe = (PieEntry) e;

                                    // 获取百分比
                                    float x = pe.getValue();
                                    float total = 0;
                                    for (int i = 0; i < entries.size(); i++) {
                                        total += entries.get(i).getValue();
                                    }
                                    float percent = x / total * 100;

                                    String text = "";
                                    text += pe.getLabel() + "\n";
                                    text += FormatRunTime.format((int) pe.getValue()) + "\n";
                                    text += String.format(Locale.getDefault(), "%.2f", percent) + "%";
                                    binding.pieChart.setCenterText(text);
                                }

                                @Override
                                public void onNothingSelected() {

                                }
                            }
                    );
                }
        );
    }


}