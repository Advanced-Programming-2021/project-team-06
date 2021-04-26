package controller;

import models.Deck;
import models.cards.*;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionJsonParser {

    private Matcher actionMatcher;

    private final HashMap<String , String> actionsRegexes= new HashMap<>();
    {
           actionsRegexes.put("collect <(?<deckList>.*)>\\[(?<attributeList>.*)]","collectCards");


    }

    private static ActionJsonParser actionJsonParserInstance;

    public static ActionJsonParser getInstance() {
        if (actionJsonParserInstance == null)
            return (actionJsonParserInstance = new ActionJsonParser());
        return actionJsonParserInstance;
    }

    public void doActionList(String actionsString) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String[] actions = actionsString.split(";");
        for (String action : actions) {
            String actionMethodName = getActionMethodName(action);
            ActionExecutor.getInstance().execute(actionMethodName , actionMatcher);
        }
    }

    private String getActionMethodName(String action) {

        for (String regex : actionsRegexes.keySet()) {
            actionMatcher = Pattern.compile(regex).matcher(action);
            if (actionMatcher.matches())
                return actionsRegexes.get(regex);
        }
        return "none";

    }

    public ArrayList<Deck> getDecksByTheirName(String[] deckListStrings) {
        ArrayList<Deck> deckList = new ArrayList<>();
        for (String deckName : deckListStrings)
            deckList.add(Deck.getDeckByName(deckName));
        return deckList;
    }

    public Card getDesiredCard(String[] attributeListStrings , String ofClass) {

        Card card;
        switch (ofClass) {
            case "Monster" : card = new Monster("desired" + ofClass);
            break;
            case "Trap" : card = new Trap("desired" + ofClass);
            break;
            case "Spell" : card = new Spell("desired" + ofClass);
            break;
            case "Any" : card = new Card("desiredCard");
            break;
            default:
                return null;
        }
        
      return card;
    }
}
