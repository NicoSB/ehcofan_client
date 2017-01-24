package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.activities.HomeActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nico on 22.01.2017.
 */

public class IsPlayoffLoader extends AsyncTaskLoader<Void> {
    private Context context;

    public IsPlayoffLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Void loadInBackground() {
        boolean isPlayoff = false;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                String rest_url = context.getString(R.string.rest_interface) + "playoffs?mode=run";
                URL restAddress = new URL(rest_url);
                HttpURLConnection urlConnection = (HttpURLConnection) restAddress.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = reader.readLine();
                if (line.equals("true")) {
                    isPlayoff = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = context.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(HomeActivity.PREF_IS_PO, isPlayoff);
        editor.apply();

        return null;
    }
}
