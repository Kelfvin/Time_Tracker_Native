package com.example.timetracker.fragment.activityManage;

import static android.view.View.*;

import static com.example.timetracker.adapter.GroupLinearAdapter.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.timetracker.R;
import com.example.timetracker.adapter.GroupLinearAdapter;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.databinding.FragmentAddActivityBinding;
import com.example.timetracker.diaglog.GroupSelectDialog;
import com.example.timetracker.result.Result;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;


public class AddActivityFragment extends Fragment implements OnClickListener, OnGroupSelectListener, ColorPickerDialogListener {

    private AddActivityViewModel addActivityViewModel;
    private FragmentAddActivityBinding binding;


    public AddActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivityViewModel = new ViewModelProvider(this).get(AddActivityViewModel.class);
        addActivityViewModel.init(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_activity, container, false);
        binding.setData(addActivityViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addActivityBtn.setOnClickListener(this);
        binding.selectGroupBtn.setOnClickListener(this);
        binding.selectColorBtn.setOnClickListener(this);

    }


    private void addActivity() {
        // 调用ViewModel的插入数据库方法
        Result result = addActivityViewModel.insertActivity();
        if (result.isSuccess()) {
            Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addActivityBtn) {
            // 获取输入框的内容
            addActivity();
            // 返回上一个页面
            requireActivity().onBackPressed();
            return;
        }
        if (v.getId() == R.id.selectGroupBtn) {
            // 启动对话框
            GroupSelectDialog groupSelectDialog = new GroupSelectDialog();
            groupSelectDialog.setOnGroupSelectListener(this);
            //设置目标Fragment
            groupSelectDialog.show(getParentFragmentManager(), "groupSelectDialog");
        }

        if (v.getId() == R.id.selectColorBtn) {
            // 启动对话框
            ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().create();
            // 设置监听器
            colorPickerDialog.setColorPickerDialogListener(this);
            colorPickerDialog.show(getParentFragmentManager(), "colorPickerDialog");
        }


    }


    @Override
    public void onGroupSelect(Group group) {
        Toast.makeText(requireContext(), "选择了" + group.getName(), Toast.LENGTH_SHORT).show();
        // 设置GroupID
        addActivityViewModel.setGroupById(group.getId());
        // 关闭对话框
        FragmentManager fragmentManager = getParentFragmentManager();
        GroupSelectDialog groupSelectDialog = (GroupSelectDialog) fragmentManager.findFragmentByTag("groupSelectDialog");
        if (groupSelectDialog != null) {
            groupSelectDialog.dismiss();
        }

    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        // 设置颜色
        String colorStr = String.format("#%06X", 0xFFFFFF & color);
        addActivityViewModel.setActivityColor(colorStr);

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}