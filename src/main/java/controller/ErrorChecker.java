package controller;

import controller.menus.MainMenu;
import models.Database;
import models.Deck;
import models.Player;
import models.cards.Card;
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
        Deck deck = Database.getInstance().getDeckByName(name);
        if (deck == null)
            return true;

            Output.getInstance().showMessage("deck with name " + name + " already exists");
            return false;

    }
    public static boolean doesDeckExist(String name) {
        Deck deck = Database.getInstance().getDeckByName(name);
        if (deck != null)
            return true;
        Output.getInstance().showMessage("deck with name " + name + " does not exist");
        return false;

    }
    public static boolean doesDeckBelongToPlayer(Deck deck , Player player) {
        if (deck.getOwner().getUsername().equals(player.getUsername()))
            return true;
        Output.getInstance().showMessage("this deck doesn't belong to you!");
        return false;
    }

    public static boolean doesCardExist(String cardName) {
        Card card = Database.getInstance().getCardByName(cardName);
        if (card != null)
            return true;
        Output.getInstance().showMessage("card with name " + cardName + " does not exist");
        return false;
    }

    public static boolean doesDeckHaveSpace(Deck deck) {
        if (deck == null) return  false;
        if (deck.getMainCards().size() < 60)
            return true;
        Output.getInstance().showMessage("main deck is full!");
        return false;
    }
    public static boolean doesSideDeckHaveSpace(Deck deck) {
        if (deck == null) return  false;
        if (deck.getSideCards().size() < 15)
            return true;
        Output.getInstance().showMessage("side deck is full!");
        return false;
    }

    public static boolean isNumberOfCardsInDeckLessThanFour(Deck deck, Card card) {
        if (deck.getNumberOfCardsInDeck(card) < 4)
            return true;
        Output.getInstance().showMessage("there are already three cards with name " + card.getName() +
                " in deck " + deck.getName() + " !");
        return false;
    }
}
