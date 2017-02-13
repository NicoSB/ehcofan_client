package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Nico on 28.07.2016.
 */
public class FirebaseHandler {
    private final static String TAG = "FirebaseHandler";
    // Firebase instance variables
    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    public static void signIn(final Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        final boolean pref_articles = prefs.getBoolean(activity.getString(R.string.pref_article_notifications), false);
        final boolean pref_matches = prefs.getBoolean(activity.getString(R.string.pref_matches_notifications), false);
        // Initialize Firebase Auths
        mAuth = FirebaseAuth.getInstance();

        // Setup Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (pref_articles) {
                                FirebaseMessaging.getInstance().subscribeToTopic(activity.getString(R.string.TOPIC_NEWS2));
                            } else {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(activity.getString(R.string.TOPIC_NEWS2));
                            }
                            if (pref_matches) {
                                FirebaseMessaging.getInstance().subscribeToTopic(activity.getString(R.string.TOPIC_GOALS));
                            } else {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(activity.getString(R.string.TOPIC_GOALS));
                            }
                        }
                    }
                });
        }
}
