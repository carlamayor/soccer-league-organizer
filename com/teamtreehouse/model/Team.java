package com.teamtreehouse.model;
import java.util.ArrayList;
import java.util.List;

public class Team implements Comparable<Team> {
    private String mTeamName;
    private String mCoachName;
    private List<Player> mPlayers;


    public Team (String teamName, String coachName){
        mTeamName = teamName;
        mCoachName = coachName;
        mPlayers = new ArrayList<>();
    }

    public String getTeamName() {
        return mTeamName;
    }

    public String getCoachName() {
        return mCoachName;
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }
    public void removePlayer(Player player) {
        mPlayers.remove(player);

    }
    public boolean addPlayer(Player player) {
        if(mPlayers.size() < 11) {
            mPlayers.add(player);
            return true;
        } else {
            System.out.println("This team is full, this player can't be added.");
            return false;
        }
    }



    @Override
    public int compareTo(Team other) {
        return mTeamName.compareTo(other.getTeamName());
    }
}
