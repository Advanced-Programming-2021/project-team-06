package controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionJsonParser {

    private final HashMap<String , String> actionsRegexes= new HashMap<>();
    {
           actionsRegexes.put("collect <(?<deckList>.*)>\\[(?<attributeList>.*)]","collectCards");
           actionsRegexes.put("say hello","sayHello");

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
            ActionExecutor.getInstance().execute(actionMethodName);
        }
    }

    private String getActionMethodName(String action) {

        for (String regex : actionsRegexes.keySet()) {
            Matcher actionMatcher = Pattern.compile(regex).matcher(action);
            if (actionMatcher.matches())
                return actionsRegexes.get(regex);
        }
        return "none";

    }
}
