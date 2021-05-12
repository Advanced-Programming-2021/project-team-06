package controller;


public class AI {

    private Duel onlineDuel;

    private AI() {
    }

    private static AI instance;

    public static AI getInstance() {
        if (instance == null)
            instance = new AI();
        return instance;
    }

    public Duel getOnlineDuel() {
        return onlineDuel;
    }

    public void setOnlineDuel(Duel onlineDuel) {
        this.onlineDuel = onlineDuel;
    }

    public void action() {

    }

}
