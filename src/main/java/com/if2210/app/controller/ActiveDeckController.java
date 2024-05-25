package com.if2210.app.controller;

import java.util.List;

import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.PlayerModel;

import javafx.scene.layout.AnchorPane;

public class ActiveDeckController {
    public static void loadActiveDeck(PlayerModel player, List<AnchorPane> activeDecks,
            GameManagerModel gameManagerModel, boolean isEnemyField) {
        FieldController.clearField(activeDecks, false, gameManagerModel, isEnemyField);
        for (int i = 0; i < 6; i++) {
            CardModel cardData = player.getActiveDeck().getCard(i);
            if (cardData != null) {
                CardController.updateCard(activeDecks.get(i), cardData, false, isEnemyField, gameManagerModel);
            }
        }
    }
}
