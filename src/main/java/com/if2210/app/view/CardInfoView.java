package com.if2210.app.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.ProductCardModel;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CardInfoView {
    private Object card;

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

    public CardInfoView(AnchorPane deck){
        card = deck.getUserData();
    }

    private ArrayList<String> mapToList(Map<String, Integer> map){
        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String str = entry.getKey() + " (" + entry.getValue() + ")";
            list.add(str);
        }
        return list;
    }

    private Map<String, Integer> listToMap(ArrayList<String> list){
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

    private void initInfoAnimal(){
        weight.setText("Weight : "+ ((AnimalCardModel)card).getCurrentWeight());
        harvestWeight.setText("Harvest weight : "+((AnimalCardModel)card).getHarvestWeight());
        if(((AnimalCardModel)card).getCurrentWeight()>=((AnimalCardModel)card).getHarvestWeight()){
            info.setText("Ready to be harvest");
            info.setTextFill(javafx.scene.paint.Color.GREEN);
        }else{
            info.setText("Your animal isn't ready to be harvested yet");
            info.setTextFill(javafx.scene.paint.Color.RED);
        }

        // gunakan untuk testing
        ArrayList<ItemCardModel> temp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            temp.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[1]));              
        }
        for (int i = 0; i < 3; i++) {
            int random = (int) (Math.random() * 6);
            temp.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[random]));              
        }
        ((AnimalCardModel)card).setActiveItems(temp);
        // -------------------------------

        ArrayList<ItemCardModel> itemList = ((AnimalCardModel)card).getActiveItems();
        ArrayList<String> nameItems = new ArrayList<>();
        for (ItemCardModel item : itemList) {
            nameItems.add(item.getName());
            
        }
        Map<String, Integer> map = listToMap(nameItems);
        ArrayList<String> result = mapToList(map);
        System.out.println(itemList.size());
        items.setText(result.toString());
    }

    public void initInfoPlant(){
        weight.setText("Age : "+ ((PlantCardModel)card).getCurrentAge());
        harvestWeight.setText("Harvest age : "+((PlantCardModel)card).getHarvestAge());
        if(((PlantCardModel)card).getCurrentAge()>=((PlantCardModel)card).getHarvestAge()){
            info.setText("Ready to be harvest");
            info.setTextFill(javafx.scene.paint.Color.GREEN);
        }else{
            info.setText("Your plant isn't ready to be harvested yet");
            info.setTextFill(javafx.scene.paint.Color.RED);
        }

        // gunakan untuk testing
        ArrayList<ItemCardModel> temp = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            temp.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[1]));              
        }
        for (int i = 0; i < 3; i++) {
            int random = (int) (Math.random() * 6);
            temp.add(ItemCardFactory.createItemCard(ItemCardFactory.itemNames[random]));              
        }
        ((PlantCardModel)card).setActiveItems(temp);
        // -------------------------------

        ArrayList<ItemCardModel> itemList = ((PlantCardModel)card).getActiveItems();
        ArrayList<String> nameItems = new ArrayList<>();
        for (ItemCardModel item : itemList) {
            nameItems.add(item.getName());
            
        }
        Map<String, Integer> map = listToMap(nameItems);
        ArrayList<String> result = mapToList(map);
        System.out.println(itemList.size());
        items.setText(result.toString());
    }

    public void initInfoProduct(){
        weight.setText("Price : "+ ((ProductCardModel)card).getPrice());
        harvestWeight.setText("Additional weight : "+((ProductCardModel)card).getAddedWeight());
    }

    public void initialize(){
        name.setText(((CardModel)card).getName());
        System.out.println(card.getClass().getName());
        if(card instanceof AnimalCardModel){
            initInfoAnimal();
        }else if(card instanceof PlantCardModel){
            initInfoPlant();
        }else{
            initInfoProduct();
        }
        Image img = new Image(((CardModel)card).getImage());
        image.setImage(img);

    }

    private void harvestPlant(){
        System.out.println("Menjalankan proses panen tanaman");
    }
    
    private void harvestAnimal(){
        System.out.println("Menjalankan proses panen hewan");

    }

    public void harvestHandler(){
        if(card instanceof AnimalCardModel){
            if(((AnimalCardModel)card).getCurrentWeight()>=((AnimalCardModel)card).getHarvestWeight()){
                harvestAnimal();
            }else{
                harvestResponse.setText("The animal is not heavy enough to harvest");
                harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
            }
        }else if(card instanceof PlantCardModel){
            if(((PlantCardModel)card).getCurrentAge()>=((PlantCardModel)card).getHarvestAge()){
                harvestPlant();
            }else{
                harvestResponse.setText("The plants are not yet mature enough to harvest");
                harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
            }
        }else{
            harvestResponse.setText("Product is not a harvestable entity");
            harvestResponse.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}
