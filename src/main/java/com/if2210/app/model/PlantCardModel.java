package com.if2210.app.model;

import java.util.ArrayList;

public class PlantCardModel extends CardModel {
    private int currentAge;
    private int harvestAge;
    private ArrayList<ItemCardModel> activeItems;

    public PlantCardModel(String color, String name, String image, int currentAge, int harvestAge,
            ArrayList<ItemCardModel> activeItem) {
        super(color, name, image);
        this.currentAge = currentAge;
        this.harvestAge = harvestAge;
        if (activeItem != null) {
            this.activeItems = activeItem;
        } else {
            this.activeItems = new ArrayList<ItemCardModel>();
        }
    }

    public int getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(int age) {
        this.currentAge = age;
    }

    public int getHarvestAge() {
        return harvestAge;
    }

    public void setHarvestAge(int age) {
        this.harvestAge = age;
    }

    public ArrayList<ItemCardModel> getActiveItems() {
        return activeItems;
    }

    public void setActiveItems(ArrayList<ItemCardModel> activeItems) {
        this.activeItems = activeItems;
    }

    public void useItem(ItemCardModel item) {
        if (this.activeItems.get(this.activeItems.indexOf(item)) != null) {
            System.out.println("Using item: " + this.activeItems.get(this.activeItems.indexOf(item)).getName());
        } else {
            System.out.println("No active item.");
        }
    }
}
