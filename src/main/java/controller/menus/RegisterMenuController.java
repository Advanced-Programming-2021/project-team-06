package controller.menus;

import models.Database;
import controller.ErrorChecker;
import models.Player;
import view.Output;

public class RegisterMenuController {
    private String username;
    private String password;

    private RegisterMenuController() {
    }

    private static RegisterMenuController instance;

    public static RegisterMenuController getInstance() {
        if (instance == null)
            instance = new RegisterMenuController();
        return instance;
    }

    public void createUser(String username, String nickname, String password) {
        if (ErrorChecker.doesUsernameExist(username)) {
            Output.getInstance().showMessage("user with username " + username + " already exists");
            return;
        }
        if (ErrorChecker.doesNicknameExist(nickname)){
            Output.getInstance().showMessage("user with nickname " + nickname + " already exists");
            return;
        }

        new Player(username,nickname,password);
        Output.getInstance().showMessage("user created successfully!");
    }

    public void login(String username, String password) {

        if (!ErrorChecker.doesUsernameExist(username)) {
            Output.getInstance().showMessage("username and password didn't match!");
            return;
        }
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (!ErrorChecker.isPasswordCorrect(player, password)) {
            Output.getInstance().showMessage("username and password didn't match!");
            return;
        }

        MainMenu.getInstance().setPlayerLoggedIn(player);
        Output.getInstance().showMessage("user loggedIn successfully!");
    }
}
