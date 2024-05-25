package com.if2210.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.if2210.app.constant.Constant;
import com.if2210.app.factory.PlantCardFactory;
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
        if (!targetCardData.getImage().equals(Constant.BLANK_IMAGE)) {
            ProductCardModel produk = ProductCardFactory
                    .createProductCard(Constant.RES_PROD.get(((CardModel) targetCardData).getName()));
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
        if (!targetCardData.getImage().equals(Constant.BLANK_IMAGE)) {
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

    public static String applyDelayEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard,
            boolean isEnemyField, GameManagerModel gameManagerModel) {
        if (!targetCardData.getImage().equals(Constant.BLANK_IMAGE)) {
            boolean foundProtect = false;

            if (targetCardData instanceof AnimalCardModel) {
                ArrayList<ItemCardModel> activeItems = ((AnimalCardModel) targetCardData).getActiveItems();
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        return "Delay card failed, target card is protected";
                    }
                }
            } else {
                ArrayList<ItemCardModel> activeItems = ((PlantCardModel) targetCardData).getActiveItems();
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        break;
                    }
                }
            }

            if (!foundProtect) {
                applyItemEffect(sourceCardData, targetCardData, targetCard, isEnemyField, gameManagerModel);

                if (targetCardData instanceof PlantCardModel
                        && (((PlantCardModel) targetCardData).getCurrentAge()
                                - 2) < ((PlantCardModel) targetCardData).getHarvestAge()) {
                    if (Constant.RES_PROD.containsValue(targetCardData.getName())) {
                        for (Map.Entry<String, String> entry : Constant.RES_PROD.entrySet()) {
                            System.out.println(entry.getValue());
                            System.out.println(targetCardData.getName());
                            if (targetCardData.getName().equals(entry.getValue())) {
                                PlantCardModel plant = PlantCardFactory
                                        .createPlantCard(entry.getKey());
                                plant.setCurrentAge(
                                        ((PlantCardModel) targetCardData).getCurrentAge());
                                plant.setActiveItems(
                                        ((PlantCardModel) targetCardData).getActiveItems());
                                CardController.updateCard(targetCard, plant, true, isEnemyField, gameManagerModel);
                            }
                        }
                    }
                }

                return "Delay card has been used";
            } else {
                return "Delay card failed, target card is protected";
            }
        }

        return "Delay card failed, target card is empty";
    }
}
