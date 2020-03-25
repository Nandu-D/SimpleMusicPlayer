package com.hands_on_android.simplemusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SimpleMusicBroadcastReceiver extends BroadcastReceiver {

    private MainActivity.SimpleMusicReceiverEvent event;

    public SimpleMusicBroadcastReceiver(MainActivity.SimpleMusicReceiverEvent event) {
        this.event = event;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (event != null) {
            event.updateView();
        }
    }
}
