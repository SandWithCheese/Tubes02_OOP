package com.if2210.app.model;

public class CardModel {
    private String color;
    private String name;
    private String image; // Path to source image

    public CardModel(String color, String name, String image) {
        this.color = color;
        this.name = name;
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
