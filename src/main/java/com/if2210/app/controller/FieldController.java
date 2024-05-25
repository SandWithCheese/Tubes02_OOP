package com.if2210.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.if2210.app.factory.PlantCardFactory;
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
    public static final String BLANK_IMAGE = "/com/if2210/app/assets/blank.png";

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
                CardController.updateCard(decks.get(i), new CardModel("", "", BLANK_IMAGE), updateField, isEnemyField,
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
            if (!cardData.getImage().equals(GUIController.BLANK_IMAGE)) {
                if (cardData instanceof PlantCardModel) {
                    int ageFromActiveItem = ((PlantCardModel) cardData).getCurrentAge();
                    for (ItemCardModel item : ((PlantCardModel) cardData).getActiveItems()) {
                        if (item.getName().equals("Accelerate")) {
                            ageFromActiveItem += 2;
                        } else if (item.getName().equals("Delay")) {
                            ageFromActiveItem -= 2;
                        }
                    }
                    Map<String, String> resProd = new HashMap<>();
                    resProd.put("Hiu Darat", "Sirip Hiu");
                    resProd.put("Sapi", "Susu");
                    resProd.put("Domba", "Daging Domba");
                    resProd.put("Kuda", "Daging Kuda");
                    resProd.put("Ayam", "Telur");
                    resProd.put("Beruang", "Daging Beruang");
                    resProd.put("Biji Jagung", "Jagung");
                    resProd.put("Biji Labu", "Labu");
                    resProd.put("Biji Stroberi", "Stroberi");
                    System.out.println(ageFromActiveItem);
                    if (ageFromActiveItem >= ((PlantCardModel) cardData).getHarvestAge()) {
                        resProd.put("Jagung", "Jagung");
                        resProd.put("Labu", "Labu");
                        resProd.put("Stroberi", "Stroberi");
                        ProductCardModel produk = ProductCardFactory
                                .createProductCard(resProd.get(((CardModel) cardData).getName()));

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
