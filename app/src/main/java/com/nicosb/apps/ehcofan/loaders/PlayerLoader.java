package com.nicosb.apps.ehcofan.loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.Cacher;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Player;
import com.nicosb.apps.ehcofan.models.PlayerWrapper;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 25.01.2017.
 */

public class PlayerLoader extends AsyncTaskLoader<ArrayList<Player>> implements Callback<ArrayList<PlayerWrapper>> {
    private Context context;
    private EHCOFanAPI mApi;
    private SharedPreferences mPrefs;

    public PlayerLoader(Context context) {
        super(context);
        this.context = context;


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(context.getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mApi = retrofit.create(EHCOFanAPI.class);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public ArrayList<Player> loadInBackground() {
        updatePlayers();

        ArrayList<Player> players = new ArrayList<>();
        SQLiteDatabase db = CacheDBHelper.getReadableDB(context);
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
        return players;
    }

    private void updatePlayers() {
        Call<ArrayList<PlayerWrapper>> call = mApi.listPlayers(mPrefs.getString(context.getString(R.string.pref_player_update), ""));
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ArrayList<PlayerWrapper>> call, Response<ArrayList<PlayerWrapper>> response) {

        for (PlayerWrapper p : response.body()) {
            String image_url;
            String lastUpdate = mPrefs.getString(context.getString(R.string.pref_player_update), "");
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
                    SharedPreferences.Editor editor = mPrefs.edit();
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
                }
            }
        }
    }

    @Override
    public void onFailure(Call<ArrayList<PlayerWrapper>> call, Throwable t) {

    }

    private void processWrappers(ArrayList<PlayerWrapper> wrappers){
        String lastUpdate = "0";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        for (PlayerWrapper p : wrappers) {
            Player player = p.toPlayer(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_account_circle_white_48dp));
            if(p.isActive()) {
                Cacher.cachePlayer(context, player);

                if (p.getPlayer_image_file_name() != null) {
                    try {
                        if (!Cacher.hasImage(context, player)) {
                            String image_url;
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
                }
            }
        }
    }
}
