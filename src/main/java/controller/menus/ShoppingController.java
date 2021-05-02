package controller.menus;

import controller.ErrorChecker;
import models.Database;
import models.Player;
import models.cards.Card;
import view.Output;

import java.util.Arrays;
import java.util.Comparator;

public class ShoppingController {
    private ShoppingController() {
    }

    private static ShoppingController instance;

    public static ShoppingController getInstance() {
        if (instance == null)
            instance = new ShoppingController();
        return instance;
    }

    public void buyCard(Player player, String cardName) {
        Card card = Database.getInstance().getCardByName(cardName);
        if (card == null) {
            Output.getInstance().showMessage("there is no card with this name");
            return;
        }
        if (ErrorChecker.doseNotHaveEnoughMoney(player, card.getPrice())) {
            Output.getInstance().showMessage("not enough money");
            return;
        }

        player.addCardToAllPlayerCard(card);
        Output.getInstance().showMessage("Card purchased");
    }

    public void showAllCard() {
        Card[] sortedCards = Database.allCards.toArray(new Card[0]);
        Arrays.sort(sortedCards, Comparator.comparing(Card::getName));
        for (Card card : sortedCards) {
            System.out.println(card);
        }
    }


}
