package controller.menus;

import controller.Duel;
import controller.ErrorChecker;
import models.Database;
import models.Player;
import view.GameInputs;
import view.Output;

import java.lang.reflect.InvocationTargetException;

public class DuelMenuController {
    private DuelMenuController() {
    }

    private static DuelMenuController instance;

    public static DuelMenuController getInstance() {
        if (instance == null)
            instance = new DuelMenuController();
        return instance;
    }

    public void startGame(String firstUsername, String secondUsername, String round) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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
        Duel duel;
        int numberOfWinPlayer1 = 0;
        int numberOfWinPlayer2 = 0;
        for (int i = 1; i <= numberOfRound; i++) {
            GameInputs.getInstance().setOnlineDuel(duel = new Duel(firstPlayer, secondPlayer));
            GameInputs.getInstance().runGamePlay();
            if (duel.getWinner().getUsername().equals(firstUsername)) numberOfWinPlayer1++;
            else numberOfWinPlayer2++;
            if (numberOfWinPlayer1 == 2 || numberOfWinPlayer2 == 1) break;
        }
        if (numberOfWinPlayer1 > numberOfWinPlayer2)
            Output.getInstance().showMessage(firstUsername + "won the whole match with score: " +
                    firstPlayer.getScore() + "-" + secondPlayer.getScore());
        else
            Output.getInstance().showMessage(secondUsername + "won the whole match with score: " +
                    secondPlayer.getScore() + "-" + firstPlayer.getScore());

    }

}
