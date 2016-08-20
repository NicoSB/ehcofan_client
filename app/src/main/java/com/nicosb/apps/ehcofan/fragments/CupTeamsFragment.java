package com.nicosb.apps.ehcofan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicosb.apps.ehcofan.R;

/**
 * Created by Nico on 15.08.2016.
 */
public class CupTeamsFragment extends Fragment {
    private static final String KEY_POSITION = "position";

    public CupTeamsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cup_teams, container, false);
    }

    public static CupTeamsFragment newInstance(int position){
        CupTeamsFragment frag=new CupTeamsFragment();
        Bundle args = new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return(frag);
    }
}
