package controller.menus;

import controller.Duel;
import controller.ErrorChecker;
import models.Database;
import models.Player;
import view.GameInputs;
import view.Output;

public class DuelMenuController {
    private DuelMenuController() {
    }

    private static DuelMenuController instance;

    public static DuelMenuController getInstance() {
        if (instance == null)
            instance = new DuelMenuController();
        return instance;
    }

    public void startGame(String firstUsername, String secondUsername, String round) throws CloneNotSupportedException {
        Player firstPlayer = Database.getInstance().getPlayerByUsername(firstUsername);
        Player secondPlayer = Database.getInstance().getPlayerByUsername(secondUsername);
        if (!ErrorChecker.doesUsernameExist(secondUsername)) {
            Output.getInstance().showMessage("there are no player with this username");
            return;
        }
        if (firstPlayer.getActiveDeck() == null) {
            Output.getInstance().showMessage(firstUsername + " has no active deck");
            return;
        }
        if (secondPlayer.getActiveDeck() == null) {
            Output.getInstance().showMessage(secondUsername + " has no active deck");
            return;
        }
        if (!ErrorChecker.isDeckAllowed(firstPlayer.getActiveDeck())) {
            Output.getInstance().showMessage(firstUsername + "'s deck is invalid");
            return;
        }
        if (!ErrorChecker.isDeckAllowed(secondPlayer.getActiveDeck())) {
            Output.getInstance().showMessage(secondUsername + "'s deck is invalid");
            return;
        }
        if (!round.equals("1") && !round.equals("3")) {
            Output.getInstance().showMessage("number of rounds is not supported");
            return;
        }

        int numberOfRound = Integer.parseInt(round);
        GameInputs.getInstance().setOnlineDuel(new Duel(firstPlayer, secondPlayer, numberOfRound));
        GameInputs.getInstance().runGamePlay();
    }

}
