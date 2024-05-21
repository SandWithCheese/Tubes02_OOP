package com.if2210.app.controller;

import com.if2210.app.model.CardModel;
import com.if2210.app.model.PlayerModel;

import java.util.ArrayList;
import java.lang.Math;

public class PlayerController {
    public PlayerModel playerModel;

    public PlayerController(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    // Get random cards from Deck with amount of 6 - sizeof(activeDeck)
    public ArrayList<CardModel> getRandomNewCard() {
        ArrayList<CardModel> newCards = new ArrayList<>();
        int emptySlot = 6 - this.playerModel.getActiveDeck().getCards().size();

        // define the range index
        int max = this.playerModel.getActiveDeck().getCards().size() - 1;
        int min = 0;
        int range = max - min + 1;

        for (int i = 0; i < emptySlot; i++) {
            // Get random index
            int randomInt = (int)(Math.random() * range) + min;

            // Kalo card di index randomInt belom ada di newCards, maka akan di add
            if (!newCards.contains(this.playerModel.getDeck().getCards().get(randomInt))) {
                // Add card from deck to newCards
                newCards.add(this.playerModel.getDeck().getCards().get(randomInt));

                // Remove card from deck
                this.playerModel.getDeck().getCards().remove(randomInt);
                continue;
            }
            i--;
        }

        return newCards;
    }
}
