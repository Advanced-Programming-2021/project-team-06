package controller;

import models.Database;
import models.Player;

public class ErrorChecker {

    public static boolean isExistUsername(String username) {
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (player == null) return false;
        return true;
    }
    public static boolean isPasswordCorrect(Player player , String password){
        if(player.getPassword().equals(password))
            return true;
        return false;
    }
}
