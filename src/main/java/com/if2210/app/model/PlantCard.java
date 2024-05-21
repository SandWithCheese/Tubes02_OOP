package com.if2210.app.model;

import java.util.ArrayList;

public class PlantCard extends Card {
    private int age;
    private ArrayList<ItemCard> activeItems;

    public PlantCard(String color, String name, String image, int age, ArrayList<ItemCard> activeItem) {
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

    public ArrayList<ItemCard> getActiveItems() {
        return activeItems;
    }

    public void setActiveItems(ArrayList<ItemCard> activeItems) {
        this.activeItems = activeItems;
    }

    public void useItem(ItemCard item) {
        if (this.activeItems.get(this.activeItems.indexOf(item)) != null) {
            System.out.println("Using item: " + this.activeItems.get(this.activeItems.indexOf(item)).getName());
        } else {
            System.out.println("No active item.");
        }
    }
}
