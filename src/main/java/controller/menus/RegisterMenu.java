package controller.menus;

import controller.FileWorker;
import models.Database;
import controller.ErrorChecker;
import models.Player;
import view.Output;

public class RegisterMenu {
    private String username;
    private String password;

    private RegisterMenu() {
    }

    private static RegisterMenu instance;

    public static RegisterMenu getInstance() {
        if (instance == null)
            instance = new RegisterMenu();
        return instance;
    }

    public void register(String username, String password) {
        String response;
        if (!ErrorChecker.isExistUsername(username)) {
            new Player(username, password);
            response = "register successfully!";
        } else response = "a user exists with this username";

        Output.getInstance().showMessage(response);
    }

    public void login(String username, String password) {

        if (!ErrorChecker.isExistUsername(username)) {
            Output.getInstance().showMessage("no user exists with this username");
            return;
        }
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (!ErrorChecker.isPasswordCorrect(player, password)) {
            Output.getInstance().showMessage("password is wrong");
            return;
        }
        MainMenu.getInstance(player);
    }
}
