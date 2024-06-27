package com.example.timetracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.timetracker.R;

public class MusicService extends Service {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";
    public static final String ACTION_STATUS = "STATUS";
    public static final String EXTRA_STATUS = "EXTRA_STATUS";
    private final IBinder binder = new MusicBinder();
    private MediaPlayer mediaPlayer;

    private void sendStatusBroadcast() {
        Log.d("MusicService", "sendStatusBroadcast: " + "Action: " + ACTION_STATUS + " Status: " + isPlaying());
        Intent intent = new Intent();
        intent.setAction(ACTION_STATUS);
        intent.putExtra(EXTRA_STATUS, isPlaying());
        sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setLooping(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case "PLAY":
                    playMusic();
                    break;
                case "PAUSE":
                    pauseMusic();
                    break;
                case "STOP":
                    stopSelf();
            }
            
            sendStatusBroadcast();
        }

        return START_STICKY;
    }

    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            showNotification("PAUSE");
        }
    }

    private void playMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            showNotification("PLAY");

        }

    }


    private void showNotification(String action) {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, "MUSIC_CHANNEL")
                .setContentTitle("背景白噪声")
                .setContentText(action.equals("PLAY") ? "正在播放" : "已经停止")
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(new NotificationCompat.Action(
                        action.equals("PLAY") ? R.drawable.play_icon : R.drawable.stop_icon,
                        action.equals("PLAY") ? "暂停" : "播放",
                        pendingIntent
                ))
                .setOngoing(true)
                .build();

        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
