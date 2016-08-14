package com.nicosb.apps.ehcofan.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.tasks.FetchMatchesTask;
import com.nicosb.apps.ehcofan.views.MatchView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment
                                implements FetchMatchesTask.OnScheduleFetchedListener{

    private static final String TAG = "ScheduleFragment";
    private ArrayList<Match> matches = new ArrayList<>();
    private Spinner spinner;
    private ProgressBar progressBar;
    private TextView tv;
    private FetchMatchesTask fetchMatchesTask;
    private String requestedCompetition = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_schedule, container, false);
        setHasOptionsMenu(true);
        spinner = (Spinner) getActivity().findViewById(R.id.schedule_spinner);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


    @Override
    public void onPause() {
        super.onPause();
        if(fetchMatchesTask != null && fetchMatchesTask.getStatus() != AsyncTask.Status.FINISHED){
            fetchMatchesTask.cancel(true);
        }
    }

    private void fetchMatches() {
        if(fetchMatchesTask != null && fetchMatchesTask.getStatus() != AsyncTask.Status.FINISHED){
            fetchMatchesTask.cancel(true);
        }
        fetchMatchesTask = new FetchMatchesTask(getContext());
        fetchMatchesTask.setOnScheduleFetchedListener(this);
        if(spinner == null || spinner.getSelectedItem().toString().equals("Alle")){
            requestedCompetition = "";
        }else {
            requestedCompetition = spinner.getSelectedItem().toString();
        }
        fetchMatchesTask.execute(requestedCompetition);

        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.container_schedule);
        rl.removeAllViews();

        progressBar = new ProgressBar(getActivity());
        TextView connectionMessage = (TextView)getActivity().findViewById(R.id.txt_schedule_noconnection);
        connectionMessage.setVisibility(View.GONE);
        rl.addView(progressBar);
    }

    private void displayMatches() {
        LinearLayout container = (LinearLayout)getActivity().findViewById(R.id.container_schedule);
        TextView connectionMessage = (TextView)getActivity().findViewById(R.id.txt_schedule_noconnection);
        if(connectionMessage.getVisibility() == View.VISIBLE){
            connectionMessage.setVisibility(View.GONE);
        }
        if(container != null) {
            if(matches.isEmpty()){
                TextView noMatchesMessage = new TextView(getContext());
                noMatchesMessage.setText("Keine Spiele gefunden");
                noMatchesMessage.setGravity(Gravity.CENTER);
                noMatchesMessage.setPadding(100, 100, 100, 100);
                container.addView(noMatchesMessage);
            }
            for (Match m : matches) {
                MatchView mv = new MatchView(getContext(), m, requestedCompetition.length() == 0);
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
        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.container_schedule);
        rl.removeView(progressBar);
        displayMatches();
    }

    private void displayNoConnectionMessage() {
        tv = (TextView)getActivity().findViewById(R.id.txt_schedule_noconnection);
        LinearLayout rl = (LinearLayout)getActivity().findViewById(R.id.container_schedule);
        rl.removeAllViews();

        if(tv != null) {
            tv.setVisibility(View.VISIBLE);
        }
    }
}
