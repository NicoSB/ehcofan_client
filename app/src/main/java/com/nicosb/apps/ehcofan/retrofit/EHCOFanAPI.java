package com.nicosb.apps.ehcofan.retrofit;

import com.nicosb.apps.ehcofan.models.ArticleWrapper;
import com.nicosb.apps.ehcofan.models.MatchWrapper;
import com.nicosb.apps.ehcofan.models.PlayerWrapper;
import com.nicosb.apps.ehcofan.models.TeamWrapper;

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
    Call<ArrayList<MatchWrapper>> listMatches(@Query("updated_at") String updated_at);

    @GET("articles")
    Call<ArrayList<ArticleWrapper>> listArticles(@Query("limit") int limit);

    @GET("articles")
    Call<ArrayList<ArticleWrapper>> listArticles(@Query("limit") int limit, @Query("offset") int offset);

    @GET("articles/{id}")
    Call<ArticleWrapper> getArticle(@Path("id") int id);

    @GET("players")
    Call<ArrayList<PlayerWrapper>> listPlayers(@Query("updated_at") String updated_at);

    @GET("teams")
    Call<ArrayList<TeamWrapper>> listTeams();

    @GET("teams")
    Call<ArrayList<TeamWrapper>> listTeams(@Query("competition") String competition);

    @GET("playoffs.json?mode=run")
    Call<Boolean> isPlayoffs();
}
