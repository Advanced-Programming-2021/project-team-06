package models.cards;

import com.google.gson.annotations.SerializedName;
import controller.ActionJsonParser;

import java.lang.reflect.InvocationTargetException;

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
            }
        }
    }
    public MonsterMode getMonsterMode() {
        return monsterMode;
    }

    public void setMonsterMode(MonsterMode monsterMode) {
        this.monsterMode = monsterMode;
    }

    public void setAdditionalAttackPower(int additionalAttackPower) {
        this.additionalAttackPower = additionalAttackPower;
    }

    public void setAdditionalDefencePower(int additionalDefencePower) {
        this.additionalDefencePower = additionalDefencePower;
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
                "ATK: " + this.attackPower + '\n' +
                "DEF: " + this.defencePower + '\n' +
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
        return monster;
    }

    public void die() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (deathTimeActions == null)
            return;
        ActionJsonParser.getInstance().doActionList(deathTimeActions , this  , "death-time");
    }
    public void summon() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (normalSummonTimeActions == null)
            return;
        ActionJsonParser.getInstance().doActionList(normalSummonTimeActions , this  , "summon-time");
    }

    public boolean isAttackable() {
        if (isAttackable == null)
            return true;
        return false;
    }
    public void resetAllFields() {
        additionalAttackPower = 0;
        additionalDefencePower = 0;
        overriddenDescription = "";
        overriddenName = "";
    }
}

