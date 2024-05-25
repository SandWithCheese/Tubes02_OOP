package com.if2210.app.plugin;

import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;

import java.util.Map;

public interface Extension {
    public void save(String folderName,
                     int currentTurn,
                     Map<ProductCardModel,
                     Integer> productList,
                     PlayerModel player1,
                     PlayerModel player2);
    public  void load(String folderName);
}
