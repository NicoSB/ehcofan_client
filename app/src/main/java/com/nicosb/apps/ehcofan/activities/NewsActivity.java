package com.nicosb.apps.ehcofan.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.ArticleFragment;
import com.nicosb.apps.ehcofan.fragments.ArticlesFragment;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.tasks.FetchArticlesTask;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements FetchArticlesTask.PostExecuteListener {
    private DrawerLayout drawerLayout;
    private String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        if (getIntent().getExtras() == null) {
            ArticlesFragment af = new ArticlesFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, af).commit();
        } else if(getIntent().getExtras().getParcelable(ArticleFragment.ARGS_ARTICLE) != null){
            ArticleFragment af = new ArticleFragment();
            Bundle args = new Bundle();
            args.putParcelable(ArticleFragment.ARGS_ARTICLE, getIntent().getExtras().getParcelable(ArticleFragment.ARGS_ARTICLE));
            af.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, af).commit();
        }
        else if(getIntent().getExtras().getBoolean("fromNotification", false)){
            FetchArticlesTask fetchArticlesTask = new FetchArticlesTask(this);
            fetchArticlesTask.setLimited(true);
            fetchArticlesTask.setPostExecuteListener(this);
            Article[] dummy = new Article[0];
            fetchArticlesTask.execute(dummy);
        }

        drawerLayout = ToolbarHelper.loadToolbar(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }

    @Override
    public void onPostExecute(ArrayList<Article> articles) {
        ArticleFragment af = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ArticleFragment.ARGS_ARTICLE, articles.get(0));
        af.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, af).commit();

    }
}
