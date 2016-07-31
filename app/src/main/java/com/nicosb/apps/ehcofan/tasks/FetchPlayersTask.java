package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.PlayerCacheHelper;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.PlayerWrapper;
import com.nicosb.apps.ehcofan.models.Player;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Nico on 23.07.2016.
 */
public class FetchPlayersTask extends AsyncTask<String, Void, ArrayList<Player>>{
    private static final String TAG = "FetchPlayersTask";

    private static final String CUSTOM_PREFS = "customPrefs";
    private static final String PREF_PLAYER_UPDATE = "playerUpdate";
    private Context context;
    private OnPlayersFetchedListener onPlayersFetchedListener;
    ArrayList<Player> players;
    SharedPreferences prefs;

    public FetchPlayersTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Player> doInBackground(String... strings) {
        try {
            prefs = context.getSharedPreferences(CUSTOM_PREFS, Context.MODE_PRIVATE);
            String lastUpdated = prefs.getString(PREF_PLAYER_UPDATE, "");
            String rest_url = context.getString(R.string.rest_interface) + "players";
            if(lastUpdated.length() > 0){
                rest_url = rest_url + "?updated_at=" + lastUpdated;
            }
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
            PlayerWrapper playersArray[] = gson.fromJson(json, PlayerWrapper[].class);
            players = new ArrayList<>();
            String image_url = "";

            for (PlayerWrapper p : playersArray) {
                Player player = p.toPlayer(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_white_48dp));
                Cacher.cachePlayer(context, player);
                if (p.getPlayer_image_file_name() != null) {
                    try {
                        if(!Cacher.hasImage(context, player)) {
                            String awsBucket = context.getString(R.string.aws_url);
                            String id = ("00" + p.getId());
                            id = id.substring(id.length() - 3);
                            image_url = String.format(Locale.GERMANY, awsBucket + "players/player_images/000/000/%s/original/%s", id, p.getPlayer_image_file_name());
                            URL imageAddress = new URL(image_url);
                            HttpURLConnection imageConnection = (HttpURLConnection) imageAddress.openConnection();
                            InputStream input = imageConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);
                            Cacher.storePlayerImage(context, player, bitmap);
                        }
                    } catch (FileNotFoundException fnfe) {
                        Log.w(TAG, "Error with: " + image_url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }

        prefs = context.getSharedPreferences(CUSTOM_PREFS, Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("Europe/Berlin"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
        editor.putString(PREF_PLAYER_UPDATE, sdf.format(currentTime.getTime()));
        editor.apply();

        SQLiteDatabase db = new PlayerCacheHelper(context).getReadableDatabase();
        Cursor c = db.query(
                PlayerCacheHelper.PlayerCache.TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "position != 'Torhüter', position != 'Verteidiger', position != 'Stürmer', number ASC"                                 // The sort order
        );

        while(c.moveToNext()){
            Player p = Player.populatePlayer(c);
            p.setPlayerImage(Cacher.getPlayerImage(context, p));
            players.add(p);
        }
        c.close();

        return players;
    }

    @Override
    protected void onPostExecute(ArrayList<Player> players) {
        super.onPostExecute(players);
        if(onPlayersFetchedListener != null) {
            onPlayersFetchedListener.onPlayersFetched(players);
        }
    }

    public void setOnPlayersFetchedListener(OnPlayersFetchedListener onPlayersFetchedListener) {
        this.onPlayersFetchedListener = onPlayersFetchedListener;
    }


    public interface OnPlayersFetchedListener{
        void onPlayersFetched(ArrayList<Player> players);
    }
}
