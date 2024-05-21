package com.if2210.app.datastore;

import java.nio.file.Files;
import java.nio.file.Path;

import com.if2210.app.model.PlayerModel;

public class DataManager {
    private final Path path;
    private final Path gameStatePath;
    private final Path player1Path;
    private final Path player2Path;

    private int currentTurn;
    // ini shop items tergantung gimana ngisinya
    private PlayerModel player1;
    private PlayerModel player2;

    public DataManager(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.txt");
        this.player1Path = this.path.resolve("player1.txt");
        this.player2Path = this.path.resolve("player2.txt");
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public PlayerModel getPlayer1() {
        return player1;
    }

    public PlayerModel getPlayer2() {
        return player2;
    }

    public void save() {
        // Save the path to a file
    }

    public void load() {
        try {
            Files.readAllLines(gameStatePath).forEach(line -> {
                // Parse the line
                System.out.println(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            Files.readAllLines(player1Path).forEach(line -> {
                // Parse the line
                System.out.println(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            Files.readAllLines(player2Path).forEach(line -> {
                // Parse the line
                System.out.println(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
