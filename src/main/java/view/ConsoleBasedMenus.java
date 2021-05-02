package view;

import controller.ErrorChecker;
import controller.menus.MainMenu;
import controller.menus.ProfileController;
import controller.menus.RegisterMenuController;
import controller.menus.ShoppingController;
import models.Player;
import models.Scoreboard;

import java.util.Scanner;
import java.util.regex.*;


public class ConsoleBasedMenus {
    public static Scanner scanner = new Scanner(System.in);

    private String runningMenu = "register";

    private final String[] registerMenusRegexes = {
            "^user create (--username|-u) (?<username>\\w+) (--nickname|-n) (?<nickname>\\w+) (--password|-p) (?<password>\\w+)$",
            "^user create (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+) (--nickname|-n) (?<nickname>\\w+)$",
            "^user login (--username|-u) (?<username>\\w+) (--password|-p) (?<password>\\w+)$",
            "^user login (--password|-p) (?<password>\\w+) (--username|-u) (?<username>\\w+)$",
            "^menu enter (?<name>Main|Deck|Duel|Profile|Scoreboard|Shop)$",
            "^menu show-current$",
            "^menu exit$"
    };

    private final String[] mainMenusRegexes = {
            "^menu enter (?<name>Duel|Deck|Scoreboard|Profile|Shop)$",
            "^user logout$",
            "^menu show-current$",
            "^menu exit$"
    };
    private final String[] SCORE_BOARD_REGEXES = {

    };

    private final String[] profileMenusRegexes = {
            "^profile change (--nickname|-n) (?<nickname>\\w+)$",
            "^profile change (--password|-p) --current (?<oldPass>\\w+) --new (?<newPass>\\w+)$",
            "^profile change (--password|-p) --new (?<newPass>\\w+) --current (?<oldPass>\\w+)$",
            "^menu show-current$",
            "^menu exit$"
    };
    private final String[] deckMenuRegexes = {
            "^deck create (?<name>\\w+)$",
            "^deck delete (?<name>\\w+)$",
            "^deck set-activate (?<name>\\w+)$",
            "^$"
    };
    private final String[] shoppingMenusRegexes = {
            "^shop buy (?<cardName>.+)$",
            "^shop show --all$",
            "^menu show-current$",
            "^menu exit$"
    };

    private ConsoleBasedMenus() {
    }

    private static ConsoleBasedMenus instance;

    public static ConsoleBasedMenus getInstance() {
        if (instance == null)
            instance = new ConsoleBasedMenus();
        return instance;
    }


    public void runRegisterMenu() {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("register")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < 7; whichCommand++) {
                commandMatcher = findMatcher(command, registerMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeRegisterMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == 6)
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    private void executeRegisterMenuCommands(Matcher commandMatcher, int whichCommand) {
        switch (whichCommand) {
            case 0:
            case 1:
                RegisterMenuController.getInstance().createUser(commandMatcher.group("username"),
                        commandMatcher.group("nickname"), commandMatcher.group("password"));
                break;
            case 2:
            case 3:
                RegisterMenuController.getInstance().login(commandMatcher.group("username"), commandMatcher.group("password"));
                break;
            case 4:
                if (commandMatcher.group("name").equals("Main")) {
                    if (!ErrorChecker.isUserLoggedIn())
                        Output.getInstance().showMessage("please login first");
                    else {
                        runningMenu = "main";
                        runMainMenu();
                    }
                } else Output.getInstance().showMessage("menu navigation is not possible");
                break;
            case 5:
                Output.getInstance().showMessage("Register Menu");
                break;
            case 6:
                runningMenu = "end";
        }
    }

    public void runMainMenu() {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("main")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < 4; whichCommand++) {
                commandMatcher = findMatcher(command, mainMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeMainMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == 3)
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    private void executeMainMenuCommands(Matcher commandMatcher, int whichCommand) {
        switch (whichCommand) {
            case 0:
                String menuName = commandMatcher.group("name");
                if (menuName.equals("Duel")) {
                    runningMenu = "duel";
                    runDuelMenu();
                }
                if (menuName.equals("Deck")) {
                    runningMenu = "deck";
                    runDeckMenu();
                }
                if (menuName.equals("Scoreboard")) {
                    runningMenu = "scoreboard";
                    runScoreboard();
                }
                if (menuName.equals("Profile")) {
                    runningMenu = "profile";
                    runProfileMenu();
                }
                if (menuName.equals("Shop")) {
                    runningMenu = "shopping";
                    runShopping();
                }
                break;
            case 3:
            case 1:
                runningMenu = "register";
                break;
            case 2:
                Output.getInstance().showMessage("Main Menu");
        }
    }

    public void runDuelMenu() {

    }

    public void runDeckMenu() {

    }

    public void runScoreboard() {
        String command;
        while (runningMenu.equals("scoreboard")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            switch (command) {
                case "scoreboard show":
                    Scoreboard.getInstance().showScoreboard();
                    break;
                case "menu show-current":
                    Output.getInstance().showMessage("Scoreboard Menu");
                    break;
                case "menu exit":
                    runningMenu = "main";
                    return;
                default:
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    public void runProfileMenu() {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("profile")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < 5; whichCommand++) {
                commandMatcher = findMatcher(command, profileMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeProfileMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == 4)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeProfileMenuCommands(Matcher commandMatcher, int whichCommand) {
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        switch (whichCommand) {
            case 0:
                ProfileController.getInstance().changeNickname(playerLoggedIn, commandMatcher.group("nickname"));
                break;
            case 1:
            case 2:
                String oldPass = commandMatcher.group("oldPass");
                String newPass = commandMatcher.group("newPass");
                ProfileController.getInstance().changePassword(playerLoggedIn, oldPass, newPass);
                break;

            case 3:
                Output.getInstance().showMessage("Profile Menu");
                break;

            case 4:
                runningMenu = "main";
        }
    }

    public void runShopping() {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("shopping")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < 4; whichCommand++) {
                commandMatcher = findMatcher(command, shoppingMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeShoppingMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == 3)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeShoppingMenuCommands(Matcher commandMatcher, int whichCommand) {
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        switch (whichCommand) {
            case 0:
                String cardName = commandMatcher.group("cardName");
                ShoppingController.getInstance().buyCard(playerLoggedIn, cardName);
                break;
            case 1:
                ShoppingController.getInstance().showAllCard();
                break;
            case 2:
                Output.getInstance().showMessage("shopping Menu");
                break;

            case 3:
                runningMenu = "main";
        }
    }

    private Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }
}
