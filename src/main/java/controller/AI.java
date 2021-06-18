package controller;


import models.Deck;
import models.Player;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class AI {

    private static AI instance;
    private Duel onlineDuel;
    private Player aiPlayer;
    private Player singlePlayer;
    private AI() {
        new Player("ai", "ai", "ai");

    }

    public static AI getInstance() {
        if (instance == null)
            instance = new AI();
        return instance;
    }

    public Duel getOnlineDuel() {
        return onlineDuel;
    }

    public void setOnlineDuel(Duel onlineDuel) {
        this.onlineDuel = onlineDuel;
    }

    public void decisionMaker() {

    }

    public void action() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        switch (onlineDuel.getPhase()) {
            case DRAW:
            case END:
            case STANDBY:
                onlineDuel.changePhase();
                break;
            case MAIN1:
                setMonster();
                setSpellsAndTraps();
                onlineDuel.changePhase();
                break;
            case BATTLE:
                attackMonster();
                handleSpell();
                onlineDuel.changePhase();
                break;
            case MAIN2:
                handleSpell();
                onlineDuel.changePhase();
                break;
        }
    }

    private void attackMonster() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Monster monster = findBestMonsterToAttack();
        Monster tale = isThereWeakerCard(monster);
        if (monster == null) return;
        onlineDuel.select(String.valueOf(aiPlayer.getBoard().getHand().mainCards.indexOf(monster) + 1), true, "m");
        if (tale == null) {
            onlineDuel.attackDirect();
        } else {
            onlineDuel.attack(String.valueOf(onlineDuel.getOfflinePlayer().getBoard().getMonsterZone().mainCards.indexOf(tale) + 1));
        }
    }

    private void setSpellsAndTraps() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Card card : getSpellsInHand()) {
            if (!aiPlayer.getBoard().isSpellZoneFull()) {
                onlineDuel.select(String.valueOf(aiPlayer.getBoard().getHand().mainCards.indexOf(card) + 1), true, "m");
                onlineDuel.setSpellAndTrap();
            }
        }
    }

    private void setMonster() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Monster monster = findBestMonsterInHand();
        if (monster == null) return;
        onlineDuel.select(String.valueOf(aiPlayer.getBoard().getHand().mainCards.indexOf(monster) + 1), true, "m");
        if (!monster.getTypeCard().equals("ritual")) {
            onlineDuel.setMonster();
            if (monster.getDefencePower() > monster.getAttackPower() || monster.getAttackPower() < 800 || monster.getDefencePower() > 1500)
                onlineDuel.setPosition("defence");
            else
                onlineDuel.setPosition("attack");
        }
    }

    public Monster findWeakestSacrificeInGame() {
        int power = 1000000;
        Monster monster = null;
        for (Card card : aiPlayer.getBoard().getMonsterZone().mainCards) {
            if (card instanceof Monster && ((Monster) card).getAttackPower() < power) {
                power = ((Monster) card).getAttackPower();
                monster = (Monster) card;
            }
        }
        return monster;
    }

    public Monster findBestMonsterToAttack() {
        int power = 0;
        Monster monster = null;
        for (Card card : aiPlayer.getBoard().getHand().mainCards) {
            if (card instanceof Monster && ((Monster) card).getAttackPower() > power
                    && ((Monster) card).getMonsterMode().equals("attack") && isThereWeakerCard((Monster) card) != null) {
                power = ((Monster) card).getAttackPower();
                monster = (Monster) card;
            }
        }
        return monster;
    }

    private Monster isThereWeakerCard(Monster monster) {
        int power = monster.getAttackPower();
        for (Card card : singlePlayer.getBoard().getMonsterZone().mainCards) {
            if (card instanceof Monster && ((Monster) card).getDefencePower() < power) {
                return (Monster) card;
            }
        }
        return null;
    }


    public Monster findBestMonsterInHand() {
        int power = 0;
        Monster monster = null;
        for (Card card : aiPlayer.getBoard().getHand().mainCards) {
            if (card instanceof Monster && ((Monster) card).getAttackPower() > power) {
                power = ((Monster) card).getAttackPower();
                monster = (Monster) card;
            }
        }
        return monster;
    }

    private void handleSpell() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Card spell = selectRandom(aiPlayer.getBoard().getSpellZone());
        if (singlePlayer.getBoard().isMonsterZoneEmpty() || spell == null) return;
        onlineDuel.select(String.valueOf(aiPlayer.getBoard().getSpellZone().mainCards.indexOf(spell) + 1), true, "s");
        onlineDuel.activateSpellCard();
    }

    public ArrayList<Card> getSpellsInHand() {
        ArrayList<Card> cards = new ArrayList<>();
        for (Card card : aiPlayer.getBoard().getHand().mainCards) {
            if (card instanceof Spell) {
                cards.add(card);
            }
        }
        return cards;
    }

    public boolean doWeHaveTrap() {
        for (Card card : aiPlayer.getBoard().getHand().mainCards) {
            if (card instanceof Trap) return true;
        }
        return false;
    }

    public Player getAiPlayer() {
        return aiPlayer;
    }

    public void setAiPlayer(Player aiPlayer) {
        this.aiPlayer = aiPlayer;
    }

    private Card selectRandom(Deck deck) {
        if (deck != null && deck.getMainCards().size() != 0) {
            int random = new Random().nextInt(deck.mainCards.size());
            if (deck.mainCards.get(random) != null)
                return deck.mainCards.get(random);
            else return null;
        } else {
            return null;
        }
    }
}
