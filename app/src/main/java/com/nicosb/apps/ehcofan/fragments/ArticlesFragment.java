package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicosb.apps.ehcofan.tasks.FetchArticlesTask;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.BottomRefreshScrollView;

import java.util.ArrayList;

import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticlesFragment extends Fragment
        implements FetchArticlesTask.PostExecuteListener,
        FetchArticlesTask.FetchImagesTask.FetchImagesListener,
        BottomRefreshScrollView.ViewOnBottomListener{
    ProgressBar progressBar;
    ArrayList<Article> articles = new ArrayList<>();
    SwipeRefreshLayout swipeContainer;
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
        swipeContainer = (SwipeRefreshLayout)getActivity().findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        BottomRefreshScrollView brsview = (BottomRefreshScrollView)getActivity().findViewById(R.id.scroll_view);
        brsview.setViewOnBottomListener(this);
        update();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void update() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            fetchArticles();
        }
        else{
            displayNoConnectionMessage();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
    }

    private void displayNoConnectionMessage() {
        TextView tv = (TextView)getActivity().findViewById(R.id.txt_noconnection);
        tv.setVisibility(View.VISIBLE);
    }

    private void fetchArticles(){
        String link = getString(R.string.rest_interface) + "articles?offset=" + articles.size();

        FetchArticlesTask nwTask = new FetchArticlesTask();
        nwTask.setPostExecuteListener(this);
        nwTask.execute(link);

        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.rl_articles);
        progressBar = new ProgressBar(getActivity());
        rl.addView(progressBar);
    }

    private void displayArticles() {
        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.rl_articles);
        for(Article a: articles){
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
        RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.fragment_container);
        rl.removeAllViews();

        ArticleView av = (ArticleView)view;
        ArticleFragment af = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable("article", av.getArticle());
        af.setArguments(args);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, af);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onPostExecute(ArticleWrapper[] wrappers) {
        if(wrappers.length == 0) {
            allArticlesLoaded = true;
            Toast.makeText(getActivity(), "All news loaded", Toast.LENGTH_SHORT).show();
        }
        else{
            FetchArticlesTask.FetchImagesTask fetchImagesTask = new FetchArticlesTask.FetchImagesTask(getContext());
            fetchImagesTask.setFetchImagesListener(this);
            fetchImagesTask.execute(wrappers);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_refresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh() {
        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.rl_articles);
        if(rl == null){
            return;
        }
        rl.removeAllViews();
        articles.clear();
        allArticlesLoaded = false;

        TextView textView = (TextView)getActivity().findViewById(R.id.txt_noconnection);
        textView.setVisibility(View.GONE);

        update();
    }

    @Override
    public void onBottomReached() {
        if(!allArticlesLoaded && !fetching) {
            fetching = true;
            update();
        }
        else{
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onImagesFetched(ArrayList<Article> articles) {
        for(Article a: articles){
            this.articles.add(a);
        }

        LinearLayout rl = (LinearLayout) getActivity().findViewById(R.id.rl_articles);
        if(rl == null){
            return;
        }
        rl.removeAllViews();
        progressBar.setVisibility(View.GONE);
        displayArticles();
        swipeContainer.setRefreshing(false);
        fetching = false;
    }
}
