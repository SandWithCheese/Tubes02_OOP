package com.if2210.app.datastore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.error.YAMLException;

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.DeckModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;
import com.if2210.app.plugin.JSONExtension;
import com.if2210.app.plugin.TXTExtension;
import com.if2210.app.plugin.YAMLExtension;

public class DataManager {
    private Path path;
    private Path gameStatePath;
    private Path player1Path;
    private Path player2Path;

    private Path resourcePath;
    private Path gameStateResourcePath;
    private Path player1ResourcePath;
    private Path player2ResourcePath;

    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;

    public DataManager() {
        try {
            this.path = Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0txt").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.gameStatePath = this.path.resolve("gamestate.txt");
        this.player1Path = this.path.resolve("player1.txt");
        this.player2Path = this.path.resolve("player2.txt");

        this.currentTurn = 0;
        this.productList = new HashMap<ProductCardModel, Integer>();
    }

    public DataManager(Path path) {
        this.path = path;
        this.gameStatePath = this.path.resolve("gamestate.txt");
        this.player1Path = this.path.resolve("player1.txt");
        this.player2Path = this.path.resolve("player2.txt");

        this.currentTurn = 0;
        this.productList = new HashMap<ProductCardModel, Integer>();
    }

    public Path getPath() {
        return path;
    }

    public Path getGameStatePath() {
        return gameStatePath;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public PlayerModel getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerModel player1) {
        this.player1 = player1;
    }

    public PlayerModel getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerModel player2) {
        this.player2 = player2;
    }

    public Map<ProductCardModel, Integer> getProductList() {
        return productList;
    }

    public void setProductList(Map<ProductCardModel, Integer> productList) {
        this.productList = productList;
    }

    public void save(String folderName, int currentTurn, Map<ProductCardModel, Integer> productList,
            PlayerModel player1, PlayerModel player2) {
                System.out.println(folderName);
        if (folderName.substring(folderName.length() - 3).equals("txt")) {
            TXTExtension dataTXT = new TXTExtension(this.path);
            dataTXT.save(folderName, currentTurn, productList, player1, player2);
        }
        else if (folderName.substring(folderName.length() - 4).equals("json")) {
            JSONExtension dataJSON = new JSONExtension(this.path);
            dataJSON.save(folderName, currentTurn, productList, player1, player2);
        }
        else if (folderName.substring(folderName.length() - 4).equals("yaml")) {
            YAMLExtension dataYAML = new YAMLExtension(this.path);
            dataYAML.save(folderName, currentTurn, productList, player1, player2);
        }
        else {
            System.out.println("Invalid file format");
        }
    }

    public void load(String folderName) {
        if (folderName.substring(folderName.length() - 3).equals("txt")) {
            TXTExtension dataTXT = new TXTExtension(this.path);
            dataTXT.load(folderName);
            this.currentTurn = dataTXT.getCurrentTurn();
            this.productList = dataTXT.getProductList();
            this.player1 = dataTXT.getPlayer1();
            this.player2 = dataTXT.getPlayer2();
        }
        else if (folderName.substring(folderName.length() - 4).equals("json")) {
            JSONExtension dataJSON = new JSONExtension(this.path);
            dataJSON.load(folderName);
            this.currentTurn = dataJSON.getCurrentTurn();
            this.productList = dataJSON.getProductList();
            this.player1 = dataJSON.getPlayer1();
            this.player2 = dataJSON.getPlayer2();
        }
        else if (folderName.substring(folderName.length() - 4).equals("yaml")) {
            YAMLExtension dataYAML = new YAMLExtension(this.path);
            dataYAML.load(folderName);
            this.currentTurn = dataYAML.getCurrentTurn();
            this.productList = dataYAML.getProductList();
            this.player1 = dataYAML.getPlayer1();
            this.player2 = dataYAML.getPlayer2();
        }
        else {
            System.out.println("Invalid file format");
        }
    }    
}
