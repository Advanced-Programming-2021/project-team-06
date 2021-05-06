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
        if (onlinePlayer.getBoard().isSummonedCardInTurn()) {
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

        Output.getInstance().showMessage("summoned successfully");

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
