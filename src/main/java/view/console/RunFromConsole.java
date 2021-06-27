package view.console;

import models.Database;

import java.lang.reflect.InvocationTargetException;

public class RunFromConsole {

    public static void main(String[] args) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Database.getInstance().loadingDatabase();
        ConsoleBasedMenus.getInstance().runRegisterMenu();
        Database.getInstance().updatingDatabase();

    }

}
