package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;

import com.nicosb.apps.ehcofan.models.Article;

/**
 * Created by Nico on 30.06.2016.
 */
public class ArticleView extends RelativeLayout {
    private Article article;

    public ArticleView(Context context){
        super(context);
        inflate(context, R.layout.view_article, this);
    }

    public ArticleView(Context context, Article article){
        super(context);
        this.article = article;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_article, this);
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl_article);
        TextView txt_title = (TextView)findViewById(R.id.text_title);
        TextView txt_date = (TextView)findViewById(R.id.text_date);
        ImageView img_news = (ImageView)findViewById(R.id.imageview);

        txt_date.setText(article.getDisplayDate());
        txt_title.setText(article.getTitle());
        img_news.setImageDrawable(new BitmapDrawable(article.getNews_image()));
    }

    public Article getArticle() {
        return article;
    }
}
