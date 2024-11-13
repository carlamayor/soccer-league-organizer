package com.teamtreehouse.model;

import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TeamCreation {
    private BufferedReader mReader;
    private HashMap<String, String> mMenu;
    private CollectionOfTeams mCollectionOfTeams;
    private List<Player> mPlayers;

    public TeamCreation(CollectionOfTeams collectionOfTeams)
    {
        mPlayers = new ArrayList<>(Arrays.asList(Players.load()));
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mMenu = new HashMap<>();
        mCollectionOfTeams = collectionOfTeams;
        mMenu.put("create", "Create new team.");
        mMenu.put("add player", "Add player to a team");
        mMenu.put("remove player", "Remove player from a team");
        mMenu.put("height report", "View Height report of players in a team");
        mMenu.put("balance report", "View League Balance report");
        mMenu.put("queue", "Play next song in the queue");
        mMenu.put("quit", "Exit the program");
    }
    private String promptAction() throws IOException{
        System.out.printf("Welcome. Your menu options are: %n");
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
            System.out.printf("%s - %s %n",
                    option.getKey(),
                    option.getValue());
        }

        System.out.print("What do you want to do:  ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    private String promptCreateTeamName() throws IOException {
        System.out.println("Provide the name of your new team: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    private String promptCoachName() throws IOException {
        System.out.println("Provide the name of your coach: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }
    private void newTeam() throws IOException {
        String newTeamName = promptCreateTeamName();
        String newCoachName = promptCoachName();
        Team newTeam = new Team(newTeamName, newCoachName);
        mCollectionOfTeams.addTeam(newTeam);
        System.out.printf("Team: %s and coach: %s were added correctly.  %n", newTeamName, newCoachName);
    }

    private Team selectTeam() throws IOException {
        System.out.println("Select a number for the team of your choice from the following list: ");
        int playerNumber = 1;
        for (Team team: mCollectionOfTeams.getTeams()) {
            System.out.println(playerNumber++ + ".) " + team.getTeamName());
        }
        String optionChosen = mReader.readLine();
        int indexNumber = (Integer.parseInt(optionChosen) - 1);
        return (Team) mCollectionOfTeams.getTeams().toArray()[indexNumber];
    }

    private void showTeamPlayers(Team selectedTeam) throws IOException {
        int playersCounter = 1;
        for (Player player: selectedTeam.getPlayers()){
            String name = player.getFirstName();
            String lastName = player.getLastName();
            int height = player.getHeightInInches();
            boolean experience = player.isPreviousExperience();
            System.out.printf( "%d.) Name: %s %s/ %n Height in inch: %s %n Previous experience: %s",
                    playersCounter,
                    name,
                    lastName,
                    height,
                    experience);
            playersCounter++;

        }
    }

    private void addPlayerToTeam() throws IOException{
        Team teamSelected = selectTeam();
        System.out.println("Select a player to add to the team from the following list: ");
        int playersCounter = 1;
        List<Player> availablePlayers = mPlayers;
        for (Player player: teamSelected.getPlayers()){
            mPlayers.remove(player);
        }
        // Loop to print players info
        for(Player player: mPlayers) {
            String name = player.getFirstName();
            String lastName = player.getLastName();
            int height = player.getHeightInInches();
            boolean experience = player.isPreviousExperience();
            System.out.printf( "%d.) Name: %s %s/ %n Height in inch: %s %n Previous experience: %s",
                    playersCounter,
                    name,
                    lastName,
                    height,
                    experience);
            playersCounter++;
        }
        //Save user's input to obtain index of the player to add to the team.
        String playerToAdd = mReader.readLine();
        int indexNumber = (Integer.parseInt(playerToAdd) - 1);
        //Check and add the player to the team if input is valid.
        if(indexNumber >= 0 && indexNumber < availablePlayers.size()) {
            Player selectedPlayer =  availablePlayers.get(indexNumber);
            teamSelected.addPlayer(selectedPlayer);
            System.out.printf("Player %s has been added to %s team. ",
                    selectedPlayer.getFirstName(),
                    selectTeam().getTeamName());
        } else {
            System.out.println("Whoops an invalid player number was provided.");
        }
    }

    private void removePlayer() throws IOException {
        Team teamSelected = selectTeam();
        showTeamPlayers(teamSelected);
        System.out.println("Choose number of the player you want to remove:   ." );
        String choice = mReader.readLine();
        int playerNumberToDelete = Integer.parseInt(choice.trim());
        List<Player> players = teamSelected.getPlayers();
        int indexNumber = ((playerNumberToDelete) - 1);
        //Check and add the player to the team if input is valid.
        if(indexNumber >= 0 && indexNumber < players.size()) {
            Player playerToDelete =  players.get(indexNumber);
            teamSelected.removePlayer(playerToDelete);
            System.out.printf("Player %s has been removed from %s team. ",
                    playerToDelete.getFirstName(),
                    selectTeam().getTeamName());
        } else {
            System.out.printf("Whoops an invalid player number was provided. %d is doesn't exist in %s ",
                    indexNumber,
                    teamSelected.getTeamName());
        }

    }


    private void reportByHeight() throws IOException {
        Team selectedTeam = selectTeam();
        List<Player> players = selectedTeam.getPlayers();
        List<Player> firstRange = new ArrayList<>();
        List<Player> secondRange = new ArrayList<>();
        List<Player> thirdRange = new ArrayList<>();
        for (Player player : players) {
            int playerHeight = player.getHeightInInches();
            if (playerHeight >= 35 && playerHeight <= 40) {
                firstRange.add(player);
            } else if (playerHeight >= 41 && playerHeight <= 46) {
                secondRange.add(player);
            } else if (playerHeight >= 47 && playerHeight <= 51) {
                thirdRange.add(player);
            }
        }
        System.out.println("Height players report of the team: " + selectedTeam.getTeamName());
        System.out.println("35-40 inches: ");
        for (Player player : firstRange) {
            System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience());
        }

        System.out.println("41-46 inches: ");
        for (Player player : secondRange) {
            System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience());
        }

        System.out.println("47-51 inches: ");
        for (Player player : thirdRange) {
            System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                    player.getFirstName(),
                    player.getLastName(),
                    player.getHeightInInches(),
                    player.isPreviousExperience());
        }
    }

    private void leagueBalanceReport () {
        Map<String, int[]> teamExperienceStats = new HashMap<>();

        for (Team team : mCollectionOfTeams.getTeams()) {
            int experiencedPlayersCount = 0;
            int inexperiencedPlayersCount = 0;
            for (Player player : team.getPlayers()) {
                if (player.isPreviousExperience()) {
                    experiencedPlayersCount++;
                } else {
                    inexperiencedPlayersCount++;
                }
            }
            teamExperienceStats.put(team.getTeamName(),new int[]{experiencedPlayersCount,inexperiencedPlayersCount});
        }
        for(Map.Entry<String, int[]> entry: teamExperienceStats.entrySet()) {
            String teamName = entry.getKey();
            int[] totalCounts = entry.getValue();
            int experiencedCount = totalCounts[0];
            int inexperiencedCount = totalCounts[1];
            int totalPlayersCount = experiencedCount + inexperiencedCount;
            float experiencePercentage = ((float) experiencedCount / totalPlayersCount);
            float inexperiencePercentage = ((float) inexperiencedCount / totalPlayersCount);
            System.out.printf("Team: %s | Experienced players: %d = %f%% | Inexperienced players: %d = %f%%",
                    teamName,
                    experiencedCount,
                    experiencePercentage,
                    inexperiencedCount,
                    inexperiencePercentage);

        }
    }


//    public void run(){}


}
