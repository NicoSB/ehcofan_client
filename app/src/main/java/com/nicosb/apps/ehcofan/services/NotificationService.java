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
import com.nicosb.apps.ehcofan.activities.HomeActivity;
import com.nicosb.apps.ehcofan.activities.NewsActivity;

/**
 * Created by Nico on 06.07.2016.
 */
public class NotificationService extends FirebaseMessagingService {
    private static final String TYPE_GOAL = "goal";
    private static final String TYPE_NEWS = "news";
    private String TAG = "NotificationService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get("type");
        Intent intent = new Intent();

        switch(type){
            case TYPE_NEWS:
                intent = new Intent(this, NewsActivity.class);
                intent.putExtra(getString(R.string.news_id), Integer.parseInt(remoteMessage.getData().get(getString(R.string.news_id))));
                break;
            case TYPE_GOAL:
                intent = new Intent(this, HomeActivity.class);
                break;
        }

        PendingIntent pi = PendingIntent.getActivity(this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"))
                .bigText(remoteMessage.getData().get("body"));

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setSmallIcon(R.drawable.splash_image)
                .setStyle(bigTextStyle)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        manager.notify(1, notification);
    }
}
