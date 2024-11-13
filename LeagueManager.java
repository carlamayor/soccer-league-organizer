import com.teamtreehouse.model.CollectionOfTeams;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.TeamCreation;

public class LeagueManager {

  public static void main(String[] args) {
    Player[] players = Players.load();
    System.out.printf("There are currently %d registered players.%n", players.length);
    // Your code here!
    CollectionOfTeams collectionOfTeams = new CollectionOfTeams();
    TeamCreation teamCreation = new TeamCreation(collectionOfTeams);
    teamCreation.run();
  }

}
