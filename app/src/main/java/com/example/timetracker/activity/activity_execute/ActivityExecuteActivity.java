package com.example.timetracker.activity.activity_execute;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.timetracker.R;
import com.example.timetracker.databinding.FragmentActivityExecuteBinding;
import com.example.timetracker.service.ActivityExecuteService;
import com.example.timetracker.utils.FormatRunTime;

public class ActivityExecuteActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentActivityExecuteBinding binding;

    private ActivityExecuteViewModel activityExecuteViewModel;
    private MediaPlayer mediaPlayer;

    private Thread timerThread;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_activity_execute);
        activityExecuteViewModel = new ViewModelProvider(this).get(ActivityExecuteViewModel.class);
        activityExecuteViewModel.init(getApplication());
        binding.setData(activityExecuteViewModel);
        binding.setLifecycleOwner(this);

        binding.finishBtn.setOnClickListener(this);

        activityExecuteViewModel.getExecuteTime().observe(this, aLong -> {
            binding.counterTextView.setText(FormatRunTime.format(aLong));
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);

        binding.playBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.finishBtn) {
            activityExecuteViewModel.finishRecord();
            finish();
        }

        if (v.getId() == R.id.playBtn) {
            if (mediaPlayer.isPlaying()) {
                binding.playBtn.setImageDrawable(getDrawable(R.drawable.play_icon));
                mediaPlayer.pause();
            } else {
                binding.playBtn.setImageDrawable(getDrawable(R.drawable.stop_icon));
                mediaPlayer.start();
            }
        }

    }

    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        super.onDestroy();
    }
}
