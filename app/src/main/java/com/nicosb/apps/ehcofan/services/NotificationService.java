package com.nicosb.apps.ehcofan.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Nico on 05.07.2016.
 */
public class NotificationService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());

    }

    private void sendRegistrationToServer(String token) {

    }
}
