package controller;

import controller.menus.MainMenu;
import models.Database;
import models.Deck;
import models.Player;
import view.Output;

public class ErrorChecker {

    public static boolean doesUsernameExist(String username) {
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (player == null) return false;
        return true;
    }

    public static boolean doesNicknameExist(String nickname) {
        Player player = Database.getInstance().getPlayerByNickname(nickname);
        if (player == null) return false;
        return true;
    }

    public static boolean isPasswordCorrect(Player player, String password) {
        if (player.getPassword().equals(password))
            return true;
        return false;
    }

    public static boolean doesOldPassEqualsNewPass(String oldPass, String newPass) {
        if (oldPass.equals(newPass))
            return true;
        return false;
    }

    public static boolean isUserLoggedIn() {
        if (MainMenu.getInstance().getPlayerLoggedIn() == null)
            return false;
        return true;
    }

    public static boolean doseNotHaveEnoughMoney(Player player, int price) {
        if (player.getMoney() < price)
            return true;
        return false;
    }

    public static boolean isDeckNameUnique(String name) {
        Deck deck = Deck.getDeckByName(name);
        if (deck == null)
            return true;
        else {
            Output.getInstance().showMessage("deck with name " + name + " already exists");
            return false;
        }
    }
}
