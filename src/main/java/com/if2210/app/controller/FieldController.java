package com.if2210.app.controller;

import java.util.List;

import com.if2210.app.constant.Constant;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.FieldModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;

import javafx.scene.layout.AnchorPane;

public class FieldController {
    public static void incrementAllCards(FieldModel field) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (field.getCard(i, j) != null && field.getCard(i, j) instanceof PlantCardModel) {
                    PlantCardModel card = (PlantCardModel) field.getCard(i, j);
                    card.setCurrentAge(card.getCurrentAge() + 1);
                    field.setCard(card, i, j);
                }
            }
        }
    }

    public static void loadField(PlayerModel player, List<AnchorPane> fieldCards, GameManagerModel gameManagerModel,
            boolean isEnemyField) {
        FieldController.clearField(fieldCards, false, gameManagerModel, isEnemyField);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                CardModel cardData = player.getField().getCard(i, j);
                if (cardData != null) {
                    CardController.updateCard(fieldCards.get(i * 5 + j), cardData, false, isEnemyField,
                            gameManagerModel);
                }
            }
        }
    }

    public static void clearField(List<AnchorPane> decks, boolean updateField, GameManagerModel gameManagerModel,
            boolean isEnemyField) {
        for (int i = 0; i < decks.size(); i++) {
            try {
                CardController.updateCard(decks.get(i), new CardModel("", "", Constant.BLANK_IMAGE), updateField,
                        isEnemyField,
                        gameManagerModel);
            } catch (Exception e) {
                // Handle exception
            }
        }
    }

    public static void updatePlantHarvested(List<AnchorPane> fields, GameManagerModel gm, GUIController gui,
            GameManagerModel gameManagerModel, boolean isEnemyField) {
        for (AnchorPane card : fields) {
            CardModel cardData = (CardModel) card.getUserData();
            if (!cardData.getImage().equals(Constant.BLANK_IMAGE)) {
                if (cardData instanceof PlantCardModel) {
                    int ageFromActiveItem = ((PlantCardModel) cardData).getCurrentAge();
                    for (ItemCardModel item : ((PlantCardModel) cardData).getActiveItems()) {
                        if (item.getName().equals("Accelerate")) {
                            ageFromActiveItem += 2;
                        } else if (item.getName().equals("Delay")) {
                            ageFromActiveItem -= 2;
                        }
                    }
                    System.out.println(ageFromActiveItem);
                    if (ageFromActiveItem >= ((PlantCardModel) cardData).getHarvestAge()) {
                        ProductCardModel produk = ProductCardFactory
                                .createProductCard(Constant.RES_PROD.get(((CardModel) cardData).getName()));

                        if (produk != null && !gm.getActivePlayer().getActiveDeck().isFull()) {
                            for (int j = 0; j < 6; j++) {
                                if (gm.getActivePlayer().getActiveDeck().getCard(j) == null) {
                                    gm.getActivePlayer().getActiveDeck().setCard(j, produk);
                                    CardController.updateCard(gui.activeDecks.get(j), produk, true, false,
                                            gameManagerModel);
                                    break;
                                }
                            }
                            CardController.deleteCard(card, isEnemyField, gameManagerModel);
                        } else {
                            cardData.setName(produk.getName());
                            cardData.setImage(produk.getImage());
                            CardController.updateCard(card, cardData, true, false, gameManagerModel);
                        }
                    }
                }
            }
        }
    }
}
