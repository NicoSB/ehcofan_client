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

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Article;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticleFragment extends Fragment {
    public static String ARGS_ARTICLE = "article";
    Article article;

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
        if (bundle != null) {
            article = bundle.getParcelable(ARGS_ARTICLE);
            displayArticle();
        }
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
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                startActivity(browserIntent);
                return true;
            default:
                return false;
        }
    }

    private void displayArticle() {
        TextView txt_title = (TextView) getActivity().findViewById(R.id.text_title);
        TextView txt_text = (TextView) getActivity().findViewById(R.id.text_text);

        txt_title.setText(article.getTitle());
        txt_text.setText(Html.fromHtml(article.getText()));
        txt_text.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
