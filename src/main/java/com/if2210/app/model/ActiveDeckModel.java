package com.if2210.app.model;

import java.util.ArrayList;

// ActiveDeckModel class adalah class yang merepresentasikan deck yang sedang aktif
// ActiveDeckModel memiliki panjang default 6 yang berarti maksimal kartu yang dapat dimiliki oleh pemain adalah 6
// Slot kosong pada deck akan diisi dengan null
public class ActiveDeckModel {
    private ArrayList<CardModel> cards;

    public ActiveDeckModel() {
        this.cards = new ArrayList<CardModel>();
        for (int i = 0; i < 6; i++) {
            this.cards.add(null);
        }
    }

    public ActiveDeckModel(ActiveDeckModel activeDeckCopy) {
        this.cards = new ArrayList<CardModel>();
        for (CardModel card : activeDeckCopy.getCards()) {
            if (card == null) {
                cards.add(null);
            }
            else {
                cards.add(new CardModel(card));
            }
        }
    }

    public ArrayList<CardModel> getCards() {
        return cards;
    }

    public void setCards(ArrayList<CardModel> cards) {
        this.cards = cards;
    }

    public CardModel getCard(int index) {
        return this.cards.get(index);
    }

    public void setCard(int index, CardModel card) {
        this.cards.set(index, card);
    }

    public void removeCard(int i) {
        this.cards.set(i, null);
    }

    public int getEffectiveDeckSize() {
        int count = 0;
        for (CardModel card : this.cards) {
            if (card != null) {
                count++;
            }
        }
        return count;
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public boolean isFull() {
        return this.getEffectiveDeckSize() == 6;
    }
}
