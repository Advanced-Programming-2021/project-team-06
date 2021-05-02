package controller.menus;

import models.Deck;

import java.util.Objects;

public class DeckMenuController {

    private DeckMenuController instance = null;

    private DeckMenuController (){ }
    public DeckMenuController getInstance () {
        return Objects.requireNonNullElseGet(instance, () -> (instance = new DeckMenuController()));
    }

    public void createDeck(String name) {
        Deck deck = new Deck(name);
    }


}
