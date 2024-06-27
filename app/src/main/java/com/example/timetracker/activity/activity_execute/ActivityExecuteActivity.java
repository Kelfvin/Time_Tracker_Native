package com.example.timetracker.activity.activity_execute;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.timetracker.R;
import com.example.timetracker.databinding.FragmentActivityExecuteBinding;
import com.example.timetracker.service.MusicService;
import com.example.timetracker.utils.FormatRunTime;

public class ActivityExecuteActivity extends AppCompatActivity implements View.OnClickListener {

    FragmentActivityExecuteBinding binding;

    private ActivityExecuteViewModel activityExecuteViewModel;
    private MusicService musicService;
    private boolean isBound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isBound = true;
            updateButtonIcon();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private final BroadcastReceiver statusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MusicService", "onReceive: ");
            if (MusicService.ACTION_STATUS.equals(intent.getAction())) {
                updateButtonIcon();

            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicService.ACTION_STATUS);
        Context context = getApplicationContext();
        ContextCompat.registerReceiver(context,statusReceiver, filter,ContextCompat.RECEIVER_EXPORTED);

        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void updateButtonIcon() {
        if (isBound) {
            boolean isPlaying = musicService.isPlaying();
            binding.playBtn.setImageDrawable(getDrawable(isPlaying ?
                    R.drawable.stop_icon :
                    R.drawable.play_icon

            ));
        }
    }

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


        binding.playBtn.setOnClickListener(this);
        updateButtonIcon();



    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.finishBtn) {
            activityExecuteViewModel.finishRecord();
            sendActionToService("STOP");
            finish();
        }

        if (v.getId() == R.id.playBtn) {
            if (isBound) {
                if (musicService.isPlaying()) {
                    sendActionToService("PAUSE");
                } else {
                    sendActionToService("PLAY");
                }
                updateButtonIcon();
            }
        }

    }

    private void sendActionToService(String pause) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(pause);
        startService(intent);
    }


}
