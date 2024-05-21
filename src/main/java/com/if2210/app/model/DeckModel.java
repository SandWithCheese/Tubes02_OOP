package com.if2210.app.model;

import java.util.ArrayList;

public class DeckModel {
    private ArrayList<CardModel> cards;

    public DeckModel() {
        this.cards = new ArrayList<CardModel>();
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardModel> cards) {
        this.cards = cards;
    }
}
