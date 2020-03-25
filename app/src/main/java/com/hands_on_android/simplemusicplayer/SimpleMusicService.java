package com.hands_on_android.simplemusicplayer;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class SimpleMusicService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;

    public class SimpleMusicBinder extends Binder {
        public SimpleMusicService getService() {
            return SimpleMusicService.this;
        }
    }

    private SimpleMusicBinder binder = new SimpleMusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    //Use this function to play a new music. If another music is already playing, calling this function
    //would stop the current music and start the new one.
    public void playMusic(Music music) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, music.getSongRes());
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        NotificationUtils.getInstance().notifyForegroundNotification(music.getSongInfo());
        Notification notification = NotificationUtils.getInstance().getForegroundNotification(music.getSongInfo());
        startForeground(NotificationUtils.FOREGROUND_NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Music currentMusic = MusicListUtils.getInstance().getCurrentMusic();
        playMusic(currentMusic);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        MusicListUtils.getInstance().setNext();
        playMusic(MusicListUtils.getInstance().getCurrentMusic());
        Intent broadcastIntent = new Intent(SimplerMusicConst.SIMPLE_MUSIC_ACTION);
        sendBroadcast(broadcastIntent);
    }
}