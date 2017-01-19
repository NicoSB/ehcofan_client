package com.nicosb.apps.ehcofan.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.activities.NewsActivity;

/**
 * Created by Nico on 06.07.2016.
 */
public class NotificationService extends FirebaseMessagingService {

    private String TAG = "NotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "CUSTOM NOTIFICATION SERVICE");
        Intent showNews = new Intent(this, NewsActivity.class);
        showNews.putExtra("fromNotification", true);

        PendingIntent pi = PendingIntent.getActivity(this,
                0,
                showNews,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setSmallIcon(R.drawable.splash_image)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(1, notification);

    }
}
