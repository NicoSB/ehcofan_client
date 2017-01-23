package com.nicosb.apps.ehcofan.models;

import android.content.Context;

/**
 * Created by Nico on 22.01.2017.
 */

public class POMatchupWrapper extends POMatchup {
    private int g1;
    private int g2;
    private int g3;
    private int g4;
    private int g5;
    private int g6;
    private int g7;


    public POMatchupWrapper(String title, String team1, String team2, int[] matchIds, Context context) {
        super(title, team1, team2, matchIds, context);
    }

    public POMatchupWrapper(String title, String team1, String team2, int[] matchIds, Context context, int g1, int g2, int g3, int g4, int g5, int g6, int g7) {
        super(title, team1, team2, matchIds, context);
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
        this.g4 = g4;
        this.g5 = g5;
        this.g6 = g6;
        this.g7 = g7;
    }

    public void setG1(int g1) {
        this.g1 = g1;
    }

    public void setG2(int g2) {
        this.g2 = g2;
    }

    public void setG3(int g3) {
        this.g3 = g3;
    }

    public void setG4(int g4) {
        this.g4 = g4;
    }

    public void setG5(int g5) {
        this.g5 = g5;
    }

    public void setG6(int g6) {
        this.g6 = g6;
    }

    public void setG7(int g7) {
        this.g7 = g7;
    }

    public POMatchup toMatchup(Context context){
        int[] ints = {g1,g2,g3,g4,g5,g6,g7};
        return new POMatchup(getTitle(), getTeam1(), getTeam2(), ints, context);
    }
}
