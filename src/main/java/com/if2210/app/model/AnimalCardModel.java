package com.if2210.app.model;

import java.util.ArrayList;

public class AnimalCardModel extends CardModel {
    public enum AnimalType {
        HERBIVORE,
        CARNIVORE,
        OMNIVORE
    }

    private int currentWeight;
    private int harvestWeight;
    private AnimalType type;

    private ArrayList<ItemCardModel> activeItems;

    public AnimalCardModel(String color, String name, String image, int currentWeight, int harvestWeight,
            AnimalType type, ArrayList<ItemCardModel> activeItem) {
        super(color, name, image);
        this.currentWeight = currentWeight;
        this.harvestWeight = harvestWeight;
        this.type = type;

        if (activeItem != null) {
            this.activeItems = activeItem;
        } else {
            this.activeItems = new ArrayList<ItemCardModel>();
        }
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(int currentWeight) {
        this.currentWeight = currentWeight;
    }

    public int getHarvestWeight() {
        return harvestWeight;
    }

    public void setHarvestWeight(int harvestWeight) {
        this.harvestWeight = harvestWeight;
    }

    public AnimalType getType() {
        return type;
    }

    public void setType(AnimalType type) {
        this.type = type;
    }

    public ArrayList<ItemCardModel> getActiveItems() {
        return activeItems;
    }

    public void setActiveItems(ArrayList<ItemCardModel> activeItems) {
        this.activeItems = activeItems;
    }
}
