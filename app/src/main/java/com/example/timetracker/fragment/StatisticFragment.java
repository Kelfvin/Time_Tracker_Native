package com.example.timetracker.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetracker.R;
import com.example.timetracker.activity.statistic.StatisticViewModel;
import com.example.timetracker.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticFragment extends Fragment {

    private StatisticViewModel mViewModel;
    FragmentStatisticBinding binding;

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupPieChart();
        loadPieChartData();
        return view;
    }

    private void setupPieChart() {
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setHoleColor(Color.WHITE);
        binding.pieChart.setTransparentCircleRadius(61f);
        binding.pieChart.setHoleRadius(58f);

        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.setEntryLabelTextSize(12);
        binding.pieChart.setEntryLabelColor(Color.BLACK);
        binding.pieChart.getDescription().setEnabled(false);

        Legend l = binding.pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.4f, "A"));
        entries.add(new PieEntry(0.3f, "B"));
        entries.add(new PieEntry(0.2f, "C"));
        entries.add(new PieEntry(0.1f, "D"));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(123, 255, 124));
        colors.add(Color.rgb(124, 123, 255));
        colors.add(Color.rgb(255, 124, 255));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        binding.pieChart.setData(data);
        binding.pieChart.invalidate(); // refresh
    }



}