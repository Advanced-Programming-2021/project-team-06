package models.cards;


public class Card {
    String Name;


    public Card(String name){
        this.Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
