package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;
import com.nicosb.apps.ehcofan.loaders.ArticleImageLoader;
import com.nicosb.apps.ehcofan.views.ArticleView;
import com.nicosb.apps.ehcofan.views.BottomRefreshScrollView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticlesFragment extends Fragment implements
        BottomRefreshScrollView.ViewOnBottomListener {
    private static final String TAG = "ArticlesFragment";
    private ProgressBar progressBar;
    private ArrayList<Article> articles = new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;
    private boolean allArticlesLoaded = false;
    private boolean fetching = false;
    private EHCOFanAPI mApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_articles, container, false);
        setHasOptionsMenu(true);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = retrofit.create(EHCOFanAPI.class);

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

    private void fetchArticles(boolean showProgressbar) {
        final Callback<ArrayList<ArticleWrapper>> articlesCB = new Callback<ArrayList<ArticleWrapper>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticleWrapper>> call, final Response<ArrayList<ArticleWrapper>> response) {
                getActivity().getSupportLoaderManager().initLoader(3, getActivity().getIntent().getExtras(), new LoaderManager.LoaderCallbacks<ArrayList<Article>>() {
                    @Override
                    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
                        return new ArticleImageLoader(getActivity(), articles, response.body());
                    }

                    @Override
                    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
                        setArticles(data);
                        displayArticles();
                        fetching = false;
                        swipeContainer.setRefreshing(false);
                        getActivity().getSupportLoaderManager().destroyLoader(3);
                    }

                    @Override
                    public void onLoaderReset(Loader<ArrayList<Article>> loader) {

                    }
                }).forceLoad();
            }

            @Override
            public void onFailure(Call<ArrayList<ArticleWrapper>> call, Throwable t) { }
        };

        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.ll_articles);
        if (showProgressbar) {
            if (progressBar == null) {
                progressBar = new ProgressBar(getActivity());
            }
             ll.addView(progressBar);
        }

        Call<ArrayList<ArticleWrapper>> call = mApi.listArticles(5, articles.size());
        call.enqueue(articlesCB);
    }

    private void displayArticles() {
        LinearLayout rl = (LinearLayout) getActivity().findViewById(R.id.ll_articles);
        rl.removeAllViews();
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
        args.putParcelable(ArticleFragment.ARGS_ARTICLE, av.getArticle());
        af.setArguments(args);

        allArticlesLoaded = false;
        fetching = false;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction()
            .replace(R.id.content_frame, af)
            .addToBackStack(null)
            .commit();
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

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
