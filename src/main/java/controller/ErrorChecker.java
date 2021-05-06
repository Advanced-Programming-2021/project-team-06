package controller;

import controller.menus.MainMenu;
import models.Board;
import models.Database;
import models.Deck;
import models.Player;
import models.cards.Card;
import models.cards.CardPlacement;
import models.cards.Monster;
import models.cards.MonsterMode;
import view.Output;

import java.util.ArrayList;

public class ErrorChecker {

    public static boolean doesUsernameExist(String username) {
        Player player = Database.getInstance().getPlayerByUsername(username);
        return player != null;
    }

    public static boolean doesNicknameExist(String nickname) {
        Player player = Database.getInstance().getPlayerByNickname(nickname);
        return player != null;
    }

    public static boolean isPasswordCorrect(Player player, String password) {
        return player.getPassword().equals(password);
    }

    public static boolean doesOldPassEqualsNewPass(String oldPass, String newPass) {
        return oldPass.equals(newPass);
    }

    public static boolean isUserLoggedIn() {
        return MainMenu.getInstance().getPlayerLoggedIn() != null;
    }

    public static boolean doseNotHaveEnoughMoney(Player player, int price) {
        return player.getMoney() < price;
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

    public static boolean doesDeckBelongToPlayer(Deck deck, Player player) {
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
        if (deck == null) return false;
        if (deck.getMainCards().size() < 60)
            return true;
        Output.getInstance().showMessage("main deck is full!");
        return false;
    }

    public static boolean doesSideDeckHaveSpace(Deck deck) {
        if (deck == null) return false;
        if (deck.getSideCards().size() < 15)
            return true;
        Output.getInstance().showMessage("side deck is full!");
        return false;
    }

    public static boolean isNumberOfCardsInDeckLessThanFour(Deck deck, Card card) {
        if (deck.getNumberOfCardsInDeck(card) < 3)
            return true;
        Output.getInstance().showMessage("there are already three cards with name " + card.getName() +
                " in deck " + deck.getName() + " !");
        return false;
    }

    public static boolean isDeckAllowed(Deck deck) {
        int numberOfCardsInSideDeck = deck.getNumberOfCardsInSideDeck();
        int numberOfCardsInMainDeck = deck.getNumberOfCardsInMainDeck();
        return numberOfCardsInMainDeck <= 60 && numberOfCardsInMainDeck >= 40 && numberOfCardsInSideDeck <= 15;
    }

    public static boolean isValidAddress(String address, String state) {
        if (!state.equals("h") && !(address.equals("1") || address.equals("2") || address.equals("3")
                || address.equals("4") || address.equals("5"))) {
            Output.getInstance().showMessage("invalid selection");
            return false;
        }
        if (state.equals("h") && !(address.equals("1") || address.equals("2") || address.equals("3")
                || address.equals("4") || address.equals("5") || address.equals("6"))) {
            Output.getInstance().showMessage("invalid selection");
            return false;
        }
        return true;
    }


    public static Card istTheSeatVacant(Board board, int address, String state) {
        Card card = null;
        if (state.equals("h")) card = board.getHandZoneCards().get(address);
        if (state.equals("m")) card = board.getMonsterZoneCards().get(address);
        if (state.equals("s")) card = board.getSpellZoneCards().get(address);
        if (state.equals("f")) card = board.getFieldZone();
        if (card == null) {
            Output.getInstance().showMessage("no card found in the given position");
            return null;
        }
        return card;
    }

    public static boolean isCardSelected(Player player) {
        if (player.getBoard().getSelectedCard() == null) {
            Output.getInstance().showMessage("no card is selected yet");
            return false;
        }

        return true;
    }

    public static boolean isMainPhase(Phases phase) {
        if (phase.equals(Phases.MAIN1) || phase.equals(Phases.MAIN2)) return true;
        Output.getInstance().showMessage("action not allowed in this phase");
        return false;
    }

    public static boolean isCardInPlayerHand(Card card, Player player) {
        ArrayList<Card> hand = player.getBoard().getHandZoneCards();
        for (Card c : hand) {
            if (c.getName().equals(card.getName())) return true;
        }
        return false;
    }

    public static boolean isMonsterCard(Card card) {
        return card instanceof Monster;
    }

    public static boolean isMonsterCardZoneFull(ArrayList<Card> monsterZone) {
        for (Card card : monsterZone) {
            if (card == null) return false;
        }
        Output.getInstance().showMessage("monster card zone is full");
        return true;
    }

    public static boolean isThereOneMonsterForTribute(ArrayList<Card> monsterZone) {
        for (Card card : monsterZone) {
            if (card == null) return true;
        }
        Output.getInstance().showMessage("there are not enough card for tribute");
        return false;
    }

    public static boolean isThereTwoMonsterForTribute(ArrayList<Card> monsterZone) {
        int counter = 0;
        for (Card card : monsterZone) {
            if (card == null) counter++;
        }
        if (counter < 2) {
            Output.getInstance().showMessage("there are not enough card for tribute");
            return false;
        }
        return true;
    }

    public static boolean isThereCardInAddress(ArrayList<Card> monsterZone, int address) {
        if (monsterZone.get(address) == null) return false;
        Output.getInstance().showMessage("there are no monster one this address");
        return true;
    }

    public static boolean isNewMonsterMode(Card card, MonsterMode newMonsterMode) {
        if (((Monster) card).getMonsterMode().equals(newMonsterMode) ||
                card.getCardPlacement().equals(CardPlacement.faceDown)) {
            System.out.println("this card is already in the wanted position");
            return false;
        }
        return true;
    }


}
