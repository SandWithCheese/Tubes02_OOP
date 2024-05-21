package com.if2210.app.model;

public class ProductCardModel extends CardModel {
    private int price;
    private int addedWeight;

    public ProductCardModel(String color, String name, String image, int price, int addedWeight) {
        super(color, name, image);
        this.price = price;
        this.addedWeight = addedWeight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAddedWeight() {
        return addedWeight;
    }

    public void setAddedWeight(int addedWeight) {
        this.addedWeight = addedWeight;
    }
}
