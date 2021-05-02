package models.cards;

public class Spell extends Card {
    Boolean isActive;

    public Spell(String name) {
        super(name);
        isActive = false;
    }

    public void activate() {
        isActive = true;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Spell" + "\n" +
                "Type: " + type + "\n" +
                "Description: " + description + "\n";
    }
}
