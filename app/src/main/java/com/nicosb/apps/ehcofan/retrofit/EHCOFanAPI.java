package com.nicosb.apps.ehcofan.retrofit;

import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.models.MatchWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Nico on 24.01.2017.
 */

public interface EHCOFanAPI {
    @GET("matches")
    Call<List<MatchWrapper>> listMatches();

//    @GET("articles?offset={offset}")
//    Call<List<ArticleWrapper>> listArticles(@Path("offset") int offset);

    @GET("articles")
    Call<ArrayList<ArticleWrapper>> listArticles(@Query("limit") int limit);

    @GET("articles")
    Call<ArrayList<ArticleWrapper>> listArticles(@Query("limit") int limit, @Query("offset") int offset);
}
