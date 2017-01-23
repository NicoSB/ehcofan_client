package com.nicosb.apps.ehcofan.views;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicosb.apps.ehcofan.R;
import com.nicosb.apps.ehcofan.models.Match;
import com.nicosb.apps.ehcofan.models.POMatchup;

import java.text.SimpleDateFormat;

/**
 * Created by Nico on 20.01.2017.
 */

public class PlayoffView extends RelativeLayout {
    private POMatchup matchup;
    private int[][] colIds = new int[3][7];
    private final int[] dateIds = {R.id.po_date_1, R.id.po_date_2, R.id.po_date_3, R.id.po_date_4, R.id.po_date_5, R.id.po_date_6, R.id.po_date_7};
    private int highlightedCol = 0;
    private int nextGame = 6;

    public PlayoffView(Context context) {
        super(context);
        inflate(context, R.layout.view_playoff, this);

        colIds[0][0] = R.id.po_date_1;
        colIds[1][0] = R.id.po_h_1;
        colIds[2][0] = R.id.po_a_1;

        colIds[0][1] = R.id.po_date_2;
        colIds[1][1] = R.id.po_h_2;
        colIds[2][1] = R.id.po_a_2;

        colIds[0][2] = R.id.po_date_3;
        colIds[1][2] = R.id.po_h_3;
        colIds[2][2] = R.id.po_a_3;

        colIds[0][3] = R.id.po_date_4;
        colIds[1][3] = R.id.po_h_4;
        colIds[2][3] = R.id.po_a_4;
        
        colIds[0][4] = R.id.po_date_5;
        colIds[1][4] = R.id.po_h_5;
        colIds[2][4] = R.id.po_a_5;

        colIds[0][5] = R.id.po_date_6;
        colIds[1][5] = R.id.po_h_6;
        colIds[2][5] = R.id.po_a_6;

        colIds[0][6] = R.id.po_date_7;
        colIds[1][6] = R.id.po_h_7;
        colIds[2][6] = R.id.po_a_7;
    }

    public PlayoffView(Context context, POMatchup matchup) {
        this(context);
        this.matchup = matchup;

        TextView txt_title = (TextView)findViewById(R.id.po_title);
        txt_title.setText(matchup.getTitle());

        TextView txt_team1 = (TextView)findViewById(R.id.po_team1);
        txt_team1.setText(matchup.getTeam1());

        TextView txt_team2 = (TextView)findViewById(R.id.po_team2);
        txt_team2.setText(matchup.getTeam2());

        if(matchup.getTeam1().equals("EHC Olten")){
            txt_team1.setTextColor(getResources().getColor(R.color.mainGreen));
            txt_team1.setTypeface(null, Typeface.BOLD);
        }
        if(matchup.getTeam2().equals("EHC Olten")){
            txt_team2.setTextColor(getResources().getColor(R.color.mainGreen));
            txt_team2.setTypeface(null, Typeface.BOLD);
        }

        extractDates();
        extractScores();
        changeGame(findViewById(dateIds[nextGame]));
    }

    private void extractDates() {
        int i = 0;
        for(Match m: matchup.getMatches()){
            TextView tv = (TextView) findViewById(dateIds[i++]);
            if(m != null && m.getDatetime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.\nMMM");
                tv.setText(sdf.format(m.getDatetime().getTime()));
            }
            else{
                tv.setText("tba");
            }
        }
    }

    private void extractScores(){
        int i = 0;
        ProgressBar pb_h = (ProgressBar)findViewById(R.id.playoff_home_progress);
        ProgressBar pb_a = (ProgressBar)findViewById(R.id.playoff_away_progress);
        pb_h.setProgress(0);
        pb_a.setProgress(0);

        for(Match m: matchup.getMatches()) {
            TextView tv = (TextView) findViewById(colIds[1][i]);
            TextView tv2 = (TextView) findViewById(colIds[2][i]);
            if(m != null) {
                boolean home = false;
                int score_h = m.getScores_home()[0] + m.getScores_home()[1] + m.getScores_home()[2] + m.getScores_home()[3];
                int score_a = m.getScores_away()[0] + m.getScores_away()[1] + m.getScores_away()[2] + m.getScores_away()[3];
                if (m.getHome_team().equals("EHC Olten")) {
                    tv.setText(String.valueOf(score_h));
                    tv2.setText(String.valueOf(score_a));
                    home = true;
                } else {
                    tv2.setText(String.valueOf(score_h));
                    tv.setText(String.valueOf(score_a));
                }

                if ((home && score_h > score_a) || (!home && score_h < score_a)) {
                    pb_h.setProgress(pb_h.getProgress() + 25);
                } else if (((home && score_a > score_h) || (!home && score_a < score_h))) {
                    pb_a.setProgress(pb_a.getProgress() + 25);
                }
                else if(m.getStatus().length() < 4){
                    tv.setText("-");
                    tv2.setText("-");
                }
                if (!m.getStatus().equals("Ende") && nextGame == 6) nextGame = i;
            }
            else{
                if(nextGame==6) nextGame = i;
                tv.setText("-");
                tv2.setText("-");
            }
            i++;
        }
    }

    private int getColumn(View view) {
        int col = -1;

        for(int i=0; i<3&& col == -1 ; i++){
            for(int j=0; j<7;j++){
                if(colIds[i][j] == view.getId()){
                    col = j;
                    break;
                }
            }
        }
        return col;
    }

    private void highlightCol(int id){
        for(int i=0;i<3;i++){
            findViewById(colIds[i][highlightedCol]).setBackgroundColor(0x00000000);
        }
        for(int i=0;i<3;i++){
            findViewById(colIds[i][id]).setBackgroundColor(getResources().getColor(R.color.lightGreen));
        }
        highlightedCol = id;
    }

    public void changeGame(View view) {
        highlightCol(getColumn(view));
        LinearLayout ll = (LinearLayout) findViewById(R.id.container_next_po_game);
        ll.removeAllViews();
        if (matchup.getMatch(highlightedCol) != null){
            ll.addView(new MatchView(getContext(), matchup.getMatch(highlightedCol), true));
        }
    }
}
