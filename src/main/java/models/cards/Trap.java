package models.cards;

public class Trap extends Card implements Cloneable {
    Boolean isActive;

    public Trap(String name) {
        super(name);
        isActive = false;
    }

    public void activate() {
        isActive = true;
    }

    public Boolean getActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\n" +
                "Trap" + "\n" +
                "Type: " + type + "\n" +
                "Description: " + description + "\n";
    }

    @Override
    public Trap clone() throws CloneNotSupportedException {
        return (Trap) super.clone();
    }
}
