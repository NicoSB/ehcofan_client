package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;

import models.Article;

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
        TextView txt_title = (TextView)findViewById(R.id.text_title);
        TextView txt_date = (TextView)findViewById(R.id.text_date);

        txt_date.setText(article.getDisplayDate());
        txt_title.setText(article.getTitle());
    }

    public Article getArticle() {
        return article;
    }
}
