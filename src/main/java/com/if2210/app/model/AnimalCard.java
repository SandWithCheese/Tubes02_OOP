package com.if2210.app.model;

public class AnimalCard extends Card {
    public enum AnimalType {
        HERBIVORE,
        CARNIVORE,
        OMNIVORE
    }

    private int weight;
    private AnimalType type;

    public AnimalCard(String color, String name, String image, int weight, AnimalType type) {
        super(color, name, image);
        this.weight = weight;
        this.type = type;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }
}
