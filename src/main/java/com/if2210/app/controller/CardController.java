package com.if2210.app.controller;

import java.util.Map;

import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.ProductCardModel;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CardController {
    public static final String BLANK_IMAGE = "/com/if2210/app/assets/blank.png";

    public static void updateCard(AnchorPane card, CardModel cardData, boolean updateField, boolean isEnemyField,
            GameManagerModel gameManagerModel) {
        card.setUserData(cardData);

        if (updateField) {
            // Update Player Field
            String cardId = card.getId();
            if (cardId.startsWith("FieldCard")) {
                int id = Integer.parseInt(cardId.substring(9)) - 1;
                int i = id / 5;
                int j = id % 5;

                if (cardData.getImage().equals(BLANK_IMAGE)) {
                    if (isEnemyField) {
                        gameManagerModel.getEnemy().getField().removeCard(i, j);
                    } else {
                        gameManagerModel.getActivePlayer().getField().removeCard(i, j);
                    }
                } else {
                    if (isEnemyField) {
                        gameManagerModel.getEnemy().getField().setCard(cardData, i, j);
                    } else {
                        gameManagerModel.getActivePlayer().getField().setCard(cardData, i, j);

                    }
                }
            } else if (cardId.startsWith("ActiveDeck")) {
                int id = Integer.parseInt(cardId.substring(10)) - 1;
                if (cardData.getImage().equals(BLANK_IMAGE)) {
                    if (isEnemyField) {
                        gameManagerModel.getEnemy().getActiveDeck().removeCard(id);
                    } else {
                        gameManagerModel.getActivePlayer().getActiveDeck().removeCard(id);
                    }
                } else {
                    if (isEnemyField) {
                        gameManagerModel.getEnemy().getActiveDeck().setCard(id, cardData);
                    } else {
                        gameManagerModel.getActivePlayer().getActiveDeck().setCard(id, cardData);
                    }
                }
            }
        }

        ImageView imageView = (ImageView) card.getChildren().get(0);
        Label label = (Label) card.getChildren().get(1);

        Image image = new Image(CardController.class.getResourceAsStream(cardData.getImage()));
        if (image != null && !cardData.getImage().equals(BLANK_IMAGE)) {
            imageView.setImage(image);
            imageView.setVisible(true);
            label.setText(cardData.getName());
            label.setVisible(true);
        } else {
            imageView.setImage(null);
            imageView.setVisible(false);
            label.setText("");
            label.setVisible(false);
        }

        card.setStyle(null);
        String color = cardData.getColor();
        if (color != null && !color.isEmpty()) {
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 7.7px;");
        }
    }

    public static void deleteCard(AnchorPane source, boolean isEnemyField, GameManagerModel gameManagerModel) {
        CardModel emptyData = new CardModel("", "", BLANK_IMAGE);
        source.setUserData(emptyData);
        updateCard(source, emptyData, true, isEnemyField, gameManagerModel);
    }

    public static Integer sellCard(AnchorPane card, boolean isEnemyField, GameManagerModel gameManagerModel) {
        if (card.getUserData() instanceof ProductCardModel) {
            ProductCardModel productCard = (ProductCardModel) card.getUserData();
            gameManagerModel.getActivePlayer()
                    .setMoney(gameManagerModel.getActivePlayer().getMoney() + productCard.getPrice());

            boolean found = false;

            for (Map.Entry<ProductCardModel, Integer> entry : gameManagerModel.getShop().getProductList().entrySet()) {
                ProductCardModel product = entry.getKey();
                String productName = product.getName();

                if (productName.equals(productCard.getName())) {
                    try {
                        gameManagerModel.getShop().addProduct(product);
                    } catch (Exception e) {
                        // Exception handling
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                gameManagerModel.getShop().getProductList().put(productCard, 1);
            }

            CardController.deleteCard(card, isEnemyField, gameManagerModel);

            return gameManagerModel.getActivePlayer().getMoney();
        }
        return null;
    }

}
