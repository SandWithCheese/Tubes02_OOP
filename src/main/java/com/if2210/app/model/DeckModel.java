package com.if2210.app.model;

import java.util.ArrayList;

import com.if2210.app.factory.AnimalCardFactory;

// DeckModel class adalah class yang merepresentasikan deck yang dimiliki oleh pemain
// DeckModel memiliki panjang maksimal 40 yang berarti maksimal kartu yang dapat dimiliki oleh pemain adalah 40
public class DeckModel {
    private ArrayList<CardModel> cards;

    public DeckModel() {
        this.cards = new ArrayList<CardModel>();
        // Mengenerate 40 kartu acak dengan ketentuan sebagai berikut:
        // 12 kartu hewan
        // 12 kartu tanaman
        // 8 kartu produk
        // 8 kartu item
        // 40 kartu tersebut akan diacak dan diletakkan ke dalam deck

        // Generate 12 random animal cards
        for (int i = 0; i < 12; i++) {
            int random = (int) (Math.random() * 6);
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        }

        // Generate 12 random plant cards
        for (int i = 0; i < 12; i++) {
            int random = (int) (Math.random() * 3);
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        }

        // Generate 8 random product cards
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * 9);
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        }

        // Generate 8 random item cards
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * 3);
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
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

    public void removeCard(CardModel card) {
        this.cards.remove(card);
    }

    public void removeCard(int index) {
        this.cards.remove(index);
    }

    public int getDeckSize() {
        return this.cards.size();
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }
}
