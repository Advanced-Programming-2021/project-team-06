package models.cards;

import models.Database;

public class Card {
    String name;


    public Card(String name){
        this.name = name;
        Database.allCards.add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
