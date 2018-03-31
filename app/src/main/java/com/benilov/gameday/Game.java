package com.benilov.gameday;

/**
 * POJO to store scheduled NBA game
 */

public class Game {

    private String gameId;
    private String startDateEastern;
    private String startTimeEastern;
    private String hTeam;
    private String vTeam;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getStartDateEastern() {
        return startDateEastern;
    }

    public void setStartDateEastern(String startDateEastern) {
        this.startDateEastern = startDateEastern;
    }

    public String getStartTimeEastern() {
        return startTimeEastern;
    }

    public void setStartTimeEastern(String startTimeEastern) {
        this.startTimeEastern = startTimeEastern;
    }

    public String gethTeam() {
        return hTeam;
    }

    public void sethTeam(String hTeam) {
        this.hTeam = hTeam;
    }

    public String getvTeam() {
        return vTeam;
    }

    public void setvTeam(String vTeam) {
        this.vTeam = vTeam;
    }

    public Game() {
    }

    @Override
    public String toString() {
        return (getStartDateEastern() + " @ " + getStartTimeEastern());
    }
}
