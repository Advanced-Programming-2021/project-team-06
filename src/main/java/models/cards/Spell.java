package models.cards;

public class Spell extends Card implements Cloneable {
    Boolean isActive;
    Boolean isActionable;

    public Spell(String name) {
        super(name);
        isActive = false;
    }

    public void setActionable(Boolean actionable) {
        isActionable = actionable;
    }

    public Boolean getActionable() {
        return isActionable;
    }

    public void activate() {
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
}
