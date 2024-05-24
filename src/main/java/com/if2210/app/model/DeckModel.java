package com.if2210.app.model;

import java.util.ArrayList;

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;

// DeckModel class adalah class yang merepresentasikan deck yang dimiliki oleh pemain
// DeckModel memiliki panjang maksimal 40 yang berarti maksimal kartu yang dapat dimiliki oleh pemain adalah 40
public class DeckModel {
    private ArrayList<CardModel> cards;

    public DeckModel() {
        // Mengenerate 40 kartu acak dengan ketentuan sebagai berikut:
        // 12 kartu hewan
        // 12 kartu tanaman
        // 8 kartu produk
        // 8 kartu item
        // 40 kartu tersebut akan diacak dan diletakkan ke dalam deck

        this.cards = new ArrayList<CardModel>();
        // Generate 12 random animal cards
        for (int i = 0; i < 12; i++) {
            int random = (int) (Math.random() * 6);
            while (AnimalCardFactory.animalNames[random].equals("Beruang")) {
                random = (int) (Math.random() * 6);
            }
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        }

        // Generate 12 random plant cards
        for (int i = 0; i < 12; i++) {
            int random = (int) (Math.random() * 3);
            this.cards.add(PlantCardFactory.createPlantCard(PlantCardFactory.plantNames[random]));
        }

        // Generate 8 random product cards
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * 9);
            this.cards.add(ProductCardFactory.createProductCard(ProductCardFactory.productNames[random]));
        }

        // Generate 8 random item cards
        for (int i = 0; i < 8; i++) {
            int random = (int) (Math.random() * 6);
            this.cards.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[random]));
        }
    }

    public DeckModel(int deckCount) {
        // Mengenerate deckCount kartu acak dengan ketentuan sebagai berikut:
        // 30% kartu hewan
        // 30% kartu tanaman
        // 20% kartu produk
        // Sisanya kartu item
        // deckCount kartu tersebut akan diacak dan diletakkan ke dalam deck

        this.cards = new ArrayList<CardModel>();
        int animalCount = (int) (deckCount * 0.3);
        int plantCount = (int) (deckCount * 0.3);
        int productCount = (int) (deckCount * 0.2);
        int itemCount = deckCount - animalCount - plantCount - productCount;

        // Generate animal cards
        for (int i = 0; i < animalCount; i++) {
            int random = (int) (Math.random() * 6);
            while (AnimalCardFactory.animalNames[random].equals("Beruang")) {
                random = (int) (Math.random() * 6);
            }
            this.cards.add(AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        }

        // Generate plant cards
        for (int i = 0; i < plantCount; i++) {
            int random = (int) (Math.random() * 3);
            this.cards.add(PlantCardFactory.createPlantCard(PlantCardFactory.plantNames[random]));
        }

        // Generate product cards
        for (int i = 0; i < productCount; i++) {
            int random = (int) (Math.random() * 9);
            this.cards.add(ProductCardFactory.createProductCard(ProductCardFactory.productNames[random]));
        }

        // Generate item cards
        for (int i = 0; i < itemCount; i++) {
            int random = (int) (Math.random() * 6);
            this.cards.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[random]));
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
