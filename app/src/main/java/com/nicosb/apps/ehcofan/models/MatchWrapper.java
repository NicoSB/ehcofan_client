package com.nicosb.apps.ehcofan.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Created by Nico on 20.07.2016.
 */
public class MatchWrapper {
    private int id;
    private String home_team;
    private String away_team;
    private String competition;
    private String datetime;
    private String updated_at;
    private int a1;
    private int a2;
    private int a3;
    private int a_ot;
    private int h1;
    private int h2;
    private int h3;
    private int h_ot;
    private boolean active;
    private String status;

    public Match toMatch() {
        int scores_home[] = {h1, h2, h3, h_ot};
        int scores_away[] = {a1, a2, a3, a_ot};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS" );
        GregorianCalendar calendar = new GregorianCalendar();
        try {
            calendar.setTime(sdf.parse(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Match(id, home_team, away_team, competition, calendar, scores_home, scores_away, status);
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isActive() {
        return active;
    }

    public int getId() {
        return id;
    }
}
