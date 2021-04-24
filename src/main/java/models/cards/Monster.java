package models.cards;

public class Monster extends Card {

    public Monster(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Monster{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
