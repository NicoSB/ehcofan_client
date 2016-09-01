package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Nico on 20.07.2016.
 */
public class MatchView extends RelativeLayout{
    private Match match;

    public MatchView(Context context, Match match, boolean showCompetition) {
        super(context);
        this.match = match;
        init(showCompetition);
    }

    private void init(boolean showCompetition) {
        inflate(getContext(), R.layout.view_match, this);
        TextView txt_home = (TextView)findViewById(R.id.txt_home);
        TextView txt_away = (TextView)findViewById(R.id.txt_away);
        TextView txt_h1 = (TextView)findViewById(R.id.txt_h1);
        TextView txt_h2 = (TextView)findViewById(R.id.txt_h2);
        TextView txt_h3 = (TextView)findViewById(R.id.txt_h3);
        TextView txt_a1 = (TextView)findViewById(R.id.txt_a1);
        TextView txt_a2 = (TextView)findViewById(R.id.txt_a2);
        TextView txt_a3 = (TextView)findViewById(R.id.txt_a3);
        TextView txt_h_total = (TextView)findViewById(R.id.txt_h_total);
        TextView txt_a_total = (TextView)findViewById(R.id.txt_a_total);
        TextView txt_datetime = (TextView)findViewById(R.id.txt_date);

        txt_home.setText(match.getHome_team());
        txt_away.setText(match.getAway_team());
        txt_h1.setText(String.valueOf(match.getScores_home()[0]));
        txt_h2.setText(String.valueOf(match.getScores_home()[1]));
        txt_h3.setText(String.valueOf(match.getScores_home()[2]));
        txt_h_total.setText(String.valueOf(match.getTotal(Match.Team.HOME)));
        txt_a1.setText(String.valueOf(match.getScores_away()[0]));
        txt_a2.setText(String.valueOf(match.getScores_away()[1]));
        txt_a3.setText(String.valueOf(match.getScores_away()[2]));
        txt_a_total.setText(String.valueOf(match.getTotal(Match.Team.AWAY)));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String dt_text = sdf.format(match.getDatetime().getTime());
        if(showCompetition){
            dt_text = dt_text + " - " + match.getCompetition();
        }
        txt_datetime.setText(dt_text);

        if(match.getHome_team().equals("EHC Olten")){
            txt_home.setTypeface(null, Typeface.BOLD);
        }
        else if(match.getAway_team().equals("EHC Olten")){
            txt_away.setTypeface(null, Typeface.BOLD);
        }

        if(!(match.getScores_home()[3] == 0 && match.getScores_away()[3] == 0)){
            LinearLayout container_ot = (LinearLayout)findViewById(R.id.container_overtime);
            TextView txt_a_ot = (TextView)findViewById(R.id.txt_a_ot);
            TextView txt_h_ot = (TextView)findViewById(R.id.txt_h_ot);

            container_ot.setVisibility(VISIBLE);
            txt_h_ot.setText(String.valueOf(match.getScores_home()[3]));
            txt_a_ot.setText(String.valueOf(match.getScores_away()[3]));
        }
    }
}
