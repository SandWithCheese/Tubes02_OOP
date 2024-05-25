package com.if2210.app.controller;

import java.util.List;

import com.if2210.app.constant.Constant;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;

import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;

public class AnchorPaneController {
    public static void initializeDecks(Group deckGroup, List<AnchorPane> decks, int count, boolean isEnemyField,
            GameManagerModel gameManagerModel) {
        for (int i = 0; i < count; i++) {
            try {
                AnchorPane anchorPane = (AnchorPane) deckGroup.getChildren().get(i);
                if (!anchorPane.getChildren().isEmpty()) {
                    AnchorPane deck = (AnchorPane) anchorPane.getChildren().get(0);
                    decks.add(deck);
                    CardModel cardData = new CardModel("", "", Constant.BLANK_IMAGE);
                    CardController.updateCard(deck, cardData, false, isEnemyField, gameManagerModel);
                }
            } catch (Exception e) {
                // Handle exception
            }
        }
    }

    public static AnchorPane findDeckById(String id, List<AnchorPane> activeDecks, List<AnchorPane> fieldCards) {
        for (AnchorPane deck : activeDecks) {
            if (deck.getId().equals(id)) {
                return deck;
            }
        }
        for (AnchorPane deck : fieldCards) {
            if (deck.getId().equals(id)) {
                return deck;
            }
        }
        return null;
    }
}
