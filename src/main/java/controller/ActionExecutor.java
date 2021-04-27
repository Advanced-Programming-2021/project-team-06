package controller;
import models.Deck;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;

public class ActionExecutor {

    private static ActionExecutor instance;
    private Matcher neededInformation;
    private Deck collectedDeck;
    public static ActionExecutor getInstance() {
        return Objects.requireNonNullElseGet(instance, () -> (instance = new ActionExecutor()));
    }
    public void execute(String methodName , Matcher matcher) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        this.getClass().getDeclaredMethod(methodName).invoke(this);
        neededInformation = matcher ;
    }
    private void collectCards() {
        ArrayList<Deck> deckList = ActionJsonParser.getInstance().getDecksByTheirName(neededInformation.group("deckList").split(","));
        getCardsFromTheirDeck(deckList ,  neededInformation.group("class"));
        Card desiredCard = ActionJsonParser.getInstance().getDesiredCard(
                        neededInformation.group("attributeList").split(","),
                        neededInformation.group("class"));
    }

    private void getCardsFromTheirDeck(ArrayList<Deck> decks , String ofClass) {
        ArrayList<Card> cards = collectedDeck.getMainCards();
        for (Deck deck : decks)
           cards.addAll(deck.getMainCards());
        switch (ofClass) {
            case "Any":
                return;
            case "Monster":
                cards.removeIf(card -> !(card instanceof Monster));
                break;
            case "Spell":
                cards.removeIf(card -> !(card instanceof Spell));
                break;
            case "Trap":
                cards.removeIf(card -> !(card instanceof Trap));
                break;
        }

    }

}
