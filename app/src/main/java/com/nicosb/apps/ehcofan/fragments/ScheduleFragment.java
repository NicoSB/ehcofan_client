package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.views.MatchView;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment
                                implements FetchMatchesTask.OnScheduleFetchedListener{


    private static final String TAG = "ScheduleFragment";
    private ArrayList<Match> matches = new ArrayList<>();
    private Spinner spinner;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spinner = (Spinner) getActivity().findViewById(R.id.schedule_spinner);

        SpinnerAdapter adapter = ArrayAdapter.createFromResource(getContext().getApplicationContext(), R.array.competition_names, R.layout.spinner_item_competition);
        spinner.setAdapter(adapter);
        spinner.setGravity(Gravity.END);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayMatches();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        update();
    }

    private void fetchMatches() {
        FetchMatchesTask fetchMatchesTask = new FetchMatchesTask(getContext());
        fetchMatchesTask.setOnScheduleFetchedListener(this);
        if(spinner == null){
            fetchMatchesTask.execute("");
        }else {
            fetchMatchesTask.execute(spinner.getSelectedItem().toString());
        }
        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.schedule_fragment_container);
        progressBar = new ProgressBar(getActivity());
        rl.addView(progressBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMatches();
    }

    private void displayMatches() {
        LinearLayout container = (LinearLayout)getActivity().findViewById(R.id.schedule_fragment_container);
        if(container != null) {
            container.removeAllViews();
            for (Match m : matches) {
                MatchView mv = new MatchView(getContext(), m);
                container.addView(mv);
            }
        }
    }

    private void update() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            fetchMatches();
        }
        else{
            displayNoConnectionMessage();
        }
    }

    @Override
    public void onScheduleFetched(ArrayList<Match> matches) {
        this.matches = matches;
        displayMatches();
    }

    private void displayNoConnectionMessage() {
        TextView tv = (TextView)getActivity().findViewById(R.id.txt_noconnection);
        tv.setVisibility(View.VISIBLE);
    }
}
