package controller;
import models.Deck;
import models.cards.Card;
import view.GameInputs;
import view.Output;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionChecker {
    private Card clientCard ;
    public boolean check(String condition , Card clientCard) {
        if (condition.equals("?")){
            Output.getInstance().showMessage("you can now activate the effect of " + clientCard.getName() +". do you want to?");
            return GameInputs.getInstance().yesOrNoQuestion();}
        Matcher conditionMatcher = getSimpleConditionMatcher(condition);
        this.clientCard = clientCard;
        int firstNumber = getVariable(conditionMatcher.group("firstNumber"));
        int secondNumber = getVariable(conditionMatcher.group("secondNumber"));

        return compare(firstNumber , secondNumber , conditionMatcher.group("sign"));
    }

    private boolean compare(int firstNumber, int secondNumber, String sign) {
        switch (sign) {
            case ">": return firstNumber > secondNumber;
            case "<": return firstNumber < secondNumber;
            case "=": return firstNumber == secondNumber;
            case ">=": return firstNumber >= secondNumber;
            case "<=": return firstNumber <= secondNumber;
            case "!=": return firstNumber != secondNumber;
        }
        return false;
    }

    private Matcher getSimpleConditionMatcher(String condition) {
        return Pattern.compile("(?<firstNumber>.?)(?<sign>>|=|<|>=|<=|!=)(?<secondNumber>.+)").matcher(condition);
    }
    private int getVariable(String variable) {
        int value = 0;
        try {
            value = Integer.parseInt(variable);
        } catch (Exception exception) {
            switch (variable) {
                case "effected{summon-time}" :
                    Deck collectedDeck = Objects.requireNonNull(ActionExecutor.getActionExecutorByName("summon-time" + ((Object) clientCard).toString())).getCollectedDeck();
                    if (collectedDeck == null)
                        break;
                   value =  collectedDeck.mainCards.size();
                    break;
                case "effected{flip-time}" :
                    collectedDeck = Objects.requireNonNull(ActionExecutor.getActionExecutorByName("flip-time" + ((Object) clientCard).toString())).getCollectedDeck();
                    if (collectedDeck == null)
                        break;
                    value =  collectedDeck.mainCards.size();
                    break;
            }
        }
        return value;
    }
}
