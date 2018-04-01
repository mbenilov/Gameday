package com.benilov.gameday.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A Singleton class which contains the users tracked teams
 */

public class TeamTracker {

    private List<Team> trackingList = null;

    private static TeamTracker instance;

    private TeamTracker() {
        trackingList = new ArrayList<Team>();
    }

    public static TeamTracker getInstance() {
        if (instance == null) {
            instance = new TeamTracker();
        }
        return instance;
    }

    public void followTeam(Team team) {
        if (trackingList.contains(team)) {
            throw new IllegalArgumentException("This team is already being tracked");
        } else {
            trackingList.add(team);
        }
    }

    public void unfollowTeam(Team team) {
        if (!trackingList.contains(team)) {
            throw new IllegalArgumentException("This team is not currently being tracked.");
        } else {
            trackingList.remove(team);
        }
    }

    public List<Team> getTrackedTeams() {
        return trackingList;
    }

}
