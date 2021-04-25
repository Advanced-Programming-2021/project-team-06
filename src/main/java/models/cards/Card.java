package models.cards;

public class Card {
    String name;
    int price;
    String description;


    public Card(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
