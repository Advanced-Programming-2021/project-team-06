package controller;

import models.Deck;
import models.cards.*;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionJsonParser {

    private Matcher conditionMatcher;
    private Duel currentDuel;

    private final HashMap<String, String> actionsRegexes = new HashMap<>();

    {
        actionsRegexes.put("collect<(?<deckList>.*)>\\[-(?<class>\\w*)-(?<attributeList>.*)]", "collectCards");
        actionsRegexes.put("increase-attack-power{(?<amount>\\d+)}", "increaseAttackPower");
        actionsRegexes.put("cancel{(?<eventName>.+)}", "cancel");
        actionsRegexes.put("kill-offender", "killOffender");
        actionsRegexes.put("kill", "kill");
        actionsRegexes.put("set-attack-power{(?<amount>\\d+)}", "set-attack-power");
        actionsRegexes.put("cancel-attack", "cancel-attack");

    }

    private static ActionJsonParser actionJsonParserInstance;

    public static ActionJsonParser getInstance() {
        if (actionJsonParserInstance == null)
            return (actionJsonParserInstance = new ActionJsonParser());
        return actionJsonParserInstance;
    }

    public void doActionList(String actionsString, Card clientCard , String event) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ActionExecutor actionExecutor = new ActionExecutor(event + ((Object)clientCard).toString());
        String[] actions = actionsString.split(";");
        for (String action : actions) {
            String actionMethodName = getActionMethodName(action);
            actionExecutor.execute(actionMethodName, conditionMatcher, clientCard);
        }
    }
    public boolean checkConditionList(String actionsString, Card clientCard) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        ConditionChecker conditionChecker = new ConditionChecker();
        String[] conditions = actionsString.split("&");
        boolean result = true;
        for (String condition : conditions) {
           result &= conditionChecker.check(condition, conditionMatcher, clientCard);
        }
        return result;
    }

    private String getActionMethodName(String action) {

        for (String regex : actionsRegexes.keySet()) {
            conditionMatcher = Pattern.compile(regex).matcher(action);
            if (conditionMatcher.matches())
                return actionsRegexes.get(regex);
        }
        return "none";

    }

    public Card getDesiredCard(String[] attributeListStrings, String ofClass) {

        Card card;
        switch (ofClass) {
            case "Monster":
                card = new Monster("desired" + ofClass);
                break;
            case "Trap":
                card = new Trap("desired" + ofClass);
                break;
            case "Spell":
                card = new Spell("desired" + ofClass);
                break;
            case "Any":
                card = new Card("desiredCard");
                break;
            default:
                return null;
        }

        return card;
    }


    public ArrayList<Deck> getDecksByTheirName(String[] deckLists) {
        ArrayList<Deck> decks = new ArrayList<>();
        for (String deckName : deckLists) {
            switch (deckName) {
                case "MZ":
                    decks.add(currentDuel.getOnlinePlayer().getBoard().getMonsterZone());
                    break;
                case "OMZ":
                    decks.add(currentDuel.getOfflinePlayer().getBoard().getMonsterZone());
                    break;
                case "SZ":
                    decks.add(currentDuel.getOnlinePlayer().getBoard().getSpellZone());
                    break;
                case "OSZ":
                    decks.add(currentDuel.getOfflinePlayer().getBoard().getSpellZone());
                    break;
                case "GY":
                    decks.add(currentDuel.getOnlinePlayer().getBoard().getGraveyardZone());
                    break;
                case "OGY":
                    decks.add(currentDuel.getOfflinePlayer().getBoard().getGraveyardZone());
                    break;
                case "H":
                    decks.add(currentDuel.getOnlinePlayer().getBoard().getHand());
                    break;
                case "OH":
                    decks.add(currentDuel.getOfflinePlayer().getBoard().getHand());
                    break;
                case "D":
                    decks.add(currentDuel.getOnlinePlayer().getBoard().getDeckZone());
                    break;
                case "OD":
                    decks.add(currentDuel.getOfflinePlayer().getBoard().getDeckZone());
                    break;
                case "F":
                    Deck singleCardDeck = new Deck("F" , null);
                    singleCardDeck.addCard(currentDuel.getOnlinePlayer().getBoard().getFieldZone());
                    decks.add(singleCardDeck);
                    break;
                case "OF":
                    singleCardDeck = new Deck("OF" , null);
                    singleCardDeck.addCard(currentDuel.getOfflinePlayer().getBoard().getFieldZone());
                    decks.add(singleCardDeck);
                case "AT":
                    singleCardDeck = new Deck("AT" , null);
                    singleCardDeck.addCard(currentDuel.getAttackingMonster());
                    decks.add(singleCardDeck);
                    break;
            }
        }
        return decks;
    }

    public void setDuel(Duel duel) {
        currentDuel = duel;
    }
}
