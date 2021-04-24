package view;

import controller.ErrorChecker;
import controller.menus.RegisterMenuController;
import models.Scoreboard;

import java.util.Scanner;
import java.util.regex.*;


public class ConsoleBasedMenus {
    public static Scanner scanner = new Scanner(System.in);


    private String[] REGISTER_MENUS_REGEXES = new String[7];

    {
        REGISTER_MENUS_REGEXES[0] = "^user create (--username|-u) (?<username>\\w+) (--nickname|-n) (?<nickname>\\w+) (--password|-p) (?<password>\\w+)$";
        REGISTER_MENUS_REGEXES[1] = "^user create (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+) (--nickname|-n) (?<nickname>\\w+)$";
        REGISTER_MENUS_REGEXES[2] = "^user login (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+)$";
        REGISTER_MENUS_REGEXES[3] = "^user login (--password|-p) (?<password>\\w+) (--username|-u) (?<username>\\w+)$";
        REGISTER_MENUS_REGEXES[4] = "^menu enter (Duel|Deck|Scoreboard|Profile|Shop)$";
        REGISTER_MENUS_REGEXES[5] = "^menu show-current$";
        REGISTER_MENUS_REGEXES[6] = "^menu exit$";
    }

    private String[] Main_MENUS_REGEXES = new String[4];

    {
        Main_MENUS_REGEXES[0] = "^menu enter (?<name>Duel|Deck|Scoreboard|Profile|Shop)$";
        Main_MENUS_REGEXES[1] = "^user logout$";
        Main_MENUS_REGEXES[2] = "^menu show-current$";
        Main_MENUS_REGEXES[3] = "^menu exit$";
    }

    private ConsoleBasedMenus() {
    }

    private static ConsoleBasedMenus instance;

    public static ConsoleBasedMenus getInstance() {
        if (instance == null)
            instance = new ConsoleBasedMenus();
        return instance;
    }


    public void runRegisterMenu() {
        boolean isMatchCommand = false;
        Matcher commandMatcher;
        String command;
        while (true) {
            command = scanner.nextLine().replaceAll("\\s+", " ");

            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[0]);
            if (commandMatcher.find()) {
                RegisterMenuController.getInstance().createUser(
                        commandMatcher.group("username"), commandMatcher.group("nickname"), commandMatcher.group("password"));
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[1]);
            if (commandMatcher.find()) {
                RegisterMenuController.getInstance().createUser(
                        commandMatcher.group("username"), commandMatcher.group("nickname"), commandMatcher.group("password"));
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[2]);
            if (commandMatcher.find()) {
                RegisterMenuController.getInstance().login(
                        commandMatcher.group("username"), commandMatcher.group("password"));
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[3]);
            if (commandMatcher.find()) {
                RegisterMenuController.getInstance().login(
                        commandMatcher.group("username"), commandMatcher.group("password"));
                isMatchCommand = true;
            }
            if (command.startsWith("menu enter")) {

                commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[4]);
                if (commandMatcher.find()) {
                    if (!ErrorChecker.isUserLoggedIn())
                        Output.getInstance().showMessage("please login first");
                    else runMainMenu();
                } else Output.getInstance().showMessage("menu navigation is not possible");
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[5]);
            if (commandMatcher.find()) Output.getInstance().showMessage("Register Menu");
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[6]);
            if (commandMatcher.find()) return;
            if (!isMatchCommand) Output.getInstance().showMessage("invalid command");
        }

    }

    public void runMainMenu() {
        boolean isMatchCommand = false;
        Matcher commandMatcher;
        String command;
        while (true) {
            command = scanner.nextLine().replaceAll("\\s+", " ");

            commandMatcher = findMatcher(command, Main_MENUS_REGEXES[0]);
            if (commandMatcher.find()) {
                String menuName = commandMatcher.group("name");
                if (menuName.equals("Duel")) runDuelMenu();
                if (menuName.equals("Deck")) runDeckMenu();
                if (menuName.equals("Scoreboard")) runScoreboard();
                if (menuName.equals("Profile")) runProfileMenu();
                if (menuName.equals("Shop")) runShopping();
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, REGISTER_MENUS_REGEXES[2]);
            if (commandMatcher.find()){
                Output.getInstance().showMessage("Main Menu");
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, Main_MENUS_REGEXES[1]);
            if (commandMatcher.find()) return;
            commandMatcher = findMatcher(command, Main_MENUS_REGEXES[3]);
            if (commandMatcher.find()) return;
            if (!isMatchCommand) Output.getInstance().showMessage("invalid command");
        }
    }

    public void runDuelMenu() {

    }

    public void runDeckMenu() {

    }

    public void runScoreboard() {
        boolean isMatchCommand = false;
        String command;

        while (true){
            command = scanner.nextLine().replaceAll("\\s+", " ");
            if(command.equals("scoreboard show")){
                Scoreboard.getInstance().showScoreboard();
                isMatchCommand = true;
            }
            if(command.equals("menu show-current")){
                Output.getInstance().showMessage("Scoreboard Menu");
                isMatchCommand = true;
            }
            if(command.startsWith("menu enter")){
                Output.getInstance().showMessage("menu navigation is not possible");
                isMatchCommand = true;
            }
            if(command.equals("menu exit")) return;
            if (!isMatchCommand) Output.getInstance().showMessage("invalid command");
        }
    }

    public void runProfileMenu() {

    }

    public void runShopping() {

    }

    private Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
