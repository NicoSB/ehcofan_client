package com.nicosb.apps.ehcofan.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;
import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.retrofit.EHCOFanAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticleFragment extends Fragment {
    public static final String ARGS_ARTICLE = "mArticle";
    private Article mArticle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_article, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        int id;
        if (bundle != null && (mArticle = bundle.getParcelable(ARGS_ARTICLE)) != null) {
            displayArticle();
        }
        else if(bundle != null && (id = bundle.getInt(getString(R.string.news_id))) != 0){
            fetchArticle(id);
        }
    }

    private void fetchArticle(int id) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.rest_interface))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        EHCOFanAPI api = retrofit.create(EHCOFanAPI.class);
        Call<ArticleWrapper> article = api.getArticle(id);

        Callback<ArticleWrapper> cbArticle = new Callback<ArticleWrapper>() {
            @Override
            public void onResponse(Call<ArticleWrapper> call, Response<ArticleWrapper> response) {
                if(response.body() != null) {
                    mArticle = response.body().toNoPicArticle();
                    displayArticle();
                }
            }

            @Override
            public void onFailure(Call<ArticleWrapper> call, Throwable t) {

            }
        };

        article.enqueue(cbArticle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_article, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.getUrl()));
                startActivity(browserIntent);
                return true;
            default:
                return false;
        }
    }

    private void displayArticle() {
        TextView txt_title = (TextView) getActivity().findViewById(R.id.text_title);
        TextView txt_text = (TextView) getActivity().findViewById(R.id.text_text);

        txt_title.setText(mArticle.getTitle());
        txt_text.setText(Html.fromHtml(mArticle.getText()));
        txt_text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
