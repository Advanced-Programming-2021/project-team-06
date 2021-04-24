package models;

import controller.FileWorker;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.io.File;
import java.util.ArrayList;

public class Database {

    public static ArrayList<Player> allPlayers = new ArrayList<>();
    public static ArrayList<Card> allCards = new ArrayList<>();
    public static ArrayList<Monster> allMonsters = new ArrayList<>();
    public static ArrayList<Spell> allSpells = new ArrayList<>();
    public static ArrayList<Trap> allTraps = new ArrayList<>();


    private final String usersDateBase = ".\\src\\main\\resources\\Database\\Users\\";
    private final String monsterDateBase = ".\\src\\main\\resources\\Database\\card-informations\\monsters";
    private final String spellDateBase = ".\\src\\main\\resources\\Database\\card-informations\\spells";
    private final String trapDateBase = ".\\src\\main\\resources\\Database\\card-informations\\traps";


    private Database() {
    }

    private static Database instance;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public Player getPlayerByUsername(String username){
        for(Player player : allPlayers) {
            if (player.getUsername().equals(username))
                return player;
        }
        return null;

    }

    public Monster getMonsterByName(String name){
        for(Monster monster : allMonsters) {
            if (monster.getName().equals(name))
                return monster;
        }
        return null;

    }

    public Spell getSpellByName(String name){
        for(Spell spell : allSpells) {
            if (spell.getName().equals(name))
                return spell;
        }
        return null;

    }

    public Trap getTrapByName(String name){
        for(Trap trap : allTraps) {
            if (trap.getName().equals(name))
                return trap;
        }
        return null;

    }

    public void loadPlayers(){
        File file = new File(usersDateBase);
        File[] files = file.listFiles();
        for(File filePointer: files){
            allPlayers.add(FileWorker.getInstance().readPlayerJSON(filePointer.toString()));
        }

    }

    public void loadMonsters(){
        File file = new File(monsterDateBase);
        File[] files = file.listFiles();
        for(File filePointer: files){
            Monster monster = FileWorker.getInstance().readMonsterJSON(filePointer.toString());
            allMonsters.add(monster);
            allCards.add(monster);
        }

    }

    public void loadSpells(){
        File file = new File(spellDateBase);
        File[] files = file.listFiles();
        for(File filePointer: files){
            Spell spell = FileWorker.getInstance().readSpellJSON(filePointer.toString());
            allSpells.add(spell);
            allCards.add(spell);
        }

    }

    public void loadTraps(){
        File file = new File(trapDateBase);
        File[] files = file.listFiles();
        for(File filePointer: files){
            Trap trap = FileWorker.getInstance().readTrapJSON(filePointer.toString());
            allTraps.add(trap);
            allCards.add(trap);
        }

    }

    public void setPlayers(){
        for (Player player : allPlayers)
            FileWorker.getInstance().writeUserJSON(player);

    }


}
