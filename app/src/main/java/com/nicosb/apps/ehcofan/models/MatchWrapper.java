package com.nicosb.apps.ehcofan.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nico on 20.07.2016.
 */
public class MatchWrapper {
    private String home_team;
    private String away_team;
    private String competition;
    private String datetime;
    private int a1;
    private int a2;
    private int a3;
    private int a_ot;
    private int h1;
    private int h2;
    private int h3;
    private int h_ot;
    
    public Match toMatch(){
        int scores_home[] = {h1,h2,h3,h_ot};
        int scores_away[] = {a1,a2,a3,a_ot};
        // TODO: parse date
        GregorianCalendar calendar = new GregorianCalendar();
        return new Match(home_team, away_team, competition, calendar, scores_home, scores_away);
    }
}
