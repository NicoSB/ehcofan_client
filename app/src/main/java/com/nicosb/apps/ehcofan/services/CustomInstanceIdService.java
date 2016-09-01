package com.nicosb.apps.ehcofan.services;

import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by Nico on 05.07.2016.
 */
public class CustomInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
    }
}
