package com.if2210.app.controller;


import com.if2210.app.model.GameManagerModel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameController {
    private GameManagerModel gameManagerModel;

    @FXML
    private Label gulden1;

    @FXML
    private Label gulden2;

    @FXML
    private Label deckCount;

    @FXML
    private Label gameTurn;

    public GameController() {
        this.gameManagerModel = new GameManagerModel();
    }

    @FXML
    public void initialize() {
        System.out.println("GameController initialized");

        gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
        gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
        deckCount.setText("My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
    }
}
