package com.nicosb.apps.ehcofan.activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.ToolbarHelper;
import com.nicosb.apps.ehcofan.fragments.ArticleFragment;
import com.nicosb.apps.ehcofan.fragments.ArticlesFragment;


public class NewsActivity extends AppCompatActivity{
    private DrawerLayout drawerLayout;
    private String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        int news_id;
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
        else if((news_id  = getIntent().getExtras().getInt(getString(R.string.news_id), 0)) > 0){
            ArticleFragment af = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(getString(R.string.news_id), news_id);
            af.setArguments(args);

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, af).commit();
        }

        drawerLayout = ToolbarHelper.loadToolbar(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }
}
