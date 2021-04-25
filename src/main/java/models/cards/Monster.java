package models.cards;

public class Monster extends Card {

    private String condition;
    private String action;
    private String cancellation;
    public Monster(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Monster{" +
                "Name='" + name + '\'' +
                '}';
    }
}
