package com.if2210.app.factory;

import com.if2210.app.model.PlantCardModel;

public class PlantCardFactory {
    public static String[] plantNames = { "Biji Jagung", "Biji Labu", "Biji Stroberi" };

    public static PlantCardModel createPlantCard(String name) {
        switch (name) {
            case "Biji Jagung":
                return new PlantCardModel("#D4E3FC", "Biji Jagung",
                        "/com/if2210/app/assets/corn-seeds.png", 0, 3, null);
            case "Biji Labu":
                return new PlantCardModel("#D4E3FC", "Biji Labu",
                        "/com/if2210/app/assets/pumpkin-seeds.png", 0, 5, null);
            case "Biji Stroberi":
                return new PlantCardModel("#D4E3FC", "Biji Stroberi",
                        "/com/if2210/app/assets/strawberry-seeds.png", 0, 4, null);
            default:
                return null;
        }
    }
}
