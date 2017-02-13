package com.nicosb.apps.ehcofan.fragments;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.loaders.StandingsLoader;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Nico on 17.08.2016.
 */
public class StandingsFragment extends Fragment {
    public static String KEY_COMPETITION = "competition";
    private ProgressBar progressBar;
    private boolean inPager = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_standings, container, false);

        fetchTeams(getArguments().getString(KEY_COMPETITION));

        progressBar = new ProgressBar(getActivity());
        LinearLayout ll_container = (LinearLayout) v.findViewById(R.id.ll_standings);
        ll_container.addView(progressBar);

        if (inPager) {
            float scale = getResources().getDisplayMetrics().density;
            int paddingTop = (int) (8 * scale + 0.5f);
            v.setPadding(0, (int) getResources().getDimension(R.dimen.tab_height) + paddingTop, 0, 0);
        }

        return v;
    }

    private void fetchTeams(final String competition) {
        final int STANDINGS_LOADER_ID = 9;
        getActivity().getSupportLoaderManager().initLoader(STANDINGS_LOADER_ID, getActivity().getIntent().getExtras(), new LoaderManager.LoaderCallbacks<ArrayList<StandingsTeam>>() {
            @Override
            public Loader<ArrayList<StandingsTeam>> onCreateLoader(int id, Bundle args) {
                return new StandingsLoader(getActivity(), competition);
            }

            @Override
            public void onLoadFinished(Loader<ArrayList<StandingsTeam>> loader, ArrayList<StandingsTeam> data) {
                init(data);
                getActivity().getSupportLoaderManager().destroyLoader(STANDINGS_LOADER_ID);
            }

            @Override
            public void onLoaderReset(Loader<ArrayList<StandingsTeam>> loader) {

            }
        }).forceLoad();
    }

    private void init(ArrayList<StandingsTeam> standingsTeams){
        if (getActivity() != null) {
            if (standingsTeams.size() == 0) {
                TextView txt_no_connection = (TextView) getActivity().findViewById(R.id.txt_noconnection);
                txt_no_connection.setVisibility(View.VISIBLE);
            }
            TableLayout container = (TableLayout) getActivity().findViewById(R.id.container_standings);
            int position = 1;
            String group = "";
            for (StandingsTeam t : standingsTeams) {
                if (t.getGroup().length() > 0 && !t.getGroup().equals(group)) {
                    group = t.getGroup();
                    position = 1;
                    addGroupHeader(group, container);
                }
                View tableRow = getActivity().getLayoutInflater().inflate(R.layout.listitem_team, null, false);

                TextView txt_rank = (TextView) tableRow.findViewById(R.id.txt_rank);
                TextView txt_name = (TextView) tableRow.findViewById(R.id.txt_team_name);
                TextView txt_games = (TextView) tableRow.findViewById(R.id.txt_games);
                TextView txt_wins = (TextView) tableRow.findViewById(R.id.txt_wins);
                TextView txt_ot_wins = (TextView) tableRow.findViewById(R.id.txt_ot_wins);
                TextView txt_ot_losses = (TextView) tableRow.findViewById(R.id.txt_ot_losses);
                TextView txt_losses = (TextView) tableRow.findViewById(R.id.txt_losses);
                TextView txt_goals = (TextView) tableRow.findViewById(R.id.txt_goals);
                TextView txt_points = (TextView) tableRow.findViewById(R.id.txt_points);

                txt_rank.setText(String.format(Locale.GERMANY, "%d.", position));
                txt_name.setText(t.getName());
                txt_games.setText(String.valueOf(t.getGames()));
                txt_goals.setText(String.format(Locale.GERMANY, "%d:%d", t.getGoals_for(), t.getGoals_against()));
                txt_points.setText(String.valueOf(t.getPoints()));

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    txt_wins.setText(String.valueOf(t.getWins()));
                    txt_ot_wins.setText(String.valueOf(t.getOt_wins()));
                    txt_ot_losses.setText(String.valueOf(t.getOt_losses()));
                    txt_losses.setText(String.valueOf(t.getLosses()));
                }

                if (t.getName().equals("EHC Olten" )) {
                    txt_name.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_name.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_rank.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_rank.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_games.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_games.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_goals.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_goals.setTypeface(Typeface.DEFAULT_BOLD);
                    txt_points.setTextColor(getResources().getColor(R.color.mainGreen));
                    txt_points.setTypeface(Typeface.DEFAULT_BOLD);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        txt_wins.setTextColor(getResources().getColor(R.color.mainGreen));
                        txt_wins.setTypeface(Typeface.DEFAULT_BOLD);
                        txt_ot_wins.setTextColor(getResources().getColor(R.color.mainGreen));
                        txt_ot_wins.setTypeface(Typeface.DEFAULT_BOLD);
                        txt_losses.setTextColor(getResources().getColor(R.color.mainGreen));
                        txt_losses.setTypeface(Typeface.DEFAULT_BOLD);
                        txt_ot_losses.setTextColor(getResources().getColor(R.color.mainGreen));
                        txt_ot_losses.setTypeface(Typeface.DEFAULT_BOLD);
                    }
                }

                container.addView(tableRow);
                if (position == 8) {
                    container.addView(getLine());
                }
                position++;
            }

            LinearLayout ll_standings = (LinearLayout) getActivity().findViewById(R.id.ll_standings);
            ll_standings.removeView(progressBar);
        }
    }
    private View getLine() {
        View v = new View(getActivity());
        v.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        v.setBackgroundColor(getResources().getColor(R.color.mainGreen));
        return v;
    }

    private void addGroupHeader(String group, TableLayout container) {
        TextView tv = new TextView(getContext());
        tv.setBackgroundColor(getContext().getResources().getColor(R.color.mainGreen));
        tv.setText(group);
        tv.setTextColor(getContext().getResources().getColor(R.color.white));
        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (8 * scale + 0.5f);
        tv.setPadding(padding, 0, padding, 0);
        tv.setGravity(Gravity.CENTER_VERTICAL);

        container.addView(tv);
    }
}
