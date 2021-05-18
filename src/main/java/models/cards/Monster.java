package models.cards;

import com.google.gson.annotations.SerializedName;
import controller.ActionJsonParser;
import controller.Duel;
import view.GameInputs;
import view.Output;

import java.awt.font.TextHitInfo;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Monster extends Card implements Cloneable{
    @SerializedName("Atk")
    int attackPower;
    int additionalAttackPower;
    @SerializedName("Def")
    int defencePower;
    int additionalDefencePower;
    @SerializedName("Monster Type")
    MonsterType monsterType;
    @SerializedName("Attribute")
    MonsterAttribute monsterAttribute;
    @SerializedName("Level")
    int LEVEL;
    @SerializedName("Action")
    private String action;
    private String isAttackable;
    private transient String normalSummonTimeActions;
    private transient String specialSummonTimeActions;
    private transient String deathTimeActions;
    private transient String flipTimeActions;
    private transient String gettingRaidTimeActions;
    private transient String endOfTurnActions;
    public transient boolean canBeUnderAttack = true;

    private boolean haveBeenAttackedWithMonsterInTurn = false;


    private String condition;
    private MonsterMode monsterMode;

    public Monster(String name) {
        super(name);
    }


    public void initializeMonstersEffects() {
        if (action == null)
            return;
        String[] actions = action.split("->");
        for (String action : actions) {
            String[] actionInformation = action.split(":");
            switch (actionInformation[0]){
                case "summon-time" : normalSummonTimeActions = actionInformation[1]; break;
                case "flip-time" : flipTimeActions = actionInformation[1]; break;
                case "death-time" : deathTimeActions = actionInformation[1]; break;
                case "getting-raid" : gettingRaidTimeActions = actionInformation[1]; break;
                case "special-summon-time" : specialSummonTimeActions = actionInformation[1]; break;
                case "end-of-turn" : endOfTurnActions = actionInformation[1]; break;
            }
        }
    }
    public MonsterMode getMonsterMode() {
        return monsterMode;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setMonsterMode(MonsterMode monsterMode) {
        this.monsterMode = monsterMode;
    }

    public void setAdditionalAttackPower(int additionalAttackPower) {
        this.additionalAttackPower = additionalAttackPower;
    }

    public int getAdditionalAttackPower() {
        return additionalAttackPower;
    }

    public int getAdditionalDefencePower() {
        return additionalDefencePower;
    }

    public void setAdditionalDefencePower(int additionalDefencePower) {
        this.additionalDefencePower = additionalDefencePower;
    }

    public int getTotalAttackPower(){
        return additionalAttackPower + attackPower;
    }

    public int getTotalDefencePower(){
        return additionalDefencePower + defencePower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public void setDefencePower(int defencePower) {
        this.defencePower = defencePower;
    }

    public void setLEVEL(int LEVEL) {
        this.LEVEL = LEVEL;
    }

    public int getAttackPower() {
        return attackPower;
    }
    public int getDefencePower() {
        return defencePower;
    }

    public int getLEVEL() {
        return LEVEL;
    }

    public boolean isHaveBeenAttackedWithMonsterInTurn() {
        return haveBeenAttackedWithMonsterInTurn;
    }

    public void setHaveBeenAttackedWithMonsterInTurn(boolean haveBeenAttackedWithMonsterInTurn) {
        this.haveBeenAttackedWithMonsterInTurn = haveBeenAttackedWithMonsterInTurn;
    }

    public MonsterAttribute getMonsterAttribute() {
        return monsterAttribute;
    }

    public void setMonsterAttribute(MonsterAttribute monsterAttribute) {
        this.monsterAttribute = monsterAttribute;
    }

    @Override
    public String toString() {
        return "Name: " + super.name + '\n' +
                "Level: " + this.LEVEL + '\n' +
                "Type: " + this.monsterType + '\n' +
                "ATK: " + ( this.attackPower + additionalAttackPower ) + '\n' +
                "DEF: " + (this.defencePower + additionalDefencePower) + '\n' +
                "Description: " + super.description + '\n';
    }
    @Override
    public Monster clone() throws CloneNotSupportedException
    {
        Monster monster = (Monster) super.clone();
        monster.setType(this.type);
        monster.setLEVEL(this.LEVEL);
        monster.setAttackPower(this.attackPower);
        monster.setDefencePower(this.defencePower);
        monster.setAction(this.action);
        monster.initializeMonstersEffects();
        return monster;
    }

    public void die() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        currentDeck = currentDeck.getOwner().getBoard().getGraveyardZone();
        this.resetAllFields();
        if (deathTimeActions == null)
            return;
        Matcher actionMatcher = getActionMatcher(deathTimeActions);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition") , this))
        ActionJsonParser.getInstance().doActionList(actionMatcher.group("action") , this  , "death-time");
    }

    public void flip() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (flipTimeActions == null)
            return;
        Matcher actionMatcher = getActionMatcher(flipTimeActions);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition") , this))
            ActionJsonParser.getInstance().doActionList(actionMatcher.group("action") , this , "flip-time");
    }

    public void summon() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        this.currentDeck = currentDeck.getOwner().getBoard().getMonsterZone();
        if (normalSummonTimeActions == null)
            return;
        Matcher actionMatcher = getActionMatcher(normalSummonTimeActions);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition") , this))
        ActionJsonParser.getInstance().doActionList(actionMatcher.group("action") , this  , "summon-time");
    }

    public void getRaid() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (gettingRaidTimeActions == null)
            return;
        Matcher actionMatcher = getActionMatcher(gettingRaidTimeActions);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition") , this))
        ActionJsonParser.getInstance().doActionList(actionMatcher.group("action") , this  , "getting-raid");
    }
    public void endOfTurn() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (endOfTurnActions == null)
            return;
        Matcher actionMatcher = getActionMatcher(endOfTurnActions);
        if (actionMatcher.group("condition").equals("") || ActionJsonParser.getInstance().checkConditionList(actionMatcher.group("condition") , this))
            ActionJsonParser.getInstance().doActionList(actionMatcher.group("action") , this  , "end-of-turn");

    }

    public boolean isAttackable() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (isAttackable == null)
            return true;
        return ActionJsonParser.getInstance().checkConditionList(isAttackable , this);
    }

    public void resetAllFields() {
        additionalAttackPower = 0;
        additionalDefencePower = 0;
        overriddenDescription = "";
        overriddenName = "";
    }

}

