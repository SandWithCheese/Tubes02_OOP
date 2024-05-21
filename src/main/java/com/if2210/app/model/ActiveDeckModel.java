package com.if2210.app.model;

import java.util.ArrayList;

public class ActiveDeckModel {
    private ArrayList<Card> cards;

    public ActiveDeckModel() {
        this.cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
