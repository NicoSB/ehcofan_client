package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;

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
 * Created by Nico on 30.06.2016.
 */
public class FetchArticlesTask extends AsyncTask<Article, Void, ArrayList<Article>> {
    private PostExecuteListener postExecuteListener;
    private ArrayList<Article> mArticles = new ArrayList<>();
    private Context context;
    private boolean isLimited = false;

    public FetchArticlesTask(Context context) {
        this.context = context;
    }

    @Override
    protected ArrayList<Article> doInBackground(Article... articles) {
        for(Article a: articles){
            if(a != null){
                mArticles.add(a);
            }
        }
        try{
            String link = context.getString(R.string.rest_interface) + "articles?offset=" + mArticles.size();
            URL restAddress = new URL(link);
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
            if(isCancelled()){
                return null;
            }
            ArticleWrapper[] wrappers = gson.fromJson(json, ArticleWrapper[].class);
            String image_url = "";
            if(wrappers.length == 0){
                return null;
            }

            for (ArticleWrapper aw : wrappers) {
                try {
                    String rest = context.getString(R.string.aws_url);
                    String id = ("00" + aw.getId());
                    id = id.substring(id.length() - 3);
                    image_url = String.format(Locale.GERMANY, rest + "articles/news_images/000/000/%s/original/%s", id, aw.getNews_image_file_name());
                    URL imageAddress = new URL(image_url);
                    HttpURLConnection imageUrlConnection = (HttpURLConnection) imageAddress.openConnection();
                    InputStream input = imageUrlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), bitmap);
                    mArticles.add(a);
                    if(isLimited){
                        return mArticles;
                    }
                }catch (FileNotFoundException fnfe){
                    Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder));
                    mArticles.add(a);
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }catch (IOException mue){
            mue.printStackTrace();
        }
        return mArticles;
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

    public boolean isLimited() {
        return isLimited;
    }

    public void setLimited(boolean limited) {
        isLimited = limited;
    }
}
