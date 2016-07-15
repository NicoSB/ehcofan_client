package com.nicosb.apps.ehcofan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;

import com.nicosb.apps.ehcofan.models.Article;

/**
 * Created by Nico on 01.07.2016.
 */
public class ArticleFragment extends Fragment {
    Article article;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            article = bundle.getParcelable("article");
            displayArticle();
        }
    }

    private void displayArticle() {
        TextView txt_title = (TextView)getActivity().findViewById(R.id.text_title);
        TextView txt_text = (TextView)getActivity().findViewById(R.id.text_text);

        txt_title.setText(article.getTitle());
        txt_text.setText(Html.fromHtml(article.getText()));
    }
}
