package com.nicosb.apps.ehcofan;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import models.Article;

/**
 * Created by Nico on 30.06.2016.
 */
public class NetworkTask extends AsyncTask<String, Void, ArrayList<Article>> {
    Logger log = Logger.getLogger("NetworkTask");
    private PostExecuteListener postExecuteListener;

    @Override
    protected ArrayList<Article> doInBackground(String... strings) {
        ArrayList<Article> articles = new ArrayList<>();
        try{
            URL restAddress = new URL(strings[0]);
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
            Article[] articlesArray = gson.fromJson(json, Article[].class);
            for(Article a: articlesArray){
                articles.add(a);
            }
        }catch (IOException mue){
            mue.printStackTrace();
        }

        return articles;
    }

    public void setPostExecuteListener(PostExecuteListener postExecuteListener) {
        this.postExecuteListener = postExecuteListener;
    }

    @Override
    protected void onPostExecute(ArrayList<Article> articles) {
        super.onPostExecute(articles);
        if(postExecuteListener != null){
            postExecuteListener.onPostExecute(articles);
        }
    }

    public interface PostExecuteListener{
        void onPostExecute(ArrayList<Article> articles);
    }
}
