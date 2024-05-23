package com.if2210.app.factory;

import com.if2210.app.model.ProductCardModel;

public class ProductCardFactory {
    public static String[] productNames = { "Sirip Hiu", "Susu", "Daging Domba", "Daging Kuda", "Telur",
            "Daging Beruang",
            "Jagung", "Labu", "Stroberi" };

    public static ProductCardModel createProductCard(String productName) {
        switch (productName) {
            case "Sirip Hiu":
                return new ProductCardModel("#D4E3FC", "Sirip Hiu",
                        "/com/if2210/app/assets/shark-fin.png", 500, 12);
            case "Susu":
                return new ProductCardModel("#D4E3FC", "Susu",
                        "/com/if2210/app/assets/susu.png", 100, 4);
            case "Daging Domba":
                return new ProductCardModel("#D4E3FC", "Daging Domba",
                        "/com/if2210/app/assets/mutton.png", 120, 6);
            case "Daging Kuda":
                return new ProductCardModel("#D4E3FC", "Daging Kuda",
                        "/com/if2210/app/assets/horse-meat.png", 150, 8);
            case "Telur":
                return new ProductCardModel("#D4E3FC", "Telur",
                        "/com/if2210/app/assets/telur.png", 50, 2);
            case "Daging Beruang":
                return new ProductCardModel("#D4E3FC", "Daging Beruang",
                        "/com/if2210/app/assets/bear-meat.png", 500, 12);
            case "Jagung":
                return new ProductCardModel("#D4E3FC", "Jagung",
                        "/com/if2210/app/assets/corn.png", 150, 3);
            case "Labu":
                return new ProductCardModel("#D4E3FC", "Labu",
                        "/com/if2210/app/assets/pumpkin.png", 500, 10);
            case "Stroberi":
                return new ProductCardModel("#D4E3FC", "Stroberi",
                        "/com/if2210/app/assets/strawberry.png", 350, 5);
            default:
                return null;
        }
    }
}
