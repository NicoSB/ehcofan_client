package com.nicosb.apps.ehcofan.models;

/**
 * Created by Nico on 24.10.2016.
 */

public class TeamWrapper extends StandingsTeam {
    private boolean active;

    public TeamWrapper(int id, String name, String competition, String group, int wins, int ot_wins, int ot_losses, int losses, int goals_for, int goals_against) {
        super(id, name, competition, group, wins, ot_wins, ot_losses, losses, goals_for, goals_against);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
