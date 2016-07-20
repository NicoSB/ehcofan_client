package com.nicosb.apps.ehcofan.models;

import java.util.Calendar;

/**
 * Created by Nico on 20.07.2016.
 */
public class Match {
    private String home_team;
    private String away_team;
    private String competition;
    private Calendar datetime;
    private int scores_home[];
    private int scores_away[];

    public Match(String home_team, String away_team, String competition, Calendar datetime, int[] scores_home, int[] scores_away) {
        this.home_team = home_team;
        this.away_team = away_team;
        this.competition = competition;
        this.datetime = datetime;
        this.scores_home = scores_home;
        this.scores_away = scores_away;
    }

    public String getHome_team() {
        return home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public String getCompetition() {
        return competition;
    }

    public Calendar getDatetime() {
        return datetime;
    }

    public int[] getScores_home() {
        return scores_home;
    }

    public int[] getScores_away() {
        return scores_away;
    }
    
    public int getTotal(Team team){
        switch(team){
            case HOME:
                return scores_home[0] + scores_home[1] + scores_home[2] + scores_home[3];
            case AWAY:
                return scores_away[0] + scores_away[1] + scores_away[2] + scores_away[3];
            default:
                return -1;
        }
        
    }
    
    public enum Team{
        HOME,
        AWAY
    }
}
