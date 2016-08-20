package com.nicosb.apps.ehcofan.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.nicosb.apps.ehcofan.CacheDBHelper;
import com.nicosb.apps.ehcofan.CacheDBHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Nico on 20.07.2016.
 */
public class Match {
    private int id;
    private String home_team;
    private String away_team;
    private String competition;
    private Calendar datetime;
    private int scores_home[];
    private int scores_away[];

    public Match(int id, String home_team, String away_team, String competition, Calendar datetime, int[] scores_home, int[] scores_away) {
        this.id = id;
        this.home_team = home_team;
        this.away_team = away_team;
        this.competition = competition;
        this.datetime = datetime;
        this.scores_home = scores_home;
        this.scores_away = scores_away;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dt_text = sdf.format(datetime.getTime());

        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_ID, id);
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM, home_team);
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM, away_team);
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION, competition);
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME, dt_text);
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_SCORES_HOME, CacheDBHelper.encodeScores(scores_home));
        contentValues.put(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_SCORES_AWAY, CacheDBHelper.encodeScores(scores_away));

        return contentValues;
    }

    public static Match populateMatch(Cursor c) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar datetime = Calendar.getInstance();
        datetime.setTime(sdf.parse(c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME))));

        int[] scores_home = CacheDBHelper.decodeScores(c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_SCORES_HOME)));
        int[] scores_away = CacheDBHelper.decodeScores(c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_SCORES_AWAY)));
        return new Match(c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_ID)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_HOME_TEAM)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_AWAY_TEAM)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_COMPETITION)),
                datetime,
                scores_home,
                scores_away
                );
    }
}
