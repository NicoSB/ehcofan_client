package com.nicosb.apps.ehcofan.models;

/**
 * Created by Nico on 27.07.2016.
 */
public class StandingsTeam {
    private String name;
    private String competition;
    private String group;
    private int wins;
    private int ot_wins;
    private int ot_losses;
    private int losses;
    private int goals_for;
    private int goals_against;

    public StandingsTeam(String name, String competition, String group, int wins, int ot_wins, int ot_losses, int losses, int goals_for, int goals_against) {
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

    public int getGames(){
        return wins + ot_wins + ot_losses + losses;
    }

    public int getPoints(){
        return 3*wins + 2*ot_wins + ot_losses;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
