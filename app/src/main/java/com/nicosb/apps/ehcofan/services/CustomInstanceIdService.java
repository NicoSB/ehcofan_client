package com.nicosb.apps.ehcofan.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.logging.Logger;

/**
 * Created by Nico on 05.07.2016.
 */
public class CustomInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Logger.getLogger("CustomInstanceIdService").warning("Token: " + FirebaseInstanceId.getInstance().getToken());
    }
}
