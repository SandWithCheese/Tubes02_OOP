package com.if2210.app.model;

import java.util.ArrayList;

public class ActiveDeckModel {
    private ArrayList<CardModel> cards;

    public ActiveDeckModel() {
        this.cards = new ArrayList<CardModel>();
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardModel> cards) {
        this.cards = cards;
    }
}
