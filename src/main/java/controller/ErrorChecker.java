package controller;

import models.Player;

public class ErrorChecker {

    public static boolean isExistUsername(String username) {
        Player player = Database.getInstance().readPlayerJSON(username);
        if (player == null) return false;
        return true;
    }
}
