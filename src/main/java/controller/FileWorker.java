package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Database;
import models.Player;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.io.*;

public class FileWorker {

    private final String usersDateBase = ".\\src\\main\\resources\\Database\\Users\\";
    private final String monsterDateBase = ".\\src\\main\\resources\\Database\\card-informations\\monsters";
    private final String spellDateBase = ".\\src\\main\\resources\\Database\\card-informations\\spells";
    private final String trapDateBase = ".\\src\\main\\resources\\Database\\card-informations\\traps";


    private FileWorker() {
    }
    private static FileWorker instance;
    public static FileWorker getInstance() {
        if (instance == null)
            instance = new FileWorker();
        return instance;
    }


    public Player readPlayerJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            Player player = gson.fromJson(bufferedReader, Player.class);
            return player;

        } catch (IOException e) {
            return null;
        }

    }

    public void writeUserJSON(Player player) {
        String fileAddress = usersDateBase + player.getUsername() + ".json";
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer;

        try {
            writer = new FileWriter(fileAddress);
            writer.write(gson.toJson(player));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Monster readMonsterJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            Monster monster = gson.fromJson(bufferedReader, Monster.class);
            return monster;

        } catch (IOException e) {
            return null;
        }

    }

    public Spell readSpellJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            Spell spell = gson.fromJson(bufferedReader, Spell.class);
            return spell;

        } catch (IOException e) {
            return null;
        }

    }

    public Trap readTrapJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            Trap trap = gson.fromJson(bufferedReader, Trap.class);
            return trap;

        } catch (IOException e) {
            return null;
        }

    }
}
