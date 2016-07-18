package com.nicosb.apps.ehcofan.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.logging.Logger;

/**
 * Created by Nico on 05.07.2016.
 */
public class CustomInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
        Logger.getLogger("CustomInstanceIdService").warning("Token: " + FirebaseInstanceId.getInstance().getToken());
    }

    private void sendRegistrationToServer(String token) {
    }


}
