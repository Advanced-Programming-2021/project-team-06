package models.cards;

import com.google.gson.annotations.SerializedName;

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
    String normalSummonTimeActions;
    String specialSummonTimeActions;
    String deathTimeActions;
    String flipTimeActions;
    String gettingRaidTimeActions;
    private boolean haveBeenAttackedWithMonsterInTurn = false;


    private String condition;
    private String action;
    private String cancellation;
    private MonsterMode monsterMode;

    public Monster(String name) {
        super(name);
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
}

