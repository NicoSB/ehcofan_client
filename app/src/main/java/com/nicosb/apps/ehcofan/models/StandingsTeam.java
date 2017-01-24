package com.nicosb.apps.ehcofan.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.nicosb.apps.ehcofan.CacheDBHelper;

/**
 * Created by Nico on 27.07.2016.
 */
public class StandingsTeam {
    private int id;
    private String name;
    private String competition;
    private String group;
    private int wins;
    private int ot_wins;
    private int ot_losses;
    private int losses;
    private int goals_for;
    private int goals_against;

    StandingsTeam(int id, String name, String competition, String group, int wins, int ot_wins, int ot_losses, int losses, int goals_for, int goals_against) {
        this.id = id;
        this.name = name;
        this.competition = competition;
        this.group = group;
        this.wins = wins;
        this.ot_wins = ot_wins;
        this.ot_losses = ot_losses;
        this.losses = losses;
        this.goals_for = goals_for;
        this.goals_against = goals_against;
    }

    public static StandingsTeam populateStandingsTeam(Cursor c) {
        return new StandingsTeam(
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_ID)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_NAME)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_COMPETITION)),
                c.getString(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GROUP)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_WINS)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_OT_WINS)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_OT_LOSSES)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_LOSSES)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GOALS_FOR)),
                c.getInt(c.getColumnIndex(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GOALS_AGAINST))
        );
    }

    public String getName() {
        return name;
    }

    public String getCompetition() {
        return competition;
    }

    public int getWins() {
        return wins;
    }

    public int getOt_wins() {
        return ot_wins;
    }

    public int getOt_losses() {
        return ot_losses;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoals_for() {
        return goals_for;
    }

    public int getGoals_against() {
        return goals_against;
    }

    public int getGames() {
        return wins + ot_wins + ot_losses + losses;
    }

    public int getPoints() {
        return 3 * wins + 2 * ot_wins + ot_losses;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_ID, id);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_NAME, name);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_COMPETITION, competition);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GROUP, group);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_WINS, wins);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_OT_WINS, ot_wins);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_OT_LOSSES, ot_losses);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_LOSSES, losses);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GOALS_FOR, goals_for);
        contentValues.put(CacheDBHelper.TableColumns.STANDINGSTEAMS_COLUMN_NAME_GOALS_AGAINST, goals_against);

        return contentValues;
    }
}
