package view;

public class GameInputs {

    private GameInputs() {
    }

    private static GameInputs instance;

    public static GameInputs getInstance() {
        if (instance == null)
            instance = new GameInputs();
        return instance;
    }

    private final String[] gamePlayRegexes = {

    };

}
