package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.MatchWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Nico on 20.07.2016.
 */
public class FetchMatchesTask extends AsyncTask<String, Void, ArrayList<Match>>{
    private static final String TAG = "FetchMatchesTask";
    private OnScheduleFetchedListener onScheduleFetchedListener;
    Context context;

    public FetchMatchesTask(Context context){
        this.context = context;
    }

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {
        String rest_url = context.getString(R.string.rest_interface);
        if(strings.length > 0) {
            rest_url = rest_url + "matches?competition=" + strings[0].replace(" ", "%20");
        }

        try{
            Log.d(TAG, rest_url);
            URL restAddress = new URL(rest_url);
            HttpURLConnection urlConnection = (HttpURLConnection)restAddress.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line).append('\n');
            }

            String json = builder.toString();

            Gson gson = new Gson();
            Log.w(TAG, "Converting JSON");
            MatchWrapper[] wrappers = gson.fromJson(json, MatchWrapper[].class);
            ArrayList<Match> matches = new ArrayList<>();
            for(MatchWrapper w: wrappers){
                matches.add(w.toMatch());
            }
            return matches;
        }catch (IOException mue){
            mue.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Match> matches) {
        super.onPostExecute(matches);
        if(onScheduleFetchedListener != null){
            onScheduleFetchedListener.onScheduleFetched(matches);
        }
        else{
            Log.w(TAG, "No listener assigned!");
        }
    }

    public void setOnScheduleFetchedListener(OnScheduleFetchedListener onScheduleFetchedListener) {
        this.onScheduleFetchedListener = onScheduleFetchedListener;
    }

    public interface OnScheduleFetchedListener{
        void onScheduleFetched(ArrayList<Match> matches);
    }
}
