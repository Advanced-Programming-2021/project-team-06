package models.cards;

import javax.lang.model.element.Name;

public class Monster extends Card {
    int ATTACK_POWER;
    int additionalAttackPower;
    int DEFENCE_POWER;
    int additionalDefencePower;
    MonsterType TYPE;
    MonsterAttribute monsterAttribute;
    int LEVEL;
    CardPlacement cardPlacement;
    String normalSummonTimeActions;
    String specialSummonTimeActions;
    String deathTimeActions;
    String flipTimeActions;
    String gettingRaidTimeActions;


    public Monster(String name) {
        super(name);
    }

    public void setAdditionalAttackPower(int additionalAttackPower) {
        this.additionalAttackPower = additionalAttackPower;
    }

    public void setAdditionalDefencePower(int additionalDefencePower) {
        this.additionalDefencePower = additionalDefencePower;
    }

    public int getATTACK_POWER() {
        return ATTACK_POWER;
    }

    public int getDEFENCE_POWER() {
        return DEFENCE_POWER;
    }

    public int getLEVEL() {
        return LEVEL;
    }

    public MonsterAttribute getMonsterAttribute() {
        return monsterAttribute;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "Name='" + name + '\'' +
                '}';
    }
}
