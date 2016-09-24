package com.nicosb.apps.ehcofan.models;

import android.database.Cursor;

/**
 * Created by Nico on 17.08.2016.
 */
public class Team {
    int _id;
    String name;
    String league;
    String country;
    String last_season;
    int founded;
    int title_count;
    String topscorer;
    String desc_text;
    String website;

    public Team(int _id, String name, String league, String country, String last_season, int founded, int title_count, String topscorer, String desc_text, String website) {
        this._id = _id;
        this.name = name;
        this.league = league;
        this.country = country;
        this.last_season = last_season;
        this.founded = founded;
        this.title_count = title_count;
        this.topscorer = topscorer;
        this.desc_text = desc_text;
        this.website = website;
    }

    public static Team populateTeam(Cursor c) {
        return new Team(c.getInt(c.getColumnIndex("_id" )),
                c.getString(c.getColumnIndex("name" )),
                c.getString(c.getColumnIndex("league" )),
                c.getString(c.getColumnIndex("Country" )),
                c.getString(c.getColumnIndex("last_season" )),
                c.getInt(c.getColumnIndex("founded" )),
                c.getInt(c.getColumnIndex("title_count" )),
                c.getString(c.getColumnIndex("topscorer" )),
                c.getString(c.getColumnIndex("desc_text" )),
                c.getString(c.getColumnIndex("website_url" )));
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLast_season() {
        return last_season;
    }

    public void setLast_season(String last_season) {
        this.last_season = last_season;
    }

    public int getFounded() {
        return founded;
    }

    public void setFounded(int founded) {
        this.founded = founded;
    }

    public int getTitle_count() {
        return title_count;
    }

    public void setTitle_count(int title_count) {
        this.title_count = title_count;
    }

    public String getTopscorer() {
        return topscorer;
    }

    public void setTopscorer(String topscorer) {
        this.topscorer = topscorer;
    }

    public String getDesc_text() {
        return desc_text;
    }

    public void setDesc_text(String desc_text) {
        this.desc_text = desc_text;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
