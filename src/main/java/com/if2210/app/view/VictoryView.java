package com.if2210.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VictoryView {
    @FXML
    private Label win;

    private String playerName;

    public VictoryView(String playerName) {
        this.playerName = playerName;
    }

    @FXML
    public void initialize() {
        if (playerName.equals("0")) {
            win.setText("Draw");
        } else {
            win.setText("Congratulations to Player " + playerName);
        }
    }

    public void handleClose() {
        Platform.exit();
    }
}
