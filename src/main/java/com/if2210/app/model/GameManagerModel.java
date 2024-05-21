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
                    Paths.get(getClass().getResource("/com/if2210/app/gamestates/state0").toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        this.dataManager.load();

        this.currentTurn = this.dataManager.getCurrentTurn();
        this.player1 = this.dataManager.getPlayer1();
        this.player2 = this.dataManager.getPlayer2();
        this.shop.setProductList(this.dataManager.getProductList());
    }
}
