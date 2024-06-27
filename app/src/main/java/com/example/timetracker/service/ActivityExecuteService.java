package com.example.timetracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class ActivityExecuteService extends Service {
    private long startTime;
    private final IBinder binder = new LocalBinder();
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // 每秒更新一次时间
            handler.postDelayed(this, 1000);
        }
    };

    public class LocalBinder extends Binder {
        public ActivityExecuteService getService() {
            return ActivityExecuteService.this;
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        startTime = System.currentTimeMillis();
        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY; // 服务被杀死后自动重启
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    // 结束活动，并记录时间
    public void endActivity() {
        // TODO: 保存时间
        long elapsedTime = getElapsedTime();
        Log.d("ActivityExecuteService", "Activity ended, elapsed time: " + elapsedTime);
        stopSelf();
    }



}