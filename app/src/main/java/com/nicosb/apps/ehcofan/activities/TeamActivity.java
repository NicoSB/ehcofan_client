package com.nicosb.apps.ehcofan.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.PremadeDBHelper;
import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.StandingsTeam;
import com.nicosb.apps.ehcofan.models.Team;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by Nico on 15.08.2016.
 */
public class TeamActivity extends AppCompatActivity {
    Team team;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PremadeDBHelper dbHelper = null;
        try {
            dbHelper = new PremadeDBHelper(this);
            dbHelper.createDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert dbHelper != null;
        dbHelper.openDatabase();
        int teamId = 1;

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            teamId = extras.getInt("team_id");
        }
        else{
            savedInstanceState.getInt("team_id");
        }
        team = dbHelper.getTeam(teamId);

        init();
    }

    private void init(){
        ImageView img_team_logo = (ImageView)findViewById(R.id.team_logo);
        TextView txt_team_name = (TextView)findViewById(R.id.team_name);
        TextView txt_team_league = (TextView)findViewById(R.id.team_league);
        TextView txt_team_placement = (TextView)findViewById(R.id.team_placement);
        TextView txt_team_founded = (TextView)findViewById(R.id.team_founded);
        TextView txt_team_titles = (TextView)findViewById(R.id.team_titles);
        TextView txt_team_topscorer = (TextView)findViewById(R.id.team_topscorer);
        TextView txt_team_text = (TextView)findViewById(R.id.txt_team);

        img_team_logo.setImageDrawable(getTeamLogo(team.get_id()));
        txt_team_name.setText(team.getName());
        txt_team_league.setText(String.format(Locale.GERMAN, "%s • %s" ,team.getLeague(), team.getCountry()));
        txt_team_placement.setText(team.getLast_season());
        txt_team_founded.setText(String.valueOf(team.getFounded()));
        txt_team_titles.setText(String.valueOf(team.getTitle_count()));
        txt_team_topscorer.setText(team.getTopscorer());
        txt_team_text.setText(Html.fromHtml(getTeamText(team.get_id())));
    }

    private Drawable getTeamLogo(int id){
        switch(id){
            case 1:
                return getDrawable(R.drawable.logo_ehco);
            case 2:
                return getDrawable(R.drawable.logo_scl);
            case 3:
                return getDrawable(R.drawable.logo_ir);
            case 4:
                return getDrawable(R.drawable.logo_gw);
            case 5:
                return getDrawable(R.drawable.logo_gs);
            case 6:
                return getDrawable(R.drawable.logo_kp);
        }

        return null;
    } 
    
    private String getTeamText(int id){
        switch(id){
            case 1:
                return getString(R.string.text_ehco);
            case 2:
                return getString(R.string.text_scl);
            case 3:
                return getString(R.string.text_ir);
            case 4:
                return getString(R.string.text_gw);
            case 5:
                return getString(R.string.text_gs);
            case 6:
                return getString(R.string.text_kp);
        }

        return null;
    }
}
