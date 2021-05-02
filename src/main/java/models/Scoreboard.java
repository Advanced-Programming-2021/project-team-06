package models;

import view.Output;
import java.util.ArrayList;


public class Scoreboard {

    private Scoreboard() {
    }

    private static Scoreboard instance;

    public static Scoreboard getInstance() {
        if (instance == null)
            instance = new Scoreboard();
        return instance;
    }

    public void showScoreboard(){
        int counter = 1;
        StringBuilder output = new StringBuilder();
        ArrayList<Player> allUsers = Database.allPlayers;
        sortScoreboard(allUsers);
        for (Player player : allUsers)
            output.append(counter).append("-").append(player.getNickname()).append(": ").append(player.getScore()).append("\n");

        Output.getInstance().showMessage(output.toString());
    }

    public void sortScoreboard(ArrayList<Player> allUsers){
        for (int i = 0; i < allUsers.size(); i++) {
            for (int j = 0; j < allUsers.size() - i - 1; j++) {
                Player user1 = allUsers.get(j);
                Player user2 = allUsers.get(j + 1);
                if (user1.getScore() > user2.getScore())
                    swap(allUsers, j, j + 1);
            }
        }
    }

    private void swap(ArrayList<Player> allUsers,int i, int j) {
        Player player = allUsers.get(i);
        allUsers.set(i, allUsers.get(j));
        allUsers.set(j, player);
    }
}
