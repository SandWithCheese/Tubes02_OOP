package com.if2210.app;

import com.if2210.app.controller.GUIController;
import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.CardModel;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/GUI.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1228, 768);

        GUIController controller = loader.getController();
        int random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck3"), AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random%AnimalCardFactory.animalNames.length]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck1"), AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random%AnimalCardFactory.animalNames.length]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck2"), PlantCardFactory.createPlantCard(PlantCardFactory.plantNames[random%PlantCardFactory.plantNames.length]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck5"), PlantCardFactory.createPlantCard(PlantCardFactory.plantNames[random%PlantCardFactory.plantNames.length]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck4"), ProductCardFactory.createProductCard(ProductCardFactory.productNames[random%ProductCardFactory.productNames.length]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck6"), ProductCardFactory.createProductCard(ProductCardFactory.productNames[random%ProductCardFactory.productNames.length]));

        stage.setTitle("MoliNana");
        stage.setScene(scene);
        stage.show();
    }
}
