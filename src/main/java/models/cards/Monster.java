package models.cards;

import com.google.gson.annotations.SerializedName;

public class Monster extends Card {
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
    CardPlacement cardPlacement;
    String normalSummonTimeActions;
    String specialSummonTimeActions;
    String deathTimeActions;
    String flipTimeActions;
    String gettingRaidTimeActions;


    private String condition;
    private String action;
    private String cancellation;

    public Monster(String name) {
        super(name);
    }



    public void setAdditionalAttackPower(int additionalAttackPower) {
        this.additionalAttackPower = additionalAttackPower;
    }

    public void setAdditionalDefencePower(int additionalDefencePower) {
        this.additionalDefencePower = additionalDefencePower;
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

    public MonsterAttribute getMonsterAttribute() {
        return monsterAttribute;
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
}
