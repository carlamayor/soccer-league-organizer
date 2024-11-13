package com.teamtreehouse.model;
import java.util.Set;
import java.util.TreeSet;


public class CollectionOfTeams {
    private Set<Team> mTeams;

    public CollectionOfTeams(){
        mTeams = new TreeSet<>();
    }

    public Set<Team> getTeams() {
        return mTeams;
    }

    public void addTeam(Team team){
        if(!mTeams.contains(team)){
            mTeams.add(team);
            System.out.println("Team was successfully added.");
        }else {
            System.out.println("Team already exists.");
        }
    }

}
