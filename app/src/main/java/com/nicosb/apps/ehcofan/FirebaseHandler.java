package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.tasks.FetchPlayersTask;

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
                                FirebaseMessaging.getInstance().subscribeToTopic("news2");
                                // TODO remove before push
                                FirebaseMessaging.getInstance().subscribeToTopic("news");
                            } else {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("news2");
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("news" );
                            }
                            if (pref_matches) {
                                FirebaseMessaging.getInstance().subscribeToTopic("testgoals");
                            } else {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic("testgoals");
                            }
                        }
                    }
                });
        }
}
