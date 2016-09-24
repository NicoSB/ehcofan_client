package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.tasks.FetchArticlesTask;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.BottomRefreshScrollView;

import java.util.ArrayList;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticlesFragment extends Fragment
        implements FetchArticlesTask.PostExecuteListener,
        BottomRefreshScrollView.ViewOnBottomListener {
    ProgressBar progressBar;
    ArrayList<Article> articles = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
    FetchArticlesTask fetchArticlesTask;
    boolean allArticlesLoaded = false;
    boolean fetching = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_articles, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeContainer = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });

        BottomRefreshScrollView brsview = (BottomRefreshScrollView) getActivity().findViewById(R.id.scroll_view);
        brsview.setViewOnBottomListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (articles.size() > 0) {
            displayArticles();
        } else {
            update(true);
        }
    }

    private void update(boolean showProgressbar) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            fetchArticles(showProgressbar);
        } else {
            displayNoConnectionMessage();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_news, menu);
    }

    private void displayNoConnectionMessage() {
        TextView tv = (TextView) getActivity().findViewById(R.id.txt_noconnection);
        tv.setVisibility(View.VISIBLE);
    }

    private void fetchArticles(boolean showProgessbar) {
        Article[] articlesArray = new Article[articles.size()];

        if (fetchArticlesTask != null && fetchArticlesTask.getStatus() != AsyncTask.Status.FINISHED) {
            fetchArticlesTask.cancel(true);
        }
        fetchArticlesTask = new FetchArticlesTask(getContext());
        fetchArticlesTask.setPostExecuteListener(this);
        articles.toArray(articlesArray);
        fetchArticlesTask.execute(articlesArray);

        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.ll_articles);
        if (showProgessbar) {
            if (progressBar == null) {
                progressBar = new ProgressBar(getActivity());
            }
            ll.addView(progressBar);
        }
    }

    private void displayArticles() {
        LinearLayout rl = (LinearLayout) getActivity().findViewById(R.id.ll_articles);
        for (Article a : articles) {
            ArticleView av = new ArticleView(getActivity(), a);
            av.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startFragment(view);
                }
            });
            rl.addView(av);
        }
    }

    private void startFragment(View view) {
        ArticleView av = (ArticleView) view;
        ArticleFragment af = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable("article", av.getArticle());
        af.setArguments(args);

        allArticlesLoaded = false;
        fetching = false;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, af);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPostExecute(ArrayList<Article> articles) {
        LinearLayout rl = (LinearLayout) getActivity().findViewById(R.id.ll_articles);

        if (articles != null) {
            this.articles = articles;
            if (rl == null) {
                return;
            }
            rl.removeAllViews();

            displayArticles();
            swipeContainer.setRefreshing(false);
            fetching = false;
        } else {
            allArticlesLoaded = true;
            Toast.makeText(getContext(), "Alle News-Einträge geladen", Toast.LENGTH_SHORT).show();
        }

        rl.removeView(progressBar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fetchArticlesTask != null && fetchArticlesTask.getStatus() != AsyncTask.Status.FINISHED) {
            fetchArticlesTask.cancel(true);
        }
    }

    private void refresh(boolean showProgressbar) {
        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.ll_articles);
        if (ll == null) {
            return;
        }
        ll.removeAllViews();
        articles.clear();
        allArticlesLoaded = false;

        TextView textView = (TextView) getActivity().findViewById(R.id.txt_noconnection);
        textView.setVisibility(View.GONE);

        update(showProgressbar);
    }

    @Override
    public void onBottomReached() {
        if (!allArticlesLoaded && !fetching) {
            fetching = true;
            update(true);
        } else {
            if (allArticlesLoaded) {
                Toast.makeText(getContext(), "Alle News-Einträge geladen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
