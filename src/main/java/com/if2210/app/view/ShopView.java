package com.if2210.app.view;

import java.util.Map;

import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;
import com.if2210.app.model.ShopModel;
import com.if2210.app.factory.ProductCardFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ShopView {
    private static ShopModel shop;
    private static PlayerModel player;

    @FXML
    private Label messagelabel;

    @FXML
    private Group productGroup;

    public ShopView(ShopModel Shop, PlayerModel Player) {
        player = Player;
        shop = Shop;
    }

    @FXML
    public void initialize(){
        for (Map.Entry<ProductCardModel, Integer> entry : shop.getProductList().entrySet()) {
            ProductCardModel product = entry.getKey();
            Integer amount = entry.getValue();

            String name = product.getName().replaceAll(" ", "").toLowerCase();
            AnchorPane anchorPane = findAnchorPaneInGroup(productGroup, name);
            Label amountLabel = findLabelInGroup(anchorPane, "amount");

            if (amountLabel != null) {
                amountLabel.setText(String.valueOf(amount));
            }
        }
        
        System.out.println("ShopView initialized");
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (player.getActiveDeck().isFull()) {
            messagelabel.setText("Active Deck is full");
        } else {
            AnchorPane parent = (AnchorPane) button.getParent().getParent();
            Label priceLabel = findLabelInGroup(parent, "price");

            if (priceLabel != null) {
                if (player.getMoney() < Integer.parseInt(priceLabel.getText())) {
                    messagelabel.setText("Gulden is not enough");
                    return;
                } else {
                    Label amountLabel = findLabelInGroup(parent, "amount");

                    if (amountLabel != null) {
                        int currentAmount = Integer.parseInt(amountLabel.getText());
                        if (currentAmount == 0) {
                            messagelabel.setText("Produk tersebut out of stock");
                        } else {
                            String productname = findLabelInAnchorPane((AnchorPane) parent.getChildren().getFirst());

                            player.setMoney(player.getMoney() - Integer.parseInt(priceLabel.getText()));

                            for (Map.Entry<ProductCardModel, Integer> entry : shop.getProductList().entrySet()) {
                                ProductCardModel product = entry.getKey();
                                String productName = product.getName();

                                if (productName.equals(productname)) {
                                    try {
                                        shop.decreaseProduct(product);
                                    } catch(Exception e) {
                                        System.out.println("mASUK");
                                    }
                                }
                            }
                            
                            amountLabel.setText(String.valueOf(currentAmount - 1));
                            
                            for (int i=0; i<6; i++) {
                                if (player.getActiveDeck().getCard(i) == null) {
                                    player.getActiveDeck().setCard(i, ProductCardFactory.createProductCard(productname));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Label findLabelInGroup(AnchorPane anchorPane, String idPrefix) {
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Group) {
                Group group = (Group) node;
                for (Node groupNode : group.getChildren()) {
                    if (groupNode instanceof Label && ((Label) groupNode).getId() != null && ((Label) groupNode).getId().startsWith(idPrefix)) {
                        return (Label) groupNode;
                    }
                }
            }
        }
        return null;
    }

    // get all label in anchorpane and merge it in one string divide by space
    private String findLabelInAnchorPane(AnchorPane anchorPane) {
        String result = "";
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof Label) {
                result += ((Label) node).getText() + " ";
            }
        }
        result = result.substring(0, result.length() - 1);
        return result;
    }

    private AnchorPane findAnchorPaneInGroup(Group group, String idString) {
        for (Node node : group.getChildren()) {
            if (node instanceof AnchorPane && ((AnchorPane) node).getId() != null && ((AnchorPane) node).getId().equals(idString)) {
                return (AnchorPane) node;
            }
        }
        return null;
    }

    public static ShopModel getShop() {
        return shop;
    }

    public static PlayerModel getPlayer() {
        return player;
    }
}
