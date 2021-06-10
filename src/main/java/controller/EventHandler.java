package controller;

import models.Player;
import models.cards.Card;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventHandler {
    private final static HashMap<Card, String>
            monsterSummonEvent = new HashMap<>(),
            opponentMonsterSummonEvent = new HashMap<>(),
    spellActivationEvent = new HashMap<>(),
    opponentSpellActivationEvent = new HashMap<>(),
    trapActivationEvent = new HashMap<>(),
    opponentTrapActivationEvent = new HashMap<>(),
    monsterAttackEvent = new HashMap<>(),
    opponentMonsterAttackEvent = new HashMap<>();
    private static Card trigger;

    public static void triggerTrapActivation(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(trapActivationEvent ,  false , "spell-activation-trigger");
    }
    public static void triggerSpellActivation(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(spellActivationEvent ,  false , "spell-activation-trigger");
    }
    public static void triggerMonsterSummon(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(monsterSummonEvent ,  false , "spell-activation-trigger");
    }
    public static void triggerMonsterAttack(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(monsterSummonEvent ,  false , "spell-activation-trigger");
    }
    public static void triggerOpponentTrapActivation(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(opponentTrapActivationEvent ,  true , "spell-activation-trigger");
    }
    public static void triggerOpponentSpellActivation(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(opponentSpellActivationEvent ,  true , "spell-activation-trigger");
    }
    public static void triggerOpponentMonsterSummon(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(opponentMonsterSummonEvent ,  true , "spell-activation-trigger");
    }
    public static void triggerOpponentMonsterAttack(Card trigger) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        EventHandler.trigger = trigger;
        triggerEvents(opponentMonsterAttackEvent ,  true , "spell-activation-trigger");
    }

    public static void waitForSpellActivation(Card waitingCard , String action) {
        spellActivationEvent.put(waitingCard , action);
    }
    public static void waitForOpponentSpellActivation(Card waitingCard , String action) {
        opponentSpellActivationEvent.put(waitingCard , action);
    }
    public static void waitForMonsterSummon(Card waitingCard , String action) {
        monsterSummonEvent.put(waitingCard , action);
    }
    public static void waitForOpponentMonsterSummon(Card waitingCard , String action) {
        opponentMonsterSummonEvent.put(waitingCard , action);
    }
    public static void waitForTrapActivation(Card waitingCard , String action) {
        trapActivationEvent.put(waitingCard , action);
    }
    public static void waitForOpponentTrapActivation(Card waitingCard , String action) {
        opponentTrapActivationEvent.put(waitingCard , action);
    }
    public static void waitForMonsterAttack(Card waitingCard , String action) {
        monsterAttackEvent.put(waitingCard , action);
    }
    public static void waitForOpponentMonsterAttack(Card waitingCard , String action) {
        opponentMonsterAttackEvent.put(waitingCard , action);
    }

    private static void triggerEvents(HashMap<Card,String> event , boolean justFromOpponent ,String eventName) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = trigger.getCurrentDeck().getOwner();
        for (Card card : event.keySet()) {
                if (justFromOpponent) {
                    Player waitingCardOwner = card.getCurrentDeck().getOwner();
                    if (waitingCardOwner == player)
                        continue;
                }
                String action = event.get(card);
            Matcher actionMatcher = getActionMatcher(action);
            if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition"), card)) {
                ActionJsonParser.getInstance().doActionList(actionMatcher.group("action"), card, eventName);
                event.remove(card);
            }
        }
    }
    public static Matcher getActionMatcher(String action) {
        Matcher matcher = Pattern.compile("\\*(?<condition>.*)\\*+(?<action>.+)").matcher(action);
        matcher.find();
        return matcher;
    }

}
