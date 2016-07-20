package com.nicosb.apps.ehcofan.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.google.gson.Gson;

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
import java.util.logging.Logger;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;

/**
 * Created by Nico on 30.06.2016.
 */
public class FetchArticlesTask extends AsyncTask<String, Void, ArticleWrapper[]> {
    Logger log = Logger.getLogger("FetchArticlesTask");
    private PostExecuteListener postExecuteListener;
    private ArrayList<Article> articles = new ArrayList<>();
    @Override
    protected ArticleWrapper[] doInBackground(String... strings) {
        log.warning("Start fetch articles");
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
            log.warning("Converting JSON");
            return gson.fromJson(json, ArticleWrapper[].class);
        }catch (IOException mue){
            mue.printStackTrace();
        }
        return null;
    }

    public void setPostExecuteListener(PostExecuteListener postExecuteListener) {
        this.postExecuteListener = postExecuteListener;
    }

    @Override
    protected void onPostExecute(ArticleWrapper[] articles) {
        log.warning("End of fetching articles");
        super.onPostExecute(articles);
        if(postExecuteListener != null){
            postExecuteListener.onPostExecute(articles);
        }
    }

    public interface PostExecuteListener{
        void onPostExecute(ArticleWrapper[] wrappers);
    }

    public static class FetchImagesTask extends AsyncTask<ArticleWrapper, Void, ArrayList<Article>>{
        FetchImagesListener fetchImagesListener;
        private ArrayList<Article> articles = new ArrayList<>();
        private Context context;
        Logger log = Logger.getLogger("FetchImagesTask");

        public FetchImagesTask(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected ArrayList<Article> doInBackground(ArticleWrapper... wrappers) {
            log.warning("Start fetching images");
            String image_url = "";
            for (ArticleWrapper aw : wrappers) {
                try {
                    Drawable news_image = null;
                    String rest = context.getString(R.string.aws_url);
                    image_url = String.format(Locale.GERMANY, rest + "articles/news_images/000/000/00%d/original/%s", aw.getId(), aw.getNews_image_file_name());
                    URL imageAddress = new URL(image_url);
                    HttpURLConnection urlConnection = (HttpURLConnection) imageAddress.openConnection();
                    InputStream input = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(input);

                    Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), bitmap);
                    articles.add(a);
                }catch (FileNotFoundException fnfe){
                    Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder));
                    articles.add(a);
                    log.warning("Error with: " + image_url);
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }
            }

            log.warning("end fetching images");

            return articles;
        }

        @Override
        protected void onPostExecute(ArrayList<Article> articles) {
            super.onPostExecute(articles);
            if(fetchImagesListener != null){
                fetchImagesListener.onImagesFetched(articles);
            }
        }

        public void setFetchImagesListener(FetchImagesListener fetchImagesListener) {
            this.fetchImagesListener = fetchImagesListener;
        }

        public interface FetchImagesListener {
            void onImagesFetched(ArrayList<Article> articles);
        }
    }
}
