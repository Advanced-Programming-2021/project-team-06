import controller.menus.RegisterMenu;
import models.Database;

public class Main {
    public static void main(String[] args) {

        Database.getInstance().loadSpells();
    }
}
