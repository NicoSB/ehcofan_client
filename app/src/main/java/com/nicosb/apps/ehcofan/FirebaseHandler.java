package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.fragments.SettingsFragment;

/**
 * Created by Nico on 28.07.2016.
 */
public class FirebaseHandler {
    // Firebase instance variables
    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "FirebaseHandler";

    public static void signIn(Activity activity){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

        if(sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_NOTIFICATIONS, false)) {
            // Initialize Firebase Auths
            Log.d(TAG, "initializing firebase");
            mAuth = FirebaseAuth.getInstance();

            // Setup Listener
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    } else {
                        Log.d(TAG, "onAuthStateChanged:signed_out");
                    }
                }
            };

            mAuth.signInAnonymously()
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInAnonymously:onComplete:" + task.isSuccessful());

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInAnonymously", task.getException());

                            } else {
                                FirebaseMessaging.getInstance().subscribeToTopic("news");
                                Log.w(TAG, "subscribed to topic 'news'");
                            }
                        }
                    });
        }

    }

    public static void signOut(Activity activity) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("news");
    }
}
