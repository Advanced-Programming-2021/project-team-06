package controller;

import models.Board;
import models.Player;
import models.cards.*;
import view.GameInputs;
import view.Output;

import java.util.ArrayList;


public class Duel {
    private Player firstPlayer;
    private Player secondPlayer;
    private Player onlinePlayer;
    private Player offlinePlayer;
    private int turn = 1;
    private Phases phase = Phases.DRAW;
    private int numberOfPhase;

    public Duel(Player firstPlayer, Player secondPlayer, int numberOfPhase) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.firstPlayer.setBoard(new Board(firstPlayer, secondPlayer));
        this.secondPlayer.setBoard(new Board(secondPlayer, firstPlayer));
        this.onlinePlayer = firstPlayer;
        this.offlinePlayer = secondPlayer;
        this.numberOfPhase = numberOfPhase;
    }

    public void changeTurn() {
        turn *= -1;
        if (turn == 1) {
            this.onlinePlayer = firstPlayer;
            this.offlinePlayer = secondPlayer;
        } else {
            this.onlinePlayer = secondPlayer;
            this.offlinePlayer = firstPlayer;
        }
    }

    public void select(String address, boolean isMyBoard, String state) {
        if (!ErrorChecker.isValidAddress(address, state)) return;
        int cardPosition = Integer.parseInt(address);

        Card selectedCard;
        if (isMyBoard) {
            if (!state.equals("h")) cardPosition = setCardAddressInMyBoard(cardPosition);
            if ((selectedCard = ErrorChecker.istTheSeatVacant(onlinePlayer.getBoard(), cardPosition, state)) == null)
                return;
        } else {
            if (!state.equals("h")) cardPosition = setCardAddressInOpponentBoard(cardPosition);
            if ((selectedCard = ErrorChecker.istTheSeatVacant(offlinePlayer.getBoard(), cardPosition, state)) == null)
                return;
        }

        onlinePlayer.getBoard().setSelectedCard(selectedCard);
        Output.getInstance().showMessage("card selected");

    }

    public void deSelect() {

        if (ErrorChecker.isCardSelected(onlinePlayer)) return;
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("card deselected");

    }

    public void changePhase() {
        if (phase.equals(Phases.DRAW)) {
            phase = Phases.STANDBY;
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.STANDBY)) {
            phase = Phases.MAIN1;
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.MAIN1)) {
            phase = Phases.BATTLE;
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.BATTLE)) {
            phase = Phases.MAIN2;
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.MAIN2)) {
            phase = Phases.END;
            Output.getInstance().showMessage("phase: " + phase + "\n "
                    + "its" + offlinePlayer.getNickname() + "'s turn");
            changeTurn();
        }
    }

    public void summon() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        ArrayList<Card> monsterZone = onlinePlayer.getBoard().getMonsterZoneCards();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!ErrorChecker.isCardInPlayerHand(selectedCard, onlinePlayer) ||
                !ErrorChecker.isMonsterCard(selectedCard)) {
            Output.getInstance().showMessage("you can't summon this card");
            return;
        }
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone)) return;
        if (onlinePlayer.getBoard().isSummonedOrSetCardInTurn()) {
            Output.getInstance().showMessage("you already summoned/set on this turn");
            return;
        }
        if (((Monster) selectedCard).getLEVEL() == 5 || ((Monster) selectedCard).getLEVEL() == 6) {
            if (!ErrorChecker.isThereOneMonsterForTribute(monsterZone)) return;
            int address = Integer.parseInt(GameInputs.getInstance().getAddressForTribute());
            if (ErrorChecker.isThereCardInAddress(monsterZone, address)) return;
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address));
            onlinePlayer.getBoard().removeFromHand(selectedCard);

        }

        if (((Monster) selectedCard).getLEVEL() == 7 || ((Monster) selectedCard).getLEVEL() == 8) {
            if (!ErrorChecker.isThereTwoMonsterForTribute(onlinePlayer.getBoard().getMonsterZoneCards())) return;
            int address1 = Integer.parseInt(GameInputs.getInstance().getAddressForTribute());
            int address2 = Integer.parseInt(GameInputs.getInstance().getAddressForTribute());
            if (ErrorChecker.isThereCardInAddress(onlinePlayer.getBoard().getMonsterZoneCards(), address1)) return;
            if (ErrorChecker.isThereCardInAddress(onlinePlayer.getBoard().getMonsterZoneCards(), address2)) return;
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address1));
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address2));
        }

        selectedCard.setCardPlacement(CardPlacement.faceUp);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.attack);
        onlinePlayer.getBoard().putCardInMonsterZone(selectedCard);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(true);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("summoned successfully");

    }

    public void set() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        ArrayList<Card> monsterZone = onlinePlayer.getBoard().getMonsterZoneCards();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!ErrorChecker.isCardInPlayerHand(selectedCard, onlinePlayer) ||
                !ErrorChecker.isMonsterCard(selectedCard)) {
            Output.getInstance().showMessage("you can't set this card");
            return;
        }
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone)) return;
        if (onlinePlayer.getBoard().isSummonedOrSetCardInTurn()) {
            Output.getInstance().showMessage("you already summoned/set on this turn");
            return;
        }

        selectedCard.setCardPlacement(CardPlacement.faceDown);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.defence);
        onlinePlayer.getBoard().putCardInMonsterZone(selectedCard);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(true);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("set successfully");
    }

    public void setPosition(String mode) {
        MonsterMode newMonsterMode = null;
        if (mode.equals("attack")) newMonsterMode = MonsterMode.attack;
        if (mode.equals("defence")) newMonsterMode = MonsterMode.defence;

        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't summon this card");
            return;
        }
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (!ErrorChecker.isNewMonsterMode(selectedCard, newMonsterMode)) return;
        if (onlinePlayer.getBoard().isChangePositionInTurn()) {
            Output.getInstance().showMessage("you already changed this card position in this turn");
            return;
        }
        ((Monster) selectedCard).setMonsterMode(newMonsterMode);
        onlinePlayer.getBoard().setChangePositionInTurn(true);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("monster card position changed successfully");

    }

    public void flipSummon() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't summon this card");
            return;
        }
        if (!ErrorChecker.isMainPhase(phase)) return;
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.attack) ||
                selectedCard.getCardPlacement().equals(CardPlacement.faceUp)) {
            Output.getInstance().showMessage("you can't flip summon this card");
            return;
        }
        ((Monster) selectedCard).setMonsterMode(MonsterMode.attack);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("flip Summoned successfully");
    }

    public void attack(String address) {
        if (!ErrorChecker.isValidAddress(address, "m")) return;
        int cardPosition = setCardAddressInOpponentBoard(Integer.parseInt(address));

        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't attack with this card");
            return;
        }
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.defence)) {
            Output.getInstance().showMessage("This model is a defense card");
            return;
        }
        if (!ErrorChecker.isBattlePhase(phase)) return;
        if (((Monster) selectedCard).isHaveBeenAttackedWithMonsterInTurn()) {
            Output.getInstance().showMessage("this card already attacked");
            return;
        }
        if (ErrorChecker.istTheSeatVacant(offlinePlayer.getBoard(), cardPosition, "m") == null) return;
        runAttack(cardPosition, (Monster) selectedCard);

    }

    private void runAttack(int address, Monster selectedCard) {
        Monster targetMonster = (Monster) offlinePlayer.getBoard().getMonsterZoneCards().get(address);
        MonsterMode monsterMode = targetMonster.getMonsterMode();
        CardPlacement monsterPlacement = targetMonster.getCardPlacement();
        if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.attack))
            monsterAttackToAttack(targetMonster, selectedCard);

        if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.defence))
            monsterAttackToDefenseFaceUp(targetMonster, selectedCard);

        if (monsterPlacement.equals(CardPlacement.faceDown) && monsterMode.equals(MonsterMode.defence))
            monsterAttackToDefenseFaceDown(targetMonster, selectedCard);
    }

    private void monsterAttackToAttack(Monster targetMonster, Monster selectedCard) {
        if (selectedCard.getAttackPower() > targetMonster.getAttackPower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            int damage = selectedCard.getAttackPower() - targetMonster.getAttackPower();
            offlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            Output.getInstance().showMessage("your opponent's monster is destroyed and your opponent receviees" +
                    damage + " battle damage");
        }
        if (selectedCard.getAttackPower() == targetMonster.getAttackPower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            onlinePlayer.getBoard().putInGraveyard(selectedCard);
            onlinePlayer.getBoard().removeFromMonsterZone(selectedCard);
            Output.getInstance().showMessage("both you and your opponent monster cards are destroyed and no one receives damage");
        }
        if (selectedCard.getAttackPower() < targetMonster.getAttackPower()) {
            onlinePlayer.getBoard().putInGraveyard(selectedCard);
            onlinePlayer.getBoard().removeFromMonsterZone(selectedCard);
            int damage = targetMonster.getAttackPower() - selectedCard.getAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            Output.getInstance().showMessage("your monster card is destroyed and you received " + damage + " battle damage");
        }
    }

    private void monsterAttackToDefenseFaceUp(Monster targetMonster, Monster selectedCard) {
        if (selectedCard.getAttackPower() > targetMonster.getDefencePower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            Output.getInstance().showMessage("the defense position monster is destroyed");
        }
        if (selectedCard.getAttackPower() == targetMonster.getDefencePower())
            Output.getInstance().showMessage("no card destroyed");

        if (selectedCard.getAttackPower() < targetMonster.getDefencePower()) {
            int damage = targetMonster.getDefencePower() - selectedCard.getAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            Output.getInstance().showMessage("no card is destroyed and you received " + damage + " battle damage");
        }
        targetMonster.setCardPlacement(CardPlacement.faceUp);
    }

    private void monsterAttackToDefenseFaceDown(Monster targetMonster, Monster selectedCard) {
        if (selectedCard.getAttackPower() > targetMonster.getDefencePower()) {
            offlinePlayer.getBoard().putInGraveyard(targetMonster);
            offlinePlayer.getBoard().removeFromMonsterZone(targetMonster);
            Output.getInstance().showMessage("opponent's monster card was " + targetMonster.getName() +
                    "and the defense position monster is destroyed");
        }
        if (selectedCard.getAttackPower() == targetMonster.getDefencePower())
            Output.getInstance().showMessage("opponent's monster card was " + targetMonster.getName() +
                    "and no card destroyed");

        if (selectedCard.getAttackPower() < targetMonster.getDefencePower()) {
            int damage = targetMonster.getDefencePower() - selectedCard.getAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            Output.getInstance().showMessage("opponent's monster card was " + targetMonster.getName() +
                    "and no card is destroyed and you received " + damage + " battle damage");
        }
        targetMonster.setCardPlacement(CardPlacement.faceUp);
    }

    public void attackDirect() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't attack with this card");
            return;
        }
        if (((Monster) selectedCard).getMonsterMode().equals(MonsterMode.defence)) {
            Output.getInstance().showMessage("This model is a defense card");
            return;
        }
        if (!ErrorChecker.isBattlePhase(phase)) return;
        if (!ErrorChecker.isMonsterZoneEmpty(offlinePlayer.getBoard().getMonsterZoneCards())) {
            Output.getInstance().showMessage("you can't attack the opponent directly");
            return;
        }
        if (((Monster) selectedCard).isHaveBeenAttackedWithMonsterInTurn()) {
            Output.getInstance().showMessage("this card already attacked");
            return;
        }

        offlinePlayer.setHealth(offlinePlayer.getHealth() - ((Monster) selectedCard).getAttackPower());
        Output.getInstance().showMessage("you opponent receives " + ((Monster) selectedCard).getAttackPower()
                + " battle damage");
    }


    private int setCardAddressInMyBoard(int address) {
        if (address == 5) return 0;
        if (address == 3) return 1;
        if (address == 1) return 2;
        if (address == 2) return 3;
        if (address == 4) return 4;
        return -1;
    }

    private int setCardAddressInOpponentBoard(int address) {
        if (address == 4) return 0;
        if (address == 2) return 1;
        if (address == 1) return 2;
        if (address == 3) return 3;
        if (address == 5) return 4;
        return -1;
    }
}
