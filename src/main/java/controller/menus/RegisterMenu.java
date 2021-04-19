package controller.menus;

import controller.Database;
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
            Database.getInstance().writeUserJSON(new Player(username, password));
            response = "register successfully!";
        } else response = "a user exists with this username";

        Output.getInstance().showMessage(response);
    }

    public void login(String username, String password) {
        String response;
        if (ErrorChecker.isExistUsername(username)) {

            response = "login successfully!";
        } else response = "no user exists with this username";
    }
}
