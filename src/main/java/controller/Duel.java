package controller;

import models.Board;
import models.Player;
import models.cards.*;
import view.GameInputs;
import view.Output;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


public class Duel {
    private static Duel currentDuel = null;
    private final Player firstPlayer;
    private final Player secondPlayer;
    private Player onlinePlayer;
    private Player offlinePlayer;
    private int turn = 1;
    private Phases phase = Phases.DRAW;
    private Player winner;
    private Monster attackingMonster, targetMonster;

    public Duel(Player firstPlayer, Player secondPlayer) throws CloneNotSupportedException {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        firstPlayer.setHealth(8000);
        secondPlayer.setHealth(8000);
        this.firstPlayer.setBoard(new Board(firstPlayer, secondPlayer));
        this.secondPlayer.setBoard(new Board(secondPlayer, firstPlayer));
        this.onlinePlayer = firstPlayer;
        this.offlinePlayer = secondPlayer;
        ActionJsonParser.getInstance().setDuel(this);
        currentDuel = this;
    }

    public Player getWinner() {
        return winner;
    }

    public static Duel getCurrentDuel() {
        return currentDuel;
    }

    public Monster getAttackingMonster() {
        return attackingMonster;
    }

    public Monster getTargetMonster() {
        return targetMonster;
    }

    public Player getOfflinePlayer() {
        return offlinePlayer;
    }

    public Player getOnlinePlayer() {
        return onlinePlayer;
    }

    public void changePhase() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        showBoard();
        if (phase.equals(Phases.DRAW)) {
            phase = Phases.STANDBY;
            actionInStandbyPhase();
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.STANDBY)) {
            phase = Phases.MAIN1;
            actionsInMainPhase();
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.MAIN1)) {
            phase = Phases.BATTLE;
            actionsInBattlePhase();
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.BATTLE)) {
            phase = Phases.MAIN2;
            actionsInMainPhase();
            Output.getInstance().showMessage("phase: " + phase);
            return;
        }
        if (phase.equals(Phases.MAIN2)) {
            phase = Phases.END;
            Output.getInstance().showMessage("phase: " + phase);
            actionsInEndPhase();
            return;
        }
        if (phase.equals(Phases.END)) {
            phase = Phases.DRAW;
            actionsInDrawPhase();
            Output.getInstance().showMessage("phase: " + phase);
        }
    }

    public void actionsInDrawPhase() {
        onlinePlayer.getBoard().drawCard();
    }

    public void actionInStandbyPhase() {
    }

    public void actionsInMainPhase() {
        onlinePlayer.getBoard().setSelectedCard(null);
        onlinePlayer.getBoard().setChangePositionInTurn(false);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(false);

    }

    public void actionsInBattlePhase() {
        onlinePlayer.getBoard().setSelectedCard(null);
        for (Card card : onlinePlayer.getBoard().getMonsterZoneCards()) {
            if (card != null)
                ((Monster) card).setHaveBeenAttackedWithMonsterInTurn(false);
        }
    }

    public void actionsInEndPhase() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        doEndOfTurnActions();
        Output.getInstance().showMessage("its " + offlinePlayer.getNickname() + "'s turn");
        setNumberOfCardInHand();
        changeTurn();
    }

    private void doEndOfTurnActions() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        for (Card card : onlinePlayer.getBoard().getMonsterZoneCards()) {
            Monster monster = (Monster) card;
            if (monster == null)
                continue;
            monster.endOfTurn();
        }
    }

    private void setNumberOfCardInHand() {
        while (onlinePlayer.getBoard().getHandZoneCards().size() > 6) {
            int address = Integer.parseInt(GameInputs.getInstance().getAddressForDeleteCard());
            onlinePlayer.getBoard().getHand().moveCardTo(onlinePlayer.getBoard().getGraveyardZone(),
                    onlinePlayer.getBoard().getHandZoneCards().get(address), true, true);
        }
    }

    public boolean isGameOver() {
        if (onlinePlayer.getHealth() <= 0 || onlinePlayer.getBoard().getDeckZone().getMainCards().size() == 0) {
            setPrize(offlinePlayer, onlinePlayer);
            Output.getInstance().showMessage(offlinePlayer.getUsername() + "won the game and the score is: " +
                    offlinePlayer.getScore() + "-" + onlinePlayer.getScore());
            winner = offlinePlayer;
            return true;
        }
        if (offlinePlayer.getHealth() <= 0 || offlinePlayer.getBoard().getDeckZone().getMainCards().size() == 0) {
            setPrize(onlinePlayer, offlinePlayer);
            Output.getInstance().showMessage(onlinePlayer.getUsername() + "won the game and the score is: " +
                    onlinePlayer.getScore() + "-" + offlinePlayer.getScore());
            winner = onlinePlayer;
            return true;
        }
        return false;
    }

    private void setPrize(Player winner, Player loser) {
        winner.setScore(winner.getScore() + 1000);
        winner.setMoney(winner.getMoney() + winner.getHealth() + 1000);
        loser.setMoney(loser.getMoney() + 100);
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
            else cardPosition = cardPosition - 1;
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

    public void summon() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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
            String addressString = GameInputs.getInstance().getAddressForTribute();
            if (addressString.equals("")) return;
            int address = setCardAddressInMyBoard(Integer.parseInt(addressString));
            if (!ErrorChecker.isThereCardInAddress(monsterZone, address)) return;
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address));
            onlinePlayer.getBoard().removeFromHand(selectedCard);
        }

        if (((Monster) selectedCard).getLEVEL() == 7 || ((Monster) selectedCard).getLEVEL() == 8) {
            if (!ErrorChecker.isThereTwoMonsterForTribute(onlinePlayer.getBoard().getMonsterZoneCards())) return;
            String addressString1 = GameInputs.getInstance().getAddressForTribute();
            if (addressString1.equals("surrender")) return;
            String addressString2 = GameInputs.getInstance().getAddressForTribute();
            if (addressString2.equals("surrender")) return;
            int address1 = setCardAddressInMyBoard(Integer.parseInt(addressString1));
            int address2 = setCardAddressInMyBoard(Integer.parseInt(addressString2));
            if (!ErrorChecker.isThereCardInAddress(onlinePlayer.getBoard().getMonsterZoneCards(), address1)) return;
            if (!ErrorChecker.isThereCardInAddress(onlinePlayer.getBoard().getMonsterZoneCards(), address2)) return;
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address1));
            onlinePlayer.getBoard().removeFromMonsterZone(monsterZone.get(address2));
        }

        selectedCard.setCardPlacement(CardPlacement.faceUp);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.attack);
        onlinePlayer.getBoard().putCardInMonsterZone(selectedCard);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(true);
        onlinePlayer.getBoard().removeFromHand(selectedCard);
        onlinePlayer.getBoard().setSelectedCard(null);
        ((Monster) selectedCard).summon();
        Output.getInstance().showMessage("summoned successfully");

    }

    public void setMonster() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!(selectedCard instanceof Monster)) return;
        ArrayList<Card> monsterZone = onlinePlayer.getBoard().getMonsterZoneCards();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!ErrorChecker.isCardInPlayerHand(selectedCard, onlinePlayer)) {
            Output.getInstance().showMessage("you can't set this card");
            return;
        }
        if (ErrorChecker.isMonsterCard(selectedCard) && !ErrorChecker.isMainPhase(phase)) return;
        if (ErrorChecker.isMonsterCardZoneFull(monsterZone)) return;
        if (onlinePlayer.getBoard().isSummonedOrSetCardInTurn()) {
            Output.getInstance().showMessage("you already summoned/set on this turn");
            return;
        }

        selectedCard.setCardPlacement(CardPlacement.faceDown);
        ((Monster) selectedCard).setMonsterMode(MonsterMode.defence);
        onlinePlayer.getBoard().putCardInMonsterZone(selectedCard);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(true);
        onlinePlayer.getBoard().removeFromHand(selectedCard);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("set successfully");
    }

    public void activateSpellCard() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (ErrorChecker.isAbleToBeActive(selectedCard, phase, onlinePlayer.getBoard())) {
            int index = onlinePlayer.getBoard().getFirstFreeSpellZoneIndex();
            selectedCard.setCardPlacement(CardPlacement.faceUp);
            onlinePlayer.getBoard().getSpellZone().mainCards.set(index, selectedCard);
            onlinePlayer.getBoard().setSelectedCard(null);
            Output.getInstance().showMessage("spell activated");
        }
    }

    public void setSpellAndTrap() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!(selectedCard instanceof Spell || selectedCard instanceof Trap)) return;
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!ErrorChecker.isCardInPlayerHand(selectedCard, onlinePlayer)) {
            Output.getInstance().showMessage("you can't set this card");
            return;
        }
        if ((ErrorChecker.isTrapCard(selectedCard) || ErrorChecker.isSpellCard(selectedCard)) &&
                !ErrorChecker.isMainPhase(phase)) return;
        if (onlinePlayer.getBoard().isSpellZoneFull()) {
            Output.getInstance().showMessage("spell card zone is full");
            return;
        }
        selectedCard.setCardPlacement(CardPlacement.faceDown);
        onlinePlayer.getBoard().putInSpellZone(selectedCard);
        onlinePlayer.getBoard().setSummonedOrSetCardInTurn(true);
        onlinePlayer.getBoard().removeFromHand(selectedCard);
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("set successfully");
    }

    public void activationOfSpellInOpponentTurn() {
        turn *= -1;
        if (turn == 1)
            Output.getInstance().showMessage("now it will be " + firstPlayer.getUsername() + "'s turn");
        else
            Output.getInstance().showMessage("now it will be " + secondPlayer.getUsername() + "'s turn");
        Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
        Output.getInstance().showMessage("do you want to activate your trap and spell?");
        if (!GameInputs.getInstance().yesOrNoQuestion()) {
            turn *= -1;
            if (turn == 1)
                Output.getInstance().showMessage("now it will be " + firstPlayer.getUsername() + "'s turn");
            else
                Output.getInstance().showMessage("now it will be " + secondPlayer.getUsername() + "'s turn");
        } else {
            Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
            if (ErrorChecker.isAbleToBeActive(selectedCard, phase, onlinePlayer.getBoard())) {
                activateSpellCard();
                Output.getInstance().showMessage("spell/trap activated");
            } else {
                Output.getInstance().showMessage("it's not your turn to play this kind of moves");
            }
        }
    }

    public void setPosition(String mode) {
        MonsterMode newMonsterMode = null;
        if (mode.equals("attack")) newMonsterMode = MonsterMode.attack;
        if (mode.equals("defence")) newMonsterMode = MonsterMode.defence;

        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        if (!onlinePlayer.getBoard().isInMonsterZone(selectedCard)) {
            Output.getInstance().showMessage("you can't set this card");
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
        Output.getInstance().showMessage("monster card position changed successfully");
    }

    public void flipSummon() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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
        ((Monster) selectedCard).flip();
        onlinePlayer.getBoard().setSelectedCard(null);
        Output.getInstance().showMessage("flip Summoned successfully");
    }

    public void ritualSummon() {

    }


    public void attack(String address) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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
        ((Monster) selectedCard).setHaveBeenAttackedWithMonsterInTurn(true);

    }

    private void runAttack(int address, Monster selectedCard) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        attackingMonster = selectedCard;
        targetMonster = (Monster) offlinePlayer.getBoard().getMonsterZoneCards().get(address);
        MonsterMode monsterMode = targetMonster.getMonsterMode();
        CardPlacement monsterPlacement = targetMonster.getCardPlacement();
        if (!targetMonster.isAttackable())
            return;
        targetMonster.getRaid();
        if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.attack))
            monsterAttackToAttack(targetMonster, selectedCard);

        if (monsterPlacement.equals(CardPlacement.faceUp) && monsterMode.equals(MonsterMode.defence))
            monsterAttackToDefenseFaceUp(targetMonster, selectedCard);

        if (monsterPlacement.equals(CardPlacement.faceDown) && monsterMode.equals(MonsterMode.defence))
            monsterAttackToDefenseFaceDown(targetMonster, selectedCard);
        attackingMonster = null;
        targetMonster = null;
    }

    private void monsterAttackToAttack(Monster targetMonster, Monster selectedCard) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (selectedCard.getAttackPower() > targetMonster.getAttackPower()) {
            kill(offlinePlayer, targetMonster);
            int damage = selectedCard.getAttackPower() - targetMonster.getAttackPower();
            offlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            Output.getInstance().showMessage("your opponent's monster is destroyed and your opponent receives" +
                    damage + " battle damage");
        }
        if (selectedCard.getAttackPower() == targetMonster.getAttackPower()) {
            kill(offlinePlayer, targetMonster);
            kill(onlinePlayer, selectedCard);
            targetMonster.die();
            attackingMonster.die();
            Output.getInstance().showMessage("both you and your opponent monster cards are destroyed and no one receives damage");
        }
        if (selectedCard.getAttackPower() < targetMonster.getAttackPower()) {
            kill(onlinePlayer, selectedCard);
            int damage = targetMonster.getAttackPower() - selectedCard.getAttackPower();
            onlinePlayer.setHealth(offlinePlayer.getHealth() - damage);
            attackingMonster.die();
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

    public void kill(Player player, Monster monster) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        player.getBoard().putInGraveyard(monster);
        player.getBoard().removeFromMonsterZone(monster);
        monster.die();
    }

    public void kill(Monster monster) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player player = monster.getCurrentDeck().getOwner();
        player.getBoard().putInGraveyard(monster);
        player.getBoard().removeFromMonsterZone(monster);
        monster.die();
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
        ((Monster) selectedCard).setHaveBeenAttackedWithMonsterInTurn(true);
        Output.getInstance().showMessage("you opponent receives " + ((Monster) selectedCard).getAttackPower()
                + " battle damage");
    }

    public void showGraveyard() {
        ArrayList<Card> graveyard = onlinePlayer.getBoard().getGraveyardZone().mainCards;
        StringBuilder show = new StringBuilder();
        if (graveyard.size() != 0) {
            for (Card card : graveyard) {
                show.append(card.getName()).append(":").append(card.getDescription()).append("\n");
            }
            Output.getInstance().showMessage(show.toString());
        } else Output.getInstance().showMessage("graveyard empty");
        while (true) {
            if (GameInputs.getInstance().backQ()) {
                Output.getInstance().showMessage(onlinePlayer.getBoard().toString(onlinePlayer));
                return;
            }
        }
    }

    public void showCard() {
        Card selectedCard = onlinePlayer.getBoard().getSelectedCard();
        if (!ErrorChecker.isCardSelected(onlinePlayer)) return;
        boolean status = false;
        for (Card card : offlinePlayer.getAllPlayerCard().getMainCards()) {
            if (card.equals(selectedCard)) {
                status = true;
                break;
            }
        }
        if (ErrorChecker.isCardVisible(selectedCard, status)) {
            Output.getInstance().showMessage(selectedCard.toString());
        }
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

    public void increaseLP(int amount) {
        onlinePlayer.setHealth(onlinePlayer.getHealth() + amount);
    }

    public void cheatForWinGame(String nickname) {
        if (onlinePlayer.getNickname().equals(nickname)) offlinePlayer.setHealth(0);
        if (offlinePlayer.getNickname().equals(nickname)) onlinePlayer.setHealth(0);
    }

    public void showBoard() {
        Output.getInstance().showMessage(offlinePlayer.getBoard().toString(offlinePlayer) +
                onlinePlayer.getBoard().toString(onlinePlayer));
    }

}
