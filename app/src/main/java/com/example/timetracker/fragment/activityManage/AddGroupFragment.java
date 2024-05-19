package com.example.timetracker.fragment.activityManage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timetracker.R;

import com.example.timetracker.data.model.Group;
import com.example.timetracker.databinding.FragmentAddGroupBinding;
import com.example.timetracker.result.Result;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class AddGroupFragment extends Fragment implements View.OnClickListener , ColorPickerDialogListener {
    FragmentAddGroupBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addGroupVieModel = new ViewModelProvider(this).get(AddGroupVieModel.class);
        addGroupVieModel.init(requireActivity().getApplication());
    }

    private AddGroupVieModel addGroupVieModel;

    public AddGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_group, container, false);
        binding.setData(addGroupVieModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addGroupBtn.setOnClickListener(this);
        binding.selectColorBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addGroupBtn) {
            addGroup();
            // 按下返回
            requireActivity().onBackPressed();
            return;
        }


        if (v.getId() == R.id.selectColorBtn) {
            // 选择颜色
            // 创建一个ColorPickerDialog对象
            // 启动对话框
            ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().create();
            // 设置监听器
            colorPickerDialog.setColorPickerDialogListener(this);
            colorPickerDialog.show(getParentFragmentManager(), "colorPickerDialog");
            return;
        }

    }

    /**
     * 获取输入框的内容，并创建一个Group对象，然后插入到数据库中
     */
    private void addGroup() {
        // 要进行非空判断，根据情况使用Toast提示用户
        Result result = addGroupVieModel.addGroup();
        if (result instanceof Result.Error) {
            Toast.makeText(requireContext(), "添加分类失败", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(requireContext(), "添加分类成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onColorSelected(int dialogId, int color) {
        // 获取颜色
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        // 设置颜色
        addGroupVieModel.setGroupColor(hexColor);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}