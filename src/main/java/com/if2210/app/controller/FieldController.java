package com.if2210.app.controller;

import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.FieldModel;
import com.if2210.app.model.PlantCardModel;

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
}
