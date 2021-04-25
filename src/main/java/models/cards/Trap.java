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

    @Override
    public String toString() {
        return "Trap{" +
                "Name='" + name + '\'' +
                '}';
    }
}
