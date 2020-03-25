package com.hands_on_android.simplemusicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MusicTitleView.MusicSelectedListener {
    public interface SimpleMusicReceiverEvent {
        void updateView();
    }


    private LinearLayout songsContainer;
    private ImageView playPauseButton;
    private TextView currentlyPlayingTextView;

    private ArrayList<MusicTitleView> titleViews = new ArrayList<>();

    private SimpleMusicBroadcastReceiver broadcastReceiver;
    private SimpleMusicService simpleMusicService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SimpleMusicService.SimpleMusicBinder binder = (SimpleMusicService.SimpleMusicBinder) service;
            simpleMusicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songsContainer = findViewById(R.id.songs_container);
        currentlyPlayingTextView = findViewById(R.id.currently_playing);
        ImageView prevButton = findViewById(R.id.prev_button);
        ImageView nextButton = findViewById(R.id.next_button);
        playPauseButton = findViewById(R.id.play_pause_button);

        prevButton.setOnClickListener(this::onPrevButtonClick);
        nextButton.setOnClickListener(this::onNextButtonClick);
        playPauseButton.setOnClickListener(this::onPlayPauseButtonClick);

        initializeMusics();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMusicViews();
        broadcastReceiver = new SimpleMusicBroadcastReceiver(new SimpleMusicReceiverEvent() {
            @Override
            public void updateView() {
                updateMusicViews();
            }
        });
        IntentFilter filter = new IntentFilter(SimplerMusicConst.SIMPLE_MUSIC_ACTION);
        registerReceiver(broadcastReceiver, filter);

        Intent serviceIntent = new Intent(this, SimpleMusicService.class);
        bindService(serviceIntent, serviceConnection, 0);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (!ServiceHelper.isServiceRunning(this, SimpleMusicService.class.getName())) {
            MusicListUtils.getInstance().setCurrentMusic(MusicListUtils.getInstance().getMusicList().get(0));
        }
    }

    //This function is completed.
    //Initialize the TitleViews when the app starts
    private void initializeMusics() {
        for (Music music : MusicListUtils.getInstance().getMusicList()) {
            MusicTitleView musicTitleView = new MusicTitleView(this);
            musicTitleView.setMusic(music);
            musicTitleView.setOnMusicSelectedListener(this);
            songsContainer.addView(musicTitleView);
            titleViews.add(musicTitleView);
        }

        updateMusicViews();
    }

    //This function is completed.
    //Updates the currentlyPlaying & selected view based on current music from the MusicListUtils
    private void updateMusicViews() {
        Music currentMusic = MusicListUtils.getInstance().getCurrentMusic();
        for (MusicTitleView musicTitleView : titleViews) {
            musicTitleView.setSelected(musicTitleView.getMusic().equals(currentMusic));
        }
        currentlyPlayingTextView.setText(currentMusic.getSongInfo());
    }

    private void onPrevButtonClick(View v) {
        MusicListUtils.getInstance().setPrevious();
        if (ServiceHelper.isServiceRunning(this, SimpleMusicService.class.getName())) {
            simpleMusicService.playMusic(MusicListUtils.getInstance().getCurrentMusic());
        }
        updateMusicViews();
    }

    private void onNextButtonClick(View v) {
        MusicListUtils.getInstance().setNext();
        if (ServiceHelper.isServiceRunning(this, SimpleMusicService.class.getName())) {
            simpleMusicService.playMusic(MusicListUtils.getInstance().getCurrentMusic());
        }
        updateMusicViews();
    }

    private void onPlayPauseButtonClick(View v) {
        Intent intent = new Intent(this, SimpleMusicService.class);
        if (ServiceHelper.isServiceRunning(this, SimpleMusicService.class.getName())) {
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            stopService(intent);
            unregisterReceiver(broadcastReceiver);
            unbindService(serviceConnection);
        } else {
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            startService(intent);
            IntentFilter filter = new IntentFilter(SimplerMusicConst.SIMPLE_MUSIC_ACTION);
            registerReceiver(broadcastReceiver, filter);

            Intent serviceIntent = new Intent(this, SimpleMusicService.class);
            bindService(serviceIntent, serviceConnection, 0);
            ContextCompat.startForegroundService(this, intent);
        }
    }

    @Override
    public void onMusicSelected(Music music) {
        MusicListUtils.getInstance().setCurrentMusic(music);
        if (ServiceHelper.isServiceRunning(this, SimpleMusicService.class.getName())) {
            simpleMusicService.playMusic(music);
        }
        updateMusicViews();
    }
}

