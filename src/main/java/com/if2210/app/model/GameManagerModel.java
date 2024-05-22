package com.if2210.app.model;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.inject.Singleton;

import com.if2210.app.datastore.DataManager;

@Singleton
public class GameManagerModel {
    private DataManager dataManager;
    private int currentTurn;
    private ShopModel shop;
    private PlayerModel player1;
    private PlayerModel player2;

    public GameManagerModel() {
        try {
            this.dataManager = new DataManager(
                    Paths.get(getClass().getResource("/com/if2210/app/gamestates/state1").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.dataManager.load();

        this.shop = ShopModel.getInstance();

        this.currentTurn = this.dataManager.getCurrentTurn();
        this.player1 = this.dataManager.getPlayer1();
        this.player2 = this.dataManager.getPlayer2();
        this.shop.setProductList(this.dataManager.getProductList());

        // Print all data
        System.out.println("Current turn: " + this.currentTurn);
        System.out.println("Player 1 field: " + this.player1.getField());
        System.out.println("Player 1 active deck: " + this.player1.getActiveDeck());
        System.out.println("Player 1 deck: " + this.player1.getDeck());
        System.out.println("Player 1 money: " + this.player1.getMoney());
        System.out.println("Player 2 field: " + this.player2.getField());
        System.out.println("Player 2 active deck: " + this.player2.getActiveDeck());
        System.out.println("Player 2 deck: " + this.player2.getDeck());
        System.out.println("Player 2 money: " + this.player2.getMoney());
        System.out.println("Shop product list: " + this.shop.getProductList());
    }
}
