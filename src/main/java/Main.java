import models.Database;
import view.ConsoleBasedMenus;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Database.getInstance().loadingDatabase();
        ConsoleBasedMenus.getInstance().runRegisterMenu();
        Database.getInstance().updatingDatabase();
    }
}
