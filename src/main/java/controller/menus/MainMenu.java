package controller.menus;

import controller.FileWorker;
import models.Player;

public class MainMenu {
    Player playerLoggedIn;

    private MainMenu(Player playerLoggedIn) {
        this.playerLoggedIn = playerLoggedIn;
    }

    private static MainMenu instance;

    public static MainMenu getInstance(Player playerLoggedIn) {
        if (instance == null)
            instance = new MainMenu(playerLoggedIn);
        return instance;
    }


}
