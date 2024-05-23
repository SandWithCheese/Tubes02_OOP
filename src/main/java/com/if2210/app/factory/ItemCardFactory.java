package com.if2210.app.factory;

import com.if2210.app.model.ItemCardModel;

public class ItemCardFactory {
    public static String[] itemNames = { "Accelerate", "Delay", "Instant Harvest", "Destroy", "Protect", "Trap" };

    public static ItemCardModel createItemCard(String name) {
        switch (name) {
            case "Accelerate":
                return new ItemCardModel("#D4E3FC", "Accelerate",
                        "/com/if2210/app/assets/accelerate.png");
            case "Delay":
                return new ItemCardModel("#D4E3FC", "Delay",
                        "/com/if2210/app/assets/delay.png");
            case "Instant Harvest":
                return new ItemCardModel("#D4E3FC", "Instant Harvest",
                        "/com/if2210/app/assets/instant-harvest.png");
            case "Destroy":
                return new ItemCardModel("#D4E3FC", "Destroy",
                        "/com/if2210/app/assets/destroy.png");
            case "Protect":
                return new ItemCardModel("#D4E3FC", "Protect",
                        "/com/if2210/app/assets/protect.png");
            case "Trap":
                return new ItemCardModel("#D4E3FC", "Trap",
                        "/com/if2210/app/assets/bear-trap.png");
            default:
                return null;
        }
    }
}
