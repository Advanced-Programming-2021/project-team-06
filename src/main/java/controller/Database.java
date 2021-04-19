package controller;

import models.Player;

import com.google.gson.*;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
    private String usersDateBase = ".\\src\\main\\resources\\Database\\Users\\";


    private Database() {
    }

    private static Database instance;

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
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
        System.out.printf("1");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer;

        try {
            writer = new FileWriter(fileAddress);
            writer.write(gson.toJson(player));
            writer.close();
            System.out.printf("");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
