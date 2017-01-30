package com.nicosb.apps.ehcofan.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nico on 24.01.2017.
 */

public class ArticleImageLoader extends AsyncTaskLoader<ArrayList<Article>> {
    private static final String TAG = "ArticleImageLoader";
    private List<ArticleWrapper> wrappers;
    private Context context;
    private Article[] articles;
    public ArticleImageLoader(Context context, Article[] articles, ArrayList<ArticleWrapper> wrappers) {
        super(context);

        this.articles = articles;
        this.context = context;
        this.wrappers = wrappers;
    }

    public ArticleImageLoader(Context context, List<Article> articlesList, ArrayList<ArticleWrapper> wrappers) {
        super(context);
        articles = new Article[articlesList.size()];
        articlesList.toArray(articles);
        this.context = context;
        this.wrappers = wrappers;
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        ArrayList<Article> mArticles = new ArrayList<>();
        if(articles != null) Collections.addAll(mArticles, articles);
        for (ArticleWrapper aw : wrappers) {
            try {

                String rest = context.getString(R.string.aws_url);
                String id = ("00" + aw.getId());
                id = id.substring(id.length() - 3);
                String image_url = String.format(Locale.GERMANY, rest + "articles/news_images/000/000/%s/original/%s", id, aw.getNews_image_file_name());
                URL imageAddress = new URL(image_url);
                HttpURLConnection imageUrlConnection = (HttpURLConnection) imageAddress.openConnection();
                InputStream input = imageUrlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), bitmap);
                mArticles.add(a);
            } catch (FileNotFoundException fnfe) {
                Article a = new Article(aw.getTitle(), aw.getText(), aw.getUrl(), aw.getDate(), BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder));
                mArticles.add(a);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return mArticles;
    }
}
