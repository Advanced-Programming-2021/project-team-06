package view;

import controller.ErrorChecker;
import controller.menus.MainMenu;
import controller.menus.ProfileController;
import controller.menus.RegisterMenuController;
import models.Player;
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
        REGISTER_MENUS_REGEXES[4] = "^menu enter (Main|Duel|Deck|Scoreboard|Profile|Shop)$";
        REGISTER_MENUS_REGEXES[5] = "^menu show-current$";
        REGISTER_MENUS_REGEXES[6] = "^menu exit$";
    }

    private String[] MAIN_MENUS_REGEXES = new String[4];
    {
        MAIN_MENUS_REGEXES[0] = "^menu enter (?<name>Duel|Deck|Scoreboard|Profile|Shop)$";
        MAIN_MENUS_REGEXES[1] = "^user logout$";
        MAIN_MENUS_REGEXES[2] = "^menu show-current$";
        MAIN_MENUS_REGEXES[3] = "^menu exit$";
    }

    private String[] PROFILE_MENUS_REGEXES = new String[5];
    {
        PROFILE_MENUS_REGEXES[0] = "^profile change (--nickname|-n) (?<nickname>\\w+)$";
        PROFILE_MENUS_REGEXES[1] = "^profile change (--password|-p) --current (?<oldPass>\\w+) --new (?<newPass>\\w+)$";
        PROFILE_MENUS_REGEXES[2] = "^profile change (--password|-p) --new (?<newPass>\\w+) --current (?<oldPass>\\w+)$";
        PROFILE_MENUS_REGEXES[3] = "^menu show-current$";
        PROFILE_MENUS_REGEXES[4] = "^menu exit$";
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
            isMatchCommand = false;
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

            commandMatcher = findMatcher(command, MAIN_MENUS_REGEXES[0]);
            if (commandMatcher.find()) {
                String menuName = commandMatcher.group("name");
                if (menuName.equals("Duel")) runDuelMenu();
                if (menuName.equals("Deck")) runDeckMenu();
                if (menuName.equals("Scoreboard")) runScoreboard();
                if (menuName.equals("Profile")) runProfileMenu();
                if (menuName.equals("Shop")) runShopping();
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, MAIN_MENUS_REGEXES[2]);
            if (commandMatcher.find()){
                Output.getInstance().showMessage("Main Menu");
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, MAIN_MENUS_REGEXES[1]);
            if (commandMatcher.find()) return;
            commandMatcher = findMatcher(command, MAIN_MENUS_REGEXES[3]);
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
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        boolean isMatchCommand = false;
        Matcher commandMatcher;
        String command;
        while (true) {
            command = scanner.nextLine().replaceAll("\\s+", " ");

            commandMatcher = findMatcher(command, PROFILE_MENUS_REGEXES[0]);
            if(commandMatcher.find()){
                ProfileController.getInstance().changeNickname(playerLoggedIn,commandMatcher.group("nickname"));
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, PROFILE_MENUS_REGEXES[1]);
            if(commandMatcher.find()){
                String oldPass = commandMatcher.group("oldPass");
                String newPass = commandMatcher.group("newPass");
                ProfileController.getInstance().changePassword(playerLoggedIn,oldPass, newPass);
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, PROFILE_MENUS_REGEXES[2]);
            if(commandMatcher.find()){
                String oldPass = commandMatcher.group("oldPass");
                String newPass = commandMatcher.group("newPass");
                ProfileController.getInstance().changePassword(playerLoggedIn,oldPass, newPass);
                isMatchCommand = true;
            }
            commandMatcher = findMatcher(command, PROFILE_MENUS_REGEXES[3]);
            if(commandMatcher.find()) Output.getInstance().showMessage("Profile Menu");
            commandMatcher = findMatcher(command, PROFILE_MENUS_REGEXES[4]);
            if (commandMatcher.find()) return;
            if (!isMatchCommand) Output.getInstance().showMessage("invalid command");
        }
    }

    public void runShopping() {

    }

    private Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
