package view;

import controller.ErrorChecker;
import controller.menus.*;
import models.Player;
import models.Scoreboard;

import java.lang.reflect.InvocationTargetException;
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
            "^deck add-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?:--side|-s)$",
            "^deck add-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+)$",
            "^deck rm-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+) (?:--side|-s)$",
            "^deck rm-card (?:--card|-c) (?<cardName>.+) (?:--deck|-d) (?<deckName>.+)$",
            "^deck show (?:--all|-a)$",
            "^deck show (?:--deck-name|-d) (?<deckName>.+) (?:--side|-s)$",
            "^deck show (?:--deck-name|-d) (?<deckName>.+)$",
            "^menu show-current$",
            "^menu exit$"
    };
    private final String[] shoppingMenusRegexes = {
            "^shop buy (?<cardName>.+)$",
            "^shop show --all$",
            "shop show money$",
            "^menu show-current$",
            "^menu exit$",
            "^increase (--money|-m) (?<amount>\\d+)"
    };
    private final String[] duelMenusRegexes = {
            "^duel new --second-player (?<username>\\w+) (--rounds|-r) (?<round>\\d+)$",
            "^duel new --single-player (--rounds|-r) (?<round>\\d+)",
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


    public void runRegisterMenu() throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("register")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < registerMenusRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, registerMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeRegisterMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == registerMenusRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    private void executeRegisterMenuCommands(Matcher commandMatcher, int whichCommand) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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

    public void runMainMenu() throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("main")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < mainMenusRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, mainMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeMainMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == mainMenusRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    private void executeMainMenuCommands(Matcher commandMatcher, int whichCommand) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
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

    public void runDeckMenu() {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("deck")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < deckMenuRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, deckMenuRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeDeckMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == deckMenuRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeDeckMenuCommands(Matcher commandMatcher, int whichCommand) {
        DeckMenuController controller = DeckMenuController.getInstance();
        Player loggedInPlayer = MainMenu.getInstance().getPlayerLoggedIn();
        switch (whichCommand) {
            case 0:
                controller.createDeck(commandMatcher.group("name"), loggedInPlayer);
                break;
            case 1:
                controller.deleteDeck(commandMatcher.group("name"));
                break;
            case 2:
                controller.setActiveDeck(commandMatcher.group("name"), loggedInPlayer);
                break;
            case 3:
                String cardName = commandMatcher.group("cardName"),
                        deckName = commandMatcher.group("deckName");
                controller.addCardToDeck(cardName, deckName, loggedInPlayer, false);
                break;
            case 4:
                cardName = commandMatcher.group("cardName");
                deckName = commandMatcher.group("deckName");
                controller.addCardToDeck(cardName, deckName, loggedInPlayer, true);
                break;
            case 5:
                cardName = commandMatcher.group("cardName");
                deckName = commandMatcher.group("deckName");
                controller.removeCardFromDeck(cardName, deckName, loggedInPlayer, false);
                break;
            case 6:
                cardName = commandMatcher.group("cardName");
                deckName = commandMatcher.group("deckName");
                controller.removeCardFromDeck(cardName, deckName, loggedInPlayer, true);
                break;
            case 7:
                controller.showAllDecks(loggedInPlayer);
                break;
            case 8:
                controller.showDeck(commandMatcher.group("deckName"), loggedInPlayer, false);
                break;
            case 9:
                controller.showDeck(commandMatcher.group("deckName"), loggedInPlayer, true);
                break;
            case 10:
                Output.getInstance().showMessage("Deck Menu");
                break;
            case 11:
                runningMenu = "main";

        }
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
            for (whichCommand = 0; whichCommand < profileMenusRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, profileMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeProfileMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == profileMenusRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeProfileMenuCommands(Matcher commandMatcher, int whichCommand) {
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        switch (whichCommand) {
            case 0:
                ProfileMenuController.getInstance().changeNickname(playerLoggedIn, commandMatcher.group("nickname"));
                break;
            case 1:
            case 2:
                String oldPass = commandMatcher.group("oldPass");
                String newPass = commandMatcher.group("newPass");
                ProfileMenuController.getInstance().changePassword(playerLoggedIn, oldPass, newPass);
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
            for (whichCommand = 0; whichCommand < shoppingMenusRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, shoppingMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeShoppingMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == shoppingMenusRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeShoppingMenuCommands(Matcher commandMatcher, int whichCommand) {
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        switch (whichCommand) {
            case 0:
                String cardName = commandMatcher.group("cardName");
                ShoppingMenuController.getInstance().buyCard(playerLoggedIn, cardName);
                break;
            case 1:
                ShoppingMenuController.getInstance().showAllCard();
                break;
            case 2:
                ShoppingMenuController.getInstance().showMoney(playerLoggedIn);
                break;
            case 3:
                Output.getInstance().showMessage("shopping Menu");
                break;
            case 4:
                runningMenu = "main";
                return;
            case 5:
                ShoppingMenuController.getInstance().increaseMoney(playerLoggedIn,
                        Integer.parseInt(commandMatcher.group("amount")));
        }
    }

    private void runDuelMenu() throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Matcher commandMatcher;
        String command;
        while (runningMenu.equals("duel")) {
            command = scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < duelMenusRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, duelMenusRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeDuelMenuCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == duelMenusRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }

        }
    }

    private void executeDuelMenuCommands(Matcher commandMatcher, int whichCommand) throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Player playerLoggedIn = MainMenu.getInstance().getPlayerLoggedIn();
        String secondUsername, round;
        switch (whichCommand) {
            case 0:
                secondUsername = commandMatcher.group("username");
                round = commandMatcher.group("round");
                DuelMenuController.getInstance().startGame(playerLoggedIn.getUsername(), secondUsername, round, false);
                break;
            case 1:
                round = commandMatcher.group("round");
                DuelMenuController.getInstance().startGame(playerLoggedIn.getUsername(), "AIPlayer", round, true);
            case 2:
                Output.getInstance().showMessage("duel Menu");
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
