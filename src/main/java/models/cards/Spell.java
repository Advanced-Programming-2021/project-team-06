package models.cards;

import com.google.gson.annotations.SerializedName;
import controller.ActionJsonParser;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;

public class Spell extends Card implements Cloneable {
    transient Boolean isActive = false;
    Boolean isActionable;
    @SerializedName("Status")
    String status;
    @SerializedName("Property")
    String property;
    @SerializedName("Action")
    String action;

    public Spell(String name) {
        super(name);
        isActive = false;
    }

    public void setActionable(Boolean actionable) {
        isActionable = actionable;
    }

    public Boolean getActionable() {
        if (isActionable == null)
            return false;
        return isActionable;
    }

    public void activate() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        currentDeck = currentDeck.getOwner().getBoard().getSpellZone();
        if (action == null)
            return;
        Matcher actionMatcher = getActionMatcher(action);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition"), this))
            ActionJsonParser.getInstance().doActionList(actionMatcher.group("action"), this, "spell-activation");
        isActive = true;
    }

    public Boolean getActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Spell" + "\n" +
                "Type: " + type + "\n" +
                "Description: " + description + "\n";
    }

    @Override
    public Spell clone() throws CloneNotSupportedException {
        return (Spell) super.clone();
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
}
