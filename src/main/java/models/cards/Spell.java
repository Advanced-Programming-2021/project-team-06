package models.cards;

public class Spell extends Card{

    public Spell(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return "Spell{" +
                "Name='" + Name + '\'' +
                '}';
    }
}
