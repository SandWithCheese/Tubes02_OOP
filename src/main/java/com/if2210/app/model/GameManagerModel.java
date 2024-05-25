package com.if2210.app.model;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import javax.inject.Singleton;

import com.if2210.app.datastore.DataManager;

@Singleton
public class GameManagerModel {
    private DataManager dataManager; 
    private int currentTurn;
    // Penentu giliran pemain, cuma bisa 0 atau 1
    private int whoseTurn;
    private ShopModel shop;
    private PlayerModel player1;
    private PlayerModel player2;

    public GameManagerModel() {
        try {
            this.dataManager = new DataManager(
                    Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.dataManager.load("state0txt");

        this.shop = ShopModel.getInstance();

        this.currentTurn = this.dataManager.getCurrentTurn();
        this.whoseTurn = -1;
        this.player1 = this.dataManager.getPlayer1();
        this.player2 = this.dataManager.getPlayer2();
        this.shop.setProductList(this.dataManager.getProductList());
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public int getWhoseTurn() {
        return whoseTurn;
    }

    public void setWhoseTurn(int whoseTurn) {
        this.whoseTurn = whoseTurn;
    }

    public PlayerModel getActivePlayer() {
        if (this.whoseTurn == 0) {
            return this.player1;
        } else {
            return this.player2;
        }
    }

    public ShopModel getShop() {
        return shop;
    }

    public void setShop(ShopModel shop) {
        this.shop = shop;
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

    public PlayerModel getEnemy() {
        if (this.whoseTurn == 0) {
            return this.player2;
        } else {
            return this.player1;
        }
    }
}
