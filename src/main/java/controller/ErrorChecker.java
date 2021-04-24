package controller;

import controller.menus.MainMenu;
import models.Database;
import models.Player;

public class ErrorChecker {

    public static boolean isExistUsername(String username) {
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (player == null) return false;
        return true;
    }
    public static boolean isExistNickname(String nickname) {
        Player player = Database.getInstance().getPlayerByNickname(nickname);
        if(player == null) return false;
        return true;
    }
    public static boolean isPasswordCorrect(Player player , String password){
        if(player.getPassword().equals(password))
            return true;
        return false;
    }
    public static boolean isUserLoggedIn(){
        if(MainMenu.getInstance().getPlayerLoggedIn()==null)
            return false;
        return true;
    }
}
