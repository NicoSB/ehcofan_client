package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.PlayerWrapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nico on 23.07.2016.
 */
public class FetchPlayersTask extends AsyncTask<String, Void, ArrayList<Player>> {
    public static final String CUSTOM_PREFS = "customPrefs";
    private static final String TAG = "FetchPlayersTask";
    ArrayList<Player> players = new ArrayList<>();
    SharedPreferences prefs;
    private Context context;
    private OnPlayersFetchedListener onPlayersFetchedListener;

    public FetchPlayersTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Player> doInBackground(String... strings) {
        updatePayers();

        SQLiteDatabase db = new CacheDBHelper(context).getReadableDatabase();
        Cursor c = db.query(
                CacheDBHelper.TableColumns.PLAYERS_TABLE_NAME,  // The table to query
                null,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "position != 'Torhüter', position != 'Verteidiger', position != 'Stürmer', number ASC"                                 // The sort order
        );

        while (c.moveToNext()) {
            Player p = Player.populatePlayer(c);
            p.setPlayerImage(Cacher.getPlayerImage(context, p));
            players.add(p);
        }
        c.close();
        db.close();
        return players;
    }

    @Override
    protected void onPostExecute(ArrayList<Player> players) {
        super.onPostExecute(players);
        if (onPlayersFetchedListener != null) {
            onPlayersFetchedListener.onPlayersFetched(players);
        }
    }

    public void setOnPlayersFetchedListener(OnPlayersFetchedListener onPlayersFetchedListener) {
        this.onPlayersFetchedListener = onPlayersFetchedListener;
    }

    private void updatePayers() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                prefs = context.getSharedPreferences(CUSTOM_PREFS, Context.MODE_PRIVATE);
                String lastUpdated = prefs.getString(context.getString(R.string.pref_player_update), "" );
                String rest_url = context.getString(R.string.rest_interface) + "players";
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
                PlayerWrapper playersArray[] = gson.fromJson(json, PlayerWrapper[].class);
                String image_url = "";
                String lastUpdate = "0";

                // extract players and save them as well as pictures to local database
                for (PlayerWrapper p : playersArray) {
                    Player player = p.toPlayer(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_white_48dp));
                    if(p.isActive()) {
                        Cacher.cachePlayer(context, player);

                        if (p.getPlayer_image_file_name() != null) {
                            try {
                                if (!Cacher.hasImage(context, player)) {
                                    String awsBucket = context.getString(R.string.aws_url);
                                    String id = ("00" + p.getId());
                                    id = id.substring(id.length() - 3);
                                    image_url = String.format(Locale.GERMANY, awsBucket + "players/player_images/000/000/%s/original/%s", id, p.getPlayer_image_file_name());
                                    URL imageAddress = new URL(image_url);
                                    HttpURLConnection imageConnection = (HttpURLConnection) imageAddress.openConnection();
                                    InputStream input = imageConnection.getInputStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                                    Cacher.storePlayerImage(context, player, bitmap);

                                    if (lastUpdate.compareTo(p.getUpdated_at()) < 0) {
                                        lastUpdate = p.getUpdated_at();
                                    }
                                }
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }

                        if (!lastUpdate.equals("0")) {
                            // update player update pref
                            prefs = context.getSharedPreferences(FetchPlayersTask.CUSTOM_PREFS, Context.MODE_APPEND);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(context.getString(R.string.pref_player_update), lastUpdate);
                            editor.apply();
                        }
                    }
                    else{
                        Cacher.delete(context, CacheDBHelper.TableColumns.PLAYERS_TABLE_NAME, p.getId());
                        if(Cacher.hasImage(context, player)){
                            String fileName = "player_" + player.getId();
                            File dir = context.getFilesDir();
                            File image = new File(dir, "player_" + player.getId());
                            Log.w("delete", "successful: " + image.delete());
                        }
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public interface OnPlayersFetchedListener {
        void onPlayersFetched(ArrayList<Player> players);
    }
}
