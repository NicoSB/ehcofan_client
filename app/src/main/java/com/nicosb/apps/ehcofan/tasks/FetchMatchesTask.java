package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.MatchWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * Created by Nico on 20.07.2016.
 */
public class FetchMatchesTask extends AsyncTask<String, Void, ArrayList<Match>> {
    private static final String TAG = "FetchMatchesTask";
    public static final String PREF_MATCH_UPDATE = "matchUpdate";
    Context context;
    private OnScheduleFetchedListener onScheduleFetchedListener;
    private ArrayList<Match> matches = new ArrayList<>();

    public FetchMatchesTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Match> doInBackground(String... strings) {
        updateMatches(strings);

        SQLiteDatabase db = new CacheDBHelper(context).getReadableDatabase();
        String where;
        String[] values;
        if (strings[0].length() > 0) {
            where = CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION + " = ?";
            values = new String[1];
            values[0] = strings[0];
        } else {
            where = CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION + " != ?";
            values = new String[1];
            values[0] = "EHCO Cup 2016";
        }
        Cursor c = db.query(
                CacheDBHelper.TableColumns.MATCHES_TABLE_NAME,  // The table to query
                null,                               // The columns to return
                where,                                // The columns for the WHERE clause
                values,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "datetime(" + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + ")"                                 // The sort order
        );

        while (c.moveToNext()) {
            try {
                Match m = Match.populateMatch(c);
                matches.add(m);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        c.close();
        db.close();

        return matches;
    }

    @Override
    protected void onPostExecute(ArrayList<Match> matches) {
        super.onPostExecute(matches);
        if (!isCancelled() && onScheduleFetchedListener != null) {
            onScheduleFetchedListener.onScheduleFetched(matches);
        }
    }

    public void setOnScheduleFetchedListener(OnScheduleFetchedListener onScheduleFetchedListener) {
        this.onScheduleFetchedListener = onScheduleFetchedListener;
    }

    private void updateMatches(String... strings) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences prefs;
            try {
                prefs = context.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_PRIVATE);
                String lastUpdated = prefs.getString(PREF_MATCH_UPDATE, "" );
                String rest_url = context.getString(R.string.rest_interface) + "matches";

                if (lastUpdated.length() > 0) {
                    rest_url = rest_url + "?updated_at=" + lastUpdated;
                }
                // Setup connection
                URL restAddress = new URL(rest_url);
                HttpURLConnection urlConnection = (HttpURLConnection) restAddress.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append('\n');
                }

                // Convert json
                String json = builder.toString();
                Gson gson = new Gson();
                MatchWrapper[] wrappers = gson.fromJson(json, MatchWrapper[].class);
                Calendar lastUpdate = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS" );

                // extract players and save them as well as pictures to local database
                for (MatchWrapper m : wrappers) {
                    Match match = m.toMatch();
                    if(m.isActive()) {
                        Cacher.cacheMatch(context, match);

                        if (lastUpdate == null || lastUpdate.before(sdf.parse(m.getUpdated_at()))) {
                            GregorianCalendar d = new GregorianCalendar();
                            d.setTime(sdf.parse(m.getUpdated_at()));
                            d.add(Calendar.SECOND, 1);
                            lastUpdate = d;
                        }
                    }
                    else{
                        Cacher.delete(context, CacheDBHelper.TableColumns.MATCHES_TABLE_NAME, m.getId());
                    }
                }

                if (lastUpdate != null) {
                    // update player update pref
                    prefs = context.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_APPEND);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(PREF_MATCH_UPDATE, sdf.format(lastUpdate.getTime()));
                    editor.apply();
                }
            } catch (IOException | ParseException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    public interface OnScheduleFetchedListener {
        void onScheduleFetched(ArrayList<Match> matches);
    }
}
