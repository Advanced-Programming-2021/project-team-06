package view;

import controller.Duel;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameInputs {

    private static GameInputs instance;
    private final String[] gamePlayRegexes = {
            "^select (--monster|-m) (?<address>\\d+)$",
            "^select (--monster|-m) (?<address>\\d+) (--opponent|-o)$",
            "^select (--spell|-s) (?<address>\\d+)$",
            "^select (--spell|-s) (?<address>\\d+) (--opponent|-o)$",
            "^select (--field|-f) (?<address>\\d+)$",
            "^select (--field|-f) (?<address>\\d+) (--opponent|-o)$",
            "^select (--hand|-h) (?<address>\\d+)$",
            "^select -d$",
            "^next phase$",
            "^summon$",
            "^set$",
            "^set (--position|-p) (?<mode>attack|defence)$",
            "^flip-summon",
            "^attack (?<address>\\d+)$",
            "^attack direct$",
            "^activate effect$",
            "^show graveyard$",
            "^card show (--selected|-s)$",
            "^increase --LP (?<amount>\\d+)$",
            "^duel set-winner (?<nickname>\\w+)$"

    };


    private Duel onlineDuel;

    private GameInputs() {
    }

    public static GameInputs getInstance() {
        if (instance == null)
            instance = new GameInputs();
        return instance;
    }

    public Duel getOnlineDuel() {
        return onlineDuel;
    }


    public void setOnlineDuel(Duel onlineDuel) {
        this.onlineDuel = onlineDuel;
    }

    
    public void runGamePlay() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Matcher commandMatcher;
        String command;
        while (!onlineDuel.isGameOver()) {
            command = ConsoleBasedMenus.scanner.nextLine().replaceAll("\\s+", " ");
            int whichCommand;
            for (whichCommand = 0; whichCommand < gamePlayRegexes.length; whichCommand++) {
                commandMatcher = findMatcher(command, gamePlayRegexes[whichCommand]);
                if (commandMatcher.find()) {
                    executeGamePlayCommands(commandMatcher, whichCommand);
                    break;
                } else if (whichCommand == gamePlayRegexes.length - 1)
                    Output.getInstance().showMessage("invalid command");
            }
        }
    }

    public boolean yesOrNoQ() {
        while (true) {
            String command = ConsoleBasedMenus.scanner.nextLine().replaceAll("\\s+", " ");
            if (command.equals("yes")) return true;
            else if (command.equals("no")) return false;
        }
    }

    public boolean backQ() {
        while (true) {
            String command = ConsoleBasedMenus.scanner.nextLine().replaceAll("\\s+", " ");
            if (command.equals("back")) return true;
        }
    }

    private void executeGamePlayCommands(Matcher commandMatcher, int whichCommand) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        switch (whichCommand) {
            case 0:
                onlineDuel.select(commandMatcher.group("address"), true, "m");
                break;
            case 1:
                onlineDuel.select(commandMatcher.group("address"), false, "m");
                break;
            case 2:
                onlineDuel.select(commandMatcher.group("address"), true, "s");
                break;
            case 3:
                onlineDuel.select(commandMatcher.group("address"), false, "s");
                break;
            case 4:
                onlineDuel.select(commandMatcher.group("address"), true, "f");
                break;
            case 5:
                onlineDuel.select(commandMatcher.group("address"), false, "f");
                break;
            case 6:
                onlineDuel.select(commandMatcher.group("address"), true, "h");
                break;
            case 7:
                onlineDuel.deSelect();
                break;
            case 8:
                onlineDuel.changePhase();
                break;
            case 9:
                onlineDuel.summon();
                break;
            case 10:
                onlineDuel.setMonster();
                onlineDuel.setSpellAndTrap();
                break;
            case 11:
                onlineDuel.setPosition(commandMatcher.group("mode"));
                break;
            case 12:
                onlineDuel.flipSummon();
                break;
            case 13:
                onlineDuel.attack(commandMatcher.group("address"));
                break;
            case 14:
                onlineDuel.attackDirect();
                break;
            case 15:
                onlineDuel.activateSpellCard();
                break;
            case 16:
                onlineDuel.showGraveyard();
                break;
            case 17:
                onlineDuel.showCard();
                break;
            case 18:
                onlineDuel.increaseLP(Integer.parseInt(commandMatcher.group("amount")));
                break;
            case 19:
                onlineDuel.cheatForWinGame(commandMatcher.group("nickname"));
        }
    }

    public String getAddressForTribute() {
        String address;
        Output.getInstance().showMessage("enter Address For tribute: ");
        address = ConsoleBasedMenus.scanner.nextLine();
        return address;
    }

    public String getAddressForDeleteCard() {
        String address;
        Output.getInstance().showMessage("enter Address For delete card: ");
        address = ConsoleBasedMenus.scanner.nextLine();
        return address;
    }


    private Matcher findMatcher(String input, String regex) {

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input);
    }

}
