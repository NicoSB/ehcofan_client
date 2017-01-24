package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.POMatchupWrapper;

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

public class PlayoffMatchupLoader extends AsyncTaskLoader<POMatchupWrapper> {
    private Context context;

    public PlayoffMatchupLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public POMatchupWrapper loadInBackground() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        try {
            if (networkInfo != null && networkInfo.isConnected()) {
                String rest_url = context.getString(R.string.rest_interface) + "playoffs.json?running=true";
                URL restAddress = new URL(rest_url);
                HttpURLConnection urlConnection = (HttpURLConnection) restAddress.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }

                String json = builder.toString();
                Gson gson = new Gson();

                return gson.fromJson(json, POMatchupWrapper[].class)[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
