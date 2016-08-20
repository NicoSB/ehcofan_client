package com.nicosb.apps.ehcofan.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Nico on 06.07.2016.
 */
public class NotificationService extends FirebaseMessagingService {

    private String TAG = "NotificationService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, ""+remoteMessage.getNotification().getTitle());
    }
}
