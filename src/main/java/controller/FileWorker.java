package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Database;
import models.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWorker {

    private String usersDateBase = ".\\src\\main\\resources\\Database\\Users\\";

    private FileWorker() {
    }

    private static FileWorker instance;

    public static FileWorker getInstance() {
        if (instance == null)
            instance = new FileWorker();
        return instance;
    }


    public Player readPlayerJSON(String username) {
        String fileAddress = usersDateBase + username + ".json";
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
}
