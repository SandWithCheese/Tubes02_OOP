package com.if2210.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.FieldModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.PlantCardModel;
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

    public static void updatePlantHarvested(List<AnchorPane> fields, GameManagerModel gm, GUIController gui ){
        for (AnchorPane card : fields) {
            CardModel cardData = (CardModel) card.getUserData();
            if(!cardData.getImage().equals(GUIController.BLANK_IMAGE)){
                if(cardData instanceof PlantCardModel){
                    if(((PlantCardModel)cardData).getCurrentAge()>=((PlantCardModel)cardData).getHarvestAge()){
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
                        ProductCardModel produk = ProductCardFactory.createProductCard(resProd.get(((CardModel) cardData).getName()));

                        for (int j = 0; j < 6; j++) {
                            if(gm.getActivePlayer().getActiveDeck().getCard(j) == null){
                                gm.getActivePlayer().getActiveDeck().setCard(j, produk);
                                gui.updateCard(gui.activeDecks.get(j), produk, true);
                                break;
                            }
                        }
                        gui.deleteCard(card);
                    }
                }
            }            
        }
    }
}
