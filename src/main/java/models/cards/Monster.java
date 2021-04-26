package models.cards;

public class Monster extends Card {
    int attackPower;
    int additionalAttackPower;
    int defencePower;
    int additionalDefencePower;
    MonsterType monsterType;
    MonsterAttribute monsterAttribute;
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
        return "Monster{" +
                "Name='" + super.name + '\'' +
                '}';
    }
}
