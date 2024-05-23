package com.if2210.app.factory;

import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.AnimalCardModel.AnimalType;

public class AnimalCardFactory {
    public static String[] animalNames = { "Hiu Darat", "Sapi", "Domba", "Kuda", "Ayam", "Beruang" };

    public static AnimalCardModel createAnimalCard(String animalName) {
        switch (animalName) {
            case "Hiu Darat":
                return new AnimalCardModel("#D4E3FC", "Hiu Darat",
                        "/com/if2210/app/assets/hiu-darat.png", 0, 20, AnimalType.CARNIVORE, null);
            case "Sapi":
                return new AnimalCardModel("#D4E3FC", "Sapi",
                        "/com/if2210/app/assets/cow.png", 0, 10, AnimalType.HERBIVORE, null);
            case "Domba":
                return new AnimalCardModel("#D4E3FC", "Domba",
                        "/com/if2210/app/assets/sheep.png", 0, 12, AnimalType.HERBIVORE, null);
            case "Kuda":
                return new AnimalCardModel("#D4E3FC", "Kuda",
                        "/com/if2210/app/assets/horse.png", 0, 14, AnimalType.HERBIVORE, null);
            case "Ayam":
                return new AnimalCardModel("#D4E3FC", "Ayam",
                        "/com/if2210/app/assets/chicken.png", 0, 5, AnimalType.OMNIVORE, null);
            case "Beruang":
                return new AnimalCardModel("#D4E3FC", "Beruang",
                        "/com/if2210/app/assets/bear.png", 0, 25, AnimalType.OMNIVORE, null);
            default:
                return null;
        }
    }
}
