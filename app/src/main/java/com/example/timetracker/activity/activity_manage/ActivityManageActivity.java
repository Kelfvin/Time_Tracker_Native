package com.example.timetracker.activity.activity_manage;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.timetracker.R;
import com.example.timetracker.databinding.ActivityManageBinding;

public class ActivityManageActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private NavController navController;
    ActivityManageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manage);


        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.ActivityManageFragmentContainerView);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        binding.radioGroup.setOnCheckedChangeListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.activityRadioButton){
            // 切换到添加活动页面，是直接替换不是用栈
            navController.navigate(R.id.action_addGroupFragment_to_addActivityFragment, null);
            return;
        }
        if (v.getId() == R.id.groupRadioButton){
            // 跳转到添加活动组页面
            navController.navigate(R.id.action_addActivityFragment_to_addGroupFragment);
            return;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.activityRadioButton) {
            navController.navigate(R.id.addActivityFragment);
        } else {
            navController.navigate(R.id.addGroupFragment);
        }
    }
}