import models.Database;
import view.ConsoleBasedMenus;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        Database.getInstance().loadingDatabase();
        ConsoleBasedMenus.getInstance().runRegisterMenu();
        Database.getInstance().updatingDatabase();
    }
}
