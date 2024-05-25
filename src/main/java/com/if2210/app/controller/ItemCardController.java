package com.if2210.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.AnimalCardModel.AnimalType;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.ProductCardModel;

import javafx.scene.layout.AnchorPane;

public class ItemCardController {
    public static final String BLANK_IMAGE = "/com/if2210/app/assets/blank.png";

    public static void applyItemEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard,
            boolean isEnemyField, GameManagerModel gameManagerModel) {
        if (targetCardData instanceof AnimalCardModel) {
            AnimalCardModel temp = (AnimalCardModel) targetCardData;
            ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
            activeItems.add((ItemCardModel) sourceCardData);
            temp.setActiveItems(activeItems);
            CardController.updateCard(targetCard, temp, true, isEnemyField, gameManagerModel);
        } else {
            PlantCardModel temp = (PlantCardModel) targetCardData;
            ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
            activeItems.add((ItemCardModel) sourceCardData);
            temp.setActiveItems(activeItems);
            CardController.updateCard(targetCard, temp, true, isEnemyField, gameManagerModel);
        }
    }

    public static void applyInstantHarvest(CardModel targetCardData, AnchorPane targetCard, AnchorPane harvestCard,
            boolean isEnemyField, GameManagerModel gameManagerModel, List<AnchorPane> activeDecks) {
        CardController.deleteCard(harvestCard, isEnemyField, gameManagerModel);
        CardController.deleteCard(targetCard, isEnemyField, gameManagerModel);
        if (!targetCardData.getImage().equals(BLANK_IMAGE)) {
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

            ProductCardModel produk = ProductCardFactory
                    .createProductCard(resProd.get(((CardModel) targetCardData).getName()));
            for (int i = 0; i < 6; i++) {
                if (gameManagerModel.getActivePlayer().getActiveDeck().getCard(i) == null) {
                    // gameManagerModel.getActivePlayer().getActiveDeck().setCard(i, produk);
                    CardController.updateCard(activeDecks.get(i), produk, true, isEnemyField, gameManagerModel);
                    ActiveDeckController.loadActiveDeck(gameManagerModel.getActivePlayer(), activeDecks,
                            gameManagerModel,
                            isEnemyField);
                    break;
                }
            }
        }
    }

    public static String applyDestroyEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard,
            boolean isEnemyField, GameManagerModel gameManagerModel) {
        if (!targetCardData.getImage().equals(BLANK_IMAGE)) {
            if (targetCardData instanceof AnimalCardModel) {
                ArrayList<ItemCardModel> activeItems = ((AnimalCardModel) targetCardData).getActiveItems();
                boolean foundProtect = false;
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        return "Destroy card failed, target card is protected";
                    }
                }

                if (!foundProtect) {
                    CardController.deleteCard(targetCard, isEnemyField, gameManagerModel);
                    return "Destroy card success";
                }
            } else {
                ArrayList<ItemCardModel> activeItems = ((PlantCardModel) targetCardData).getActiveItems();
                boolean foundProtect = false;
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        return "Destroy card failed, target card is protected";
                    }
                }

                if (!foundProtect) {
                    CardController.deleteCard(targetCard, isEnemyField, gameManagerModel);
                    return "Destroy card success";
                }
            }
        }

        return "Destroy card failed, target card is empty";
    }

    public static String applyFeedEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard,
            AnchorPane sourceCard, boolean isEnemyField, GameManagerModel gameManagerModel) {
        AnimalCardModel temp = (AnimalCardModel) targetCardData;

        AnimalType tipe = temp.getType();
        ProductCardModel food = (ProductCardModel) sourceCardData;
        switch (tipe) {
            case OMNIVORE:
                temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                CardController.updateCard(targetCard, temp, true, isEnemyField, gameManagerModel);
                CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                // messageLabel.setText("Product card has been used to feed the animal");
                return "Product card has been used to feed the animal";
            case HERBIVORE:
                if (food.getName().equals("Jagung") || food.getName().equals("Labu")
                        || food.getName().equals("Stroberi")) {
                    temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                    CardController.updateCard(targetCard, temp, true, isEnemyField, gameManagerModel);
                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                    // messageLabel.setText("Product card has been used to feed the animal");
                    return "Product card has been used to feed the animal";
                }
                return "Product card cannot be used to feed the animal";
            case CARNIVORE:
                if (food.getName().equals("Daging Domba") || food.getName().equals("Daging Kuda")
                        || food.getName().equals("Daging Beruang") || food.getName().equals("Sirip Hiu")
                        || food.getName().equals("Telur") || food.getName().equals("Susu")) {
                    temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                    CardController.updateCard(targetCard, temp, true, isEnemyField, gameManagerModel);
                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                    // messageLabel.setText("Product card has been used to feed the animal");
                    return "Product card has been used to feed the animal";
                }
                return "Product card cannot be used to feed the animal";
            default:
                return "Product card cannot be used to feed the animal";
        }
    }
}
