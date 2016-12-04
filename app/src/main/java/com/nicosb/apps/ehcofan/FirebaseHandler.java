package com.nicosb.apps.ehcofan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    final static String PREF_NOTIFICATIONS = "pref_notifications";
    private final static String TAG = "FirebaseHandler";
    // Firebase instance variables
    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;

    public static void signIn(final Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(PREF_NOTIFICATIONS, false)) {
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
                                FirebaseMessaging.getInstance().subscribeToTopic("testgoals" );
                            }
                        }
                    });
        }

    }

    private static void signOut() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("testgoals" );
    }

    static void sign(Activity activity, boolean signIn) {
        if (signIn) {
            signIn(activity);
        } else {
            signOut();
        }
    }
}
