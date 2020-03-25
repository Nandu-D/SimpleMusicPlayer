package com.hands_on_android.simplemusicplayer;

import android.app.Application;

public class SimpleMusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MusicListUtils.initialize();
        NotificationUtils.initialize(this);
    }
}
