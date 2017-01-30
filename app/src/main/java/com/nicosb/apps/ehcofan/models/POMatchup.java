package com.nicosb.apps.ehcofan.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nicosb.apps.ehcofan.CacheDBHelper;

import java.text.ParseException;

/**
 * Created by Nico on 20.01.2017.
 */
public class POMatchup {
    private Match[] matches = new Match[7];
    private int[][] scores = new int[7][2];
    private int[] matchIds;
    private Context context;
    private String team1;
    private String team2;
    private String title;

    POMatchup(String title, String team1, String team2, int[] matchIds, Context context){
        this.title = title;
        this.team1 = team1;
        this.team2 = team2;
        this.matchIds = matchIds;
        this.context = context;
    }

    public Match getMatch(int id){
        if(id < matches.length) {
            return matches[id];
        }
        else{
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public Match[] getMatches() {
        fillMatches();
        return matches;
    }

    private void fillMatches(){
        SQLiteDatabase db = CacheDBHelper.getReadableDB(context);

        String query =  "SELECT * FROM " + CacheDBHelper.TableColumns.MATCHES_TABLE_NAME + " WHERE " + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_ID + " IN (";
        for(int id: matchIds){
            query = query + id + ",";
        }
        query = query.substring(0,query.length() - 1);
        query = query + ") ORDER by " + CacheDBHelper.TableColumns.MATCHES_COLUMN_NAME_DATETIME + " ASC";

        Cursor c = db.rawQuery(query, null);

        int i = 0;
        while (c.moveToNext()) {
            try {
                Match m = Match.populateMatch(c);
                matches[i++] = m;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        c.close();
        db.close();
    }
}
