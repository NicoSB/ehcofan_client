package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
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
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nico on 23.07.2016.
 */
public class FetchPlayersTask extends AsyncTask<String, Void, ArrayList<Player>>{
    private static final String TAG = "FetchPlayersTask";
    private Context context;
    private OnPlayersFetchedListener onPlayersFetchedListener;
    ArrayList<Player> players;

    public FetchPlayersTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Player> doInBackground(String... strings) {
        try {
            String rest_url = context.getString(R.string.rest_interface) + "players";
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
                if (p.getPlayer_image_file_name() != null) {
                    try {
                        String rest = context.getString(R.string.aws_url);
                        String id = ("00" + p.getId());
                        id = id.substring(id.length() - 3);
                        image_url = String.format(Locale.GERMANY, rest + "players/player_images/000/000/%s/original/%s", id, p.getPlayer_image_file_name());
                        URL imageAddress = new URL(image_url);
                        HttpURLConnection imageConnection = (HttpURLConnection) imageAddress.openConnection();
                        InputStream input = imageConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(input);

                        player.setPlayerImage(bitmap);
                    } catch (FileNotFoundException fnfe) {
                        Log.w(TAG, "Error with: " + image_url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
                players.add(player);
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            return null;
        }

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
