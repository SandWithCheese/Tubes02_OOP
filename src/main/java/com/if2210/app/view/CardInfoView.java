package com.if2210.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.if2210.app.controller.GUIController;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.ProductCardModel;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CardInfoView {
    private Object card;

    private AnchorPane cardPane;

    private GUIController controller;

    private GameManagerModel gm;

    private Map<String, String> resProd = new HashMap<>();

    private int weightAfterActiveItem = 0;

    private static ProductCardModel productItem;

    @FXML
    private Label weight;

    @FXML
    private Label harvestWeight;

    @FXML
    private Label name;

    @FXML
    private ImageView image;

    @FXML
    private Label info;

    @FXML
    private Label items;

    @FXML
    private Label harvestResponse;

    @FXML
    private Button harvestButton;

    public CardInfoView(AnchorPane deck, GUIController gui, GameManagerModel gm) {
        card = deck.getUserData();
        this.gm = gm;
        cardPane = deck;
        controller = gui;
        resProd.put("Hiu Darat", "Sirip Hiu");
        resProd.put("Sapi", "Susu");
        resProd.put("Domba", "Daging Domba");
        resProd.put("Kuda", "Daging Kuda");
        resProd.put("Ayam", "Telur");
        resProd.put("Beruang", "Daging Beruang");
        resProd.put("Biji Jagung", "Jagung");
        resProd.put("Biji Labu", "Labu");
        resProd.put("Biji Stroberi", "Stroberi");
        resProd.put("Jagung", "Jagung");
        resProd.put("Labu", "Labu");
        resProd.put("Stroberi", "Stroberi");
    }

    private ArrayList<String> mapToList(Map<String, Integer> map) {
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String str = entry.getKey() + " (" + entry.getValue() + ")";
            list.add(str);
        }
        return list;
    }

    private Map<String, Integer> listToMap(ArrayList<String> list) {
        Map<String, Integer> map = new HashMap<>();

        for (String name : list) {
            if (map.containsKey(name)) {
                map.put(name, map.get(name) + 1);
            } else {
                map.put(name, 1);
            }
        }
        return map;
    }

    private void initInfoAnimal() {
        AnimalCardModel animalCard = (AnimalCardModel) card;
        int weightFromActiveItem = 0;
        for (ItemCardModel item : animalCard.getActiveItems()) {
            if (item.getName().equals("Accelerate")) {
                weightFromActiveItem += 8;
            } else if (item.getName().equals("Delay")) {
                weightFromActiveItem -= 5;
            }
        }

        this.weightAfterActiveItem = animalCard.getCurrentWeight() + weightFromActiveItem;
        if (this.weightAfterActiveItem < 0) {
            this.weightAfterActiveItem = 0;
        }

        String weightText = "Weight : " + animalCard.getCurrentWeight() + " (" + this.weightAfterActiveItem + ")";
        weight.setText(weightText);
        harvestWeight.setText("Harvest weight : " + animalCard.getHarvestWeight());
        if (weightAfterActiveItem >= animalCard.getHarvestWeight()) {
            if(!controller.isEnemyField){
                info.setText("Ready to be harvest");
                info.setTextFill(javafx.scene.paint.Color.GREEN);
                harvestButton.setVisible(true);
            }else{
                info.setText("This is not yours");
                info.setTextFill(javafx.scene.paint.Color.RED);
            }
        } else {
            info.setText("Your animal isn't ready to be harvested yet");
            info.setTextFill(javafx.scene.paint.Color.RED);
        }

        ArrayList<ItemCardModel> itemList = animalCard.getActiveItems();
        ArrayList<String> nameItems = new ArrayList<>();
        for (ItemCardModel item : itemList) {
            nameItems.add(item.getName());
        }

        Map<String, Integer> map = listToMap(nameItems);
        ArrayList<String> result = mapToList(map);
        System.out.println(itemList.size());
        items.setText("Active Items: \n"+result.toString().substring(1, result.toString().length() - 1));
    }

    public void initInfoPlant() {
        PlantCardModel plantCard = (PlantCardModel) card;
        int ageFromActiveItem = 0;
        for (ItemCardModel item : plantCard.getActiveItems()) {
            if (item.getName().equals("Accelerate")) {
                ageFromActiveItem += 2;
            } else if (item.getName().equals("Delay")) {
                ageFromActiveItem -= 2;
            }
        }

        this.weightAfterActiveItem = plantCard.getCurrentAge() + ageFromActiveItem;
        if (this.weightAfterActiveItem < 0) {
            this.weightAfterActiveItem = 0;
        }

        String ageText = "Age : " + plantCard.getCurrentAge() + " (" + this.weightAfterActiveItem + ")";
        weight.setText(ageText);

        harvestWeight.setText("Harvest age : " + plantCard.getHarvestAge());
        if (this.weightAfterActiveItem >= plantCard.getHarvestAge()) {
            if(!controller.isEnemyField){
                info.setText("Ready to be harvest");
                info.setTextFill(javafx.scene.paint.Color.GREEN);
                harvestButton.setVisible(true);
            }else{
                info.setText("This is not yours");
                info.setTextFill(javafx.scene.paint.Color.RED);
            }
        } else {
            info.setText("Your plant isn't ready to be harvested yet");
            info.setTextFill(javafx.scene.paint.Color.RED);
        }

        ArrayList<ItemCardModel> itemList = plantCard.getActiveItems();
        ArrayList<String> nameItems = new ArrayList<>();
        for (ItemCardModel item : itemList) {
            nameItems.add(item.getName());
        }
        Map<String, Integer> map = listToMap(nameItems);
        ArrayList<String> result = mapToList(map);
        System.out.println(itemList.size());
        items.setText("Active Items: \n"+result.toString().substring(1, result.toString().length() - 1));
    }

    public void initInfoProduct() {
        weight.setText("Price : " + ((ProductCardModel) card).getPrice());
        harvestWeight.setText("Additional weight : " + ((ProductCardModel) card).getAddedWeight());
    }

    public void initInfoItem(){
        if(((CardModel)card).getName().equals("Accelerate")){
            harvestWeight.setText("Description:\nMenambah umur tanaman sebanyak 2 turn atau menambah berat kartu hewan sebesar 8.");
        }else if(((CardModel)card).getName().equals("Delay")){
            harvestWeight.setText("Description:\nMengurangi umur tanaman sebanyak 2 turn (umur tanaman minimal bernilai 0) atau mengurangi berat kartu hewan sebesar 5 (berat hewan minimal bernilai 0).");
        }else if(((CardModel)card).getName().equals("Instant Harvest")){
            harvestWeight.setText("Description:\nMelakukan harvest secara langsung untuk kartu tanaman/hewan yang dipilih.");
        }else if(((CardModel)card).getName().equals("Destroy")){
            harvestWeight.setText("Description:\nMenghancurkan kartu tanaman/hewan lawan.");
        }else if(((CardModel)card).getName().equals("Protect")){
            harvestWeight.setText("Description:\nMelindungi kartu tanaman/hewan diri sendiri dari item yang ditambahkan oleh lawan ke ladang atau serangan beruang.");
        }else if(((CardModel)card).getName().equals("Trap")){
            harvestWeight.setText("Description:\nMengubah beruang menjadi kartu hewan yang dapat diternak apabila menyerang hewan/tanaman yang diberikan item ini.");
        }
    }

    public void initialize() {
        harvestButton.setVisible(false);
        name.setText(((CardModel) card).getName());
        System.out.println(card.getClass().getName());
        if (card instanceof AnimalCardModel) {
            initInfoAnimal();
        } else if (card instanceof PlantCardModel) {
            initInfoPlant();
        } else if(card instanceof ProductCardModel){
            initInfoProduct();
        }else{
            initInfoItem();
        }
        Image img = new Image(((CardModel) card).getImage());
        image.setImage(img);

    }

    private void harvestPlant() {
        System.out.println("Menjalankan proses panen tanaman");
        ProductCardModel produk = ProductCardFactory.createProductCard(resProd.get(((CardModel) card).getName()));
        controller.updateCard(cardPane, produk, true, false);
        productItem = produk;
        Stage stage = (Stage) info.getScene().getWindow();
        stage.close();
    }

    private void harvestAnimal() {
        System.out.println("Menjalankan proses panen hewan");
        ProductCardModel produk = ProductCardFactory.createProductCard(resProd.get(((CardModel) card).getName()));
        controller.updateCard(cardPane, produk, true, false);
        productItem = produk;
        Stage stage = (Stage) info.getScene().getWindow();
        stage.close();
    }

    public void harvestHandler() {
        if(gm.getActivePlayer().getActiveDeck().isFull()){// kalo deck aktif penuh
            info.setText("Your deck is full bro!!");
            info.setTextFill(javafx.scene.paint.Color.RED);
        }else{
            if (card instanceof AnimalCardModel) {
                if (this.weightAfterActiveItem >= ((AnimalCardModel) card).getHarvestWeight()) {
                    harvestAnimal();
                } else {
                    harvestResponse.setText("The animal is not heavy enough to harvest");
                    harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
                }
            } else if (card instanceof PlantCardModel) {
                if (this.weightAfterActiveItem >= ((PlantCardModel) card).getHarvestAge()) {
                    harvestPlant();
                } else {
                    harvestResponse.setText("The plants are not yet mature enough to harvest");
                    harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
                }
            } else {
                harvestResponse.setText("Product is not a harvestable entity");
                harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
            }
        }
    }
    
    public static ProductCardModel getProductItem() {
        ProductCardModel temp = productItem;
        productItem = null;
        return temp;
    }
}
