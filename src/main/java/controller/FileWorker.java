package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Deck;
import models.Player;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;

import java.io.*;

public class FileWorker {

    private final String usersDateBase = "./src/main/resources/Database/Users/";
    private final String decksDateBase = "./src/main/resources/Database/Decks/";
    private final String monsterDateBase = "./src/main/resources/Database/card-information/monsters";
    private final String spellDateBase = "./src/main/resources/Database/card-information/spells";
    private final String trapDateBase = "./src/main/resources/Database/card-information/traps";


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

            return gson.fromJson(bufferedReader, Player.class);

        } catch (IOException e) {
            return null;
        }

    }

    public void writeUserJSON(Player player) {
        String fileAddress = usersDateBase + player.getUsername() + ".json";
        writeFileTo(fileAddress, player);

    }

    public void writeDeckJSON(Deck deck) {
        String fileAddress = decksDateBase + deck.getName() + ".json";
        GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Player.class, Player.getPlayerSerializerForDeck()).registerTypeAdapter(Card.class, Card.getCardSerializerForDeck());
        Gson gson = builder.create();
        FileWriter writer;

        try {
            writer = new FileWriter(fileAddress);
            writer.write(gson.toJson(deck));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeFileTo(String fileAddress, Object objectToWrite) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer;

        try {
            writer = new FileWriter(fileAddress);
            writer.write(gson.toJson(objectToWrite));
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

            return gson.fromJson(bufferedReader, Monster.class);

        } catch (IOException e) {
            return null;
        }

    }

    public Spell readSpellJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            return gson.fromJson(bufferedReader, Spell.class);

        } catch (IOException e) {
            return null;
        }

    }

    public Trap readTrapJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            return gson.fromJson(bufferedReader, Trap.class);

        } catch (IOException e) {
            return null;
        }

    }

    public Deck readDeckJSON(String fileAddress) {

        try (FileReader reader = new FileReader(fileAddress)) {
            GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Player.class, Player.getPlayerDeserializerForDeck()).registerTypeAdapter(Card.class, Card.getCardDeserializerForDeck());
            Gson gson = builder.create();

            BufferedReader bufferedReader = new BufferedReader(reader);

            return gson.fromJson(bufferedReader, Deck.class);

        } catch (IOException e) {
            return null;
        }

    }
}
