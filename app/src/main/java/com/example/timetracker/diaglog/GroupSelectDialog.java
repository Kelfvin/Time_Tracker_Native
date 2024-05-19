package com.example.timetracker.diaglog;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timetracker.R;
import com.example.timetracker.adapter.GroupLinearAdapter;
import com.example.timetracker.databinding.GroupSelectDialogBinding;

public class GroupSelectDialog extends DialogFragment {

    private GroupSelectViewModel groupSelectViewModel;
    private GroupSelectDialogBinding binding;



    private GroupLinearAdapter.OnGroupSelectListener onGroupSelectListener;

    public void setOnGroupSelectListener(GroupLinearAdapter.OnGroupSelectListener onGroupSelectListener) {
        this.onGroupSelectListener = onGroupSelectListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupSelectViewModel = new ViewModelProvider(this).get(GroupSelectViewModel.class);
        groupSelectViewModel.init(requireActivity().getApplication());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 绑定数据
        binding = DataBindingUtil.inflate(inflater, R.layout.group_select_dialog, container, false);
        binding.setLifecycleOwner(this);

        GroupLinearAdapter groupLinearAdapter = new GroupLinearAdapter();
        groupLinearAdapter.setOnGroupSelectListener(onGroupSelectListener);

        binding.groupRecyclerView.setAdapter(groupLinearAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        binding.groupRecyclerView.setLayoutManager(layoutManager);
        groupSelectViewModel.getGroupsLiveData().observe(getViewLifecycleOwner(), groups -> {
            // 更新数据
            groupLinearAdapter.setGroups(groups);
            groupLinearAdapter.notifyDataSetChanged();
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window == null) {
            return;
        }

// 获取屏幕高度
        WindowManager windowManager = requireActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        // 设置高度为屏幕的一半
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 2);
        window.setGravity(Gravity.BOTTOM);

        // 设置背景透明
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

}
