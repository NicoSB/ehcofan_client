package com.nicosb.apps.ehcofan.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.fragments.ArticlesFragment;

import java.util.logging.Logger;

public class NewsActivity extends AppCompatActivity{
    Logger log = Logger.getLogger("NewsActivity");
    private String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ArticlesFragment af = new ArticlesFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, af).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
    }
}
