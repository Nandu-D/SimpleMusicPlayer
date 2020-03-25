package com.hands_on_android.simplemusicplayer;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtils {
    private static NotificationUtils INSTANCE;
    private static final String CHANNEL_ID = "SimpleMusicNotificationChannel";
    public static final int FOREGROUND_NOTIFICATION_ID = 2;

    private Context context;

    private NotificationUtils(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Simple Music Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }
        }
    }

    public static void initialize(Context context) {
        INSTANCE = new NotificationUtils(context);
    }

    public static NotificationUtils getInstance() {
        return INSTANCE;
    }

    public Notification getForegroundNotification(String musicName) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Simple Music Player")
                .setContentText(musicName)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        return notification;
    }

    public void notifyForegroundNotification(String musicName) {
        Notification notification = getForegroundNotification(musicName);
        NotificationManagerCompat.from(context).notify(FOREGROUND_NOTIFICATION_ID, notification);
    }
}
