package com.if2210.app.model;

import java.util.ArrayList;

public class PlantCardModel extends CardModel {
    private int age;
    private ArrayList<ItemCardModel> activeItems;

    public PlantCardModel(String color, String name, String image, int age, ArrayList<ItemCardModel> activeItem) {
        super(color, name, image);
        this.age = age;
        this.activeItems = activeItem;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
