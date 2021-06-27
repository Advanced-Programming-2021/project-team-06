package controller.menus;

import models.Database;
import controller.ErrorChecker;
import models.Player;
import view.graphics.Prompt;
import view.graphics.PromptType;

public class RegisterMenuController {

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
            Prompt.showMessage("user with username " + username + " already exists", PromptType.Error);
            return;
        }
        if (ErrorChecker.doesNicknameExist(nickname)) {
            Prompt.showMessage("user with nickname " + nickname + " already exists", PromptType.Error);
            return;
        }

        new Player(username, nickname, password);
        Prompt.showMessage("user created successfully!", PromptType.Success);
    }

    public void login(String username, String password) {

        if (!ErrorChecker.doesUsernameExist(username)) {
            Prompt.showMessage("username and password didn't match!", PromptType.Error);
            return;
        }
        Player player = Database.getInstance().getPlayerByUsername(username);
        if (!ErrorChecker.isPasswordCorrect(player, password)) {
            Prompt.showMessage("username and password didn't match!", PromptType.Error);
            return;
        }

        MainMenuController.getInstance().setPlayerLoggedIn(player);
        Prompt.showMessage("user loggedIn successfully!", PromptType.Success);
    }
}
