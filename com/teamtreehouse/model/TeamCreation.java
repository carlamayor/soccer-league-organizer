package com.teamtreehouse.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class TeamCreation {
    private BufferedReader mReader;
    private HashMap<String, String> mMenu;
    private CollectionOfTeams mCollectionOfTeams;
    private List<Player> mPlayers;
    private Queue<Player> mWaitList;

    public TeamCreation(CollectionOfTeams collectionOfTeams) {
//Create a menu for the user
        mPlayers = new ArrayList<>(Arrays.asList(Players.load()));
        mReader = new BufferedReader(new InputStreamReader(System.in));
        mMenu = new LinkedHashMap<>();
        mCollectionOfTeams = collectionOfTeams;
        mWaitList = new ArrayDeque<>();
        mMenu.put("create", "Create new team.");
        mMenu.put("add", "Add player to a team");
        mMenu.put("remove", "Remove player from a team");
        mMenu.put("height report", "View Height report of players in a team");
        mMenu.put("balance report", "View League Balance report");
        mMenu.put("player rooster", "View a team rooster");
        mMenu.put("build", "To build teams automatically");
        mMenu.put("waitlist", "Add a player to the waiting list.");
        mMenu.put("quit", "Exit the program");
    }

    //Create the display of the options for the Map menu.
    private String promptAction() throws IOException {
        System.out.printf("%nWelcome. Your menu options are: %n%n");
        for (Map.Entry<String, String> option : mMenu.entrySet()) {
            System.out.printf("%s - %s %n",
                    option.getKey(),
                    option.getValue());
        }

        System.out.printf("%n%nWhat do you want to do:  %n");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    //Here we can receive/store users input for team creation
    private String promptCreateTeamName() throws IOException {
        System.out.println("Provide the name of your new team: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    //Here we can receive/store users input for teams coach
    private String promptCoachName() throws IOException {
        System.out.println("Provide the name of your coach: ");
        String choice = mReader.readLine();
        return choice.trim().toLowerCase();
    }

    //The method collects the inputs and creates a new team.
    private void newTeam() throws IOException {
        if (!mPlayers.isEmpty()) {
            String newTeamName = promptCreateTeamName();
            String newCoachName = promptCoachName();
            Team newTeam = new Team(newTeamName, newCoachName);
            mCollectionOfTeams.addTeam(newTeam);
            System.out.printf("Team: %s and coach: %s were added correctly.  %n", newTeamName, newCoachName);

        } else {
            System.out.println("Sorry there are no more players left, unable to create a new team.");
        }
    }

    // This method helps us select a team and if there is no teams to pick from returns null.
    private Team selectTeam() throws IOException {
        System.out.println("Select a number for the team of your choice from the following list: ");
        if (!mCollectionOfTeams.getTeams().isEmpty()) {
            int playerNumber = 1;
            for (Team team : mCollectionOfTeams.getTeams()) {
                System.out.println(playerNumber++ + ".) " + team.getTeamName());
            }
            System.out.println("Enter the team number of your choice: ");
            String optionChosen = mReader.readLine();
            int indexNumber = (Integer.parseInt(optionChosen) - 1);
            if (indexNumber < 0 || indexNumber >= mCollectionOfTeams.getTeams().size()) {
                System.out.println("Invalid team selection");
                return null;
            }
            return (Team) mCollectionOfTeams.getTeams().toArray()[indexNumber];
        } else {
            return null;
        }
    }

    //Here we can check if there are players in a team and if there are we can show the players to the user.
    private void showTeamPlayers(Team selectedTeam) throws IOException {
        int playersCounter = 1;
        if (selectedTeam.getPlayers().isEmpty()) {
            System.out.println("No players available to delete.");
            return;
        }
        Set<Player> playersByLastName = new TreeSet<>(selectedTeam.getPlayers());
        for (Player player : playersByLastName) {
            String name = player.getFirstName();
            String lastName = player.getLastName();
            int height = player.getHeightInInches();
            boolean experience = player.isPreviousExperience();
            System.out.printf("%d.) Name: %s %s/ %n Height in inch: %s %n Previous experience: %s %n",
                    playersCounter,
                    name,
                    lastName,
                    height,
                    experience);
            playersCounter++;

        }
    }

    /*
    This method helps us check that there are teams to be able to add players to a team.
    We also check that there are players available to add to the teams.
     */
    private void addPlayerToTeam() throws IOException {
        List<Player> availablePlayers = mPlayers;
        Team teamSelected = selectTeam();
        if (teamSelected != null) {
            if (availablePlayers.isEmpty()) {
                System.out.println("No players available to add.");
                return;
            }
            System.out.println("Select a player to add to the team from the following list: ");
            int playersCounter = 1;
            // Loops over the available players to display to the user their information.
            for (Player player : availablePlayers) {
                String name = player.getFirstName();
                String lastName = player.getLastName();
                int height = player.getHeightInInches();
                boolean experience = player.isPreviousExperience();
                System.out.printf("%d.) Name: %s %s/ %n Height in inch: %s %n Previous experience: %s %n",
                        playersCounter,
                        name,
                        lastName,
                        height,
                        experience);
                playersCounter++;
            }
            System.out.println("Select the player number to add to your new team:  ");
            String playerToAdd = mReader.readLine();
            int indexNumber = (Integer.parseInt(playerToAdd) - 1);
            //Check that user input (player chosen) exists and adds it to the team if it does.
            if (indexNumber >= 0 && indexNumber < availablePlayers.size()) {
                Player selectedPlayer = availablePlayers.get(indexNumber);
                if (teamSelected.addPlayer(selectedPlayer)) {
                    // We remove the player from the available list of players.
                    availablePlayers.remove(selectedPlayer);
                    System.out.printf("%n%n%nPlayer %s has been added to %s team. %n%n%n",
                            selectedPlayer.getFirstName(),
                            teamSelected.getTeamName());
                }

            } else {
                System.out.println("Whoops an invalid player number was provided.");
            }

        } else {
            System.out.println("There are no teams available, please create a team to add players to.");
        }
    }


    /*We check that there are teams to choose from ,and then players inside the chosen team to
    be able to remove a player.
     */
    private void removePlayer() throws IOException {
        Team teamSelected = selectTeam();
        if (teamSelected != null) {
            if (teamSelected.getPlayers().isEmpty()) {
                System.out.println("There are no players in this team. ");
            } else {
                showTeamPlayers(teamSelected);
                System.out.println("Choose number of the player you want to remove:   .");
                String choice = mReader.readLine();
                int playerNumberToDelete = Integer.parseInt(choice.trim());
                List<Player> players = teamSelected.getPlayers();
                int indexNumber = ((playerNumberToDelete) - 1);
                //Check and add the player to the team if input is valid.
                if (indexNumber >= 0 && indexNumber < players.size()) {
                    Player playerToDelete = players.get(indexNumber);
                    teamSelected.removePlayer(playerToDelete);
                    mPlayers.add(playerToDelete);
                    System.out.printf("Player %s has been removed from %s team. ",
                            playerToDelete.getFirstName(),
                            teamSelected.getTeamName());
                    replacePlayer(teamSelected);
                } else {
                    System.out.printf("Whoops an invalid player number was provided. %d is doesn't exist in %s ",
                            indexNumber,
                            teamSelected.getTeamName());
                }

            }
        } else {
            System.out.println("There are no teams created, please try deleting after creating some teams");
        }

    }
    private void replacePlayer(Team team) throws IOException {
        if (!mWaitList.isEmpty()) {
            System.out.println("Would you like to add next player in our waiting list? ");
            String answer = mReader.readLine();
            if (answer.equalsIgnoreCase("yes")) {
                Player player = mWaitList.poll();
                team.addPlayer(player);
                System.out.printf("The player: %s %s has been added to the team %s successfully ",
                        player.getFirstName(),
                        player.getLastName(),
                        team.getTeamName());
            }
        }
    }

    //Check there are teams available and players to be able to generate a players report by height.
    private void reportByHeight() throws IOException {
        Team selectedTeam = selectTeam();
        if (selectedTeam != null) {
            List<Player> players = selectedTeam.getPlayers();
            if (!players.isEmpty()) {
                //We create 3 empty lists to store the players that belong to its corresponding height category.
                List<Player> firstRange = new ArrayList<>();//35-40 inches
                List<Player> secondRange = new ArrayList<>();//41-46 inches
                List<Player> thirdRange = new ArrayList<>();//47-51 inches

                //Loop through players to sort them depending on their height.
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

                //Retrieve the data from the 3 lists we created and then display it to the user.
                System.out.println("Height players report of the team: " + selectedTeam.getTeamName());
                System.out.printf("%nThere is a total of %d players in the the 35-40 inches range:",
                        firstRange.size());
                for (Player player : firstRange) {
                    System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getHeightInInches(),
                            player.isPreviousExperience());
                }

                System.out.printf("%nThere is a total of %d players in the the 41-46 inches range:",
                        secondRange.size());
                for (Player player : secondRange) {
                    System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getHeightInInches(),
                            player.isPreviousExperience());
                }

                System.out.printf("%nThere is a total of %d players in the the 47-51 inches range:",
                        thirdRange.size());
                for (Player player : thirdRange) {
                    System.out.printf("Name: %s %s | Height in inches: %s | Experience: %s %n",
                            player.getFirstName(),
                            player.getLastName(),
                            player.getHeightInInches(),
                            player.isPreviousExperience());
                }
            } else {
                System.out.println("There are no players to display. ");
            }
        } else {
            System.out.println("There are no teams available, try again after adding some.");
        }
    }


    //We generate the balance report of all the teams to see how balanced they are.
    private void leagueBalanceReport() {
        Map<String, int[]> teamExperienceStats = new HashMap<>();
        if (mCollectionOfTeams.getTeams().isEmpty()) {
            System.out.println("No teams available, add teams and players to generate a report. ");
        } else {
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
                teamExperienceStats.put(team.getTeamName(), new int[]{experiencedPlayersCount, inexperiencedPlayersCount});
            }
            for (Map.Entry<String, int[]> entry : teamExperienceStats.entrySet()) {
                String teamName = entry.getKey();
                int[] totalCounts = entry.getValue();
                int experiencedCount = totalCounts[0];
                int inexperiencedCount = totalCounts[1];
                int totalPlayersCount = experiencedCount + inexperiencedCount;
                float experiencePercentage = ((float) experiencedCount / totalPlayersCount) * 100;
                float inexperiencePercentage = ((float) inexperiencedCount / totalPlayersCount) * 100;
                System.out.printf("%nTeam: %s | Experienced players: %d = %.2f%% | Inexperienced players: %d = %.2f%% %n",
                        teamName,
                        experiencedCount,
                        experiencePercentage,
                        inexperiencedCount,
                        inexperiencePercentage);

            }
        }

    }

    private void buildTeamsAutomatically() throws IOException {
        if(mCollectionOfTeams.getTeams().isEmpty()) {
            newTeam();
            newTeam();
            newTeam();
            List<Player> players = new ArrayList<>(mPlayers);
            players.sort(new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    return Boolean.compare(o1.isPreviousExperience(), o2.isPreviousExperience());
                }
            });
            int teamIndex = 0;
            List<Team> teams = new ArrayList<>(mCollectionOfTeams.getTeams());
            Iterator<Player> iterator = mPlayers.iterator();
            while (iterator.hasNext()){
                Player player = iterator.next();
                if( teams.get(teamIndex).addPlayer(player)) {
                    iterator.remove();
                }
                teamIndex = (teamIndex + 1) % 3;
            }



        }

    }

    private void addNewPlayer () throws IOException {
        System.out.println("To create a player please provide a first name:  ");
        String firstName = mReader.readLine().trim();
        System.out.println("Now please provide player's lastname:  ");
        String lastName = mReader.readLine().trim();
        System.out.println("Please provide player's height in inches:  ");
        int heightInches = Integer.parseInt(mReader.readLine().trim());
        System.out.println("Please write true or false to add player's experience:  ");
        boolean experience = Boolean.parseBoolean(mReader.readLine().trim());
        Player player = new Player(firstName, lastName, heightInches, experience);
        mWaitList.add(player);
        System.out.printf("%nPlayer: %s %s | Height in inches: %d | Experience: %b  " +
                        "was added to the waitlist successfully%n",
                firstName,
                lastName,
                heightInches,
                experience);
    }


    public void run() {
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch (choice) {
                    case "create":
                        newTeam();
                        break;
                    case "add":
                        addPlayerToTeam();
                        break;
                    case "remove":
                        removePlayer();
                        break;
                    case "build":
                        buildTeamsAutomatically();
                        break;
                    case "height report":
                        reportByHeight();
                        break;
                    case "balance report":
                        leagueBalanceReport();
                        break;
                    case "player rooster":
                        Team selectedTeam = selectTeam();
                        showTeamPlayers(selectedTeam);
                        break;
                    case "waitlist":
                        addNewPlayer ();
                        break;
                    case "quit":
                        System.out.println("See you later alligator :)");
                        break;
                    default:
                        System.out.printf("Unknown choice: '%s'. Try again.  %n%n%n",
                                choice);
                }


            } catch (IOException ioe) {
                System.out.println("Problem with input");
                ioe.printStackTrace();
            }

        } while (!choice.equals("quit"));
    }


}
