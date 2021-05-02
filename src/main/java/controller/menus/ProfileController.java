package controller.menus;

import controller.ErrorChecker;
import models.Player;
import view.Output;

public class ProfileController {

    private ProfileController() {
    }

    private static ProfileController instance;

    public static ProfileController getInstance() {
        if (instance == null)
            instance = new ProfileController();
        return instance;
    }

    public void changeNickname(Player player, String nickName) {
        if (ErrorChecker.doesNicknameExist(nickName)) {
            Output.getInstance().showMessage("user with this nickname " + nickName + " is already exists");
            return;
        }
        player.setNickname(nickName);
        Output.getInstance().showMessage("nickname changed successfully!");
    }

    public void changePassword(Player player, String oldPassword, String newPassword) {
        if (!ErrorChecker.isPasswordCorrect(player, oldPassword)) {
            Output.getInstance().showMessage("current password is invalid");
            return;
        }
        if (ErrorChecker.doesOldPassEqualsNewPass(oldPassword, newPassword)) {
            Output.getInstance().showMessage("please enter a new password");
            return;
        }

        player.setPassword(newPassword);
        Output.getInstance().showMessage("password changed successfully!");
    }
}
