package models.cards;

public class Trap extends Card {
    Boolean isActive;

    public Trap(String name) {
        super(name);
        isActive = false;
    }

    public void activate() {
        isActive = true;
    }

    public String toString() {
        return "Name: " + name +"\n" +
                "Trap" + "\n" +
                "Type: " + type + "\n" +
                "Description: " + description + "\n";
    }
}
