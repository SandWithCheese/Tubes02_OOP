package com.if2210.app;

import com.if2210.app.controller.GUIController;
import com.if2210.app.factory.AnimalCardFactory;
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
        controller.updateCard(controller.findDeckById("ActiveDeck3"), AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));
        random = (int) (Math.random() * 6);
        controller.updateCard(controller.findDeckById("ActiveDeck1"), AnimalCardFactory.createAnimalCard(AnimalCardFactory.animalNames[random]));

        // Load the logo image for taskbar logo
        String iconPath = "/com/if2210/app/assets/Anya.png";
        stage.getIcons().add(new javafx.scene.image.Image(iconPath));

        stage.setTitle("MoliNana");
        stage.setScene(scene);
        stage.show();
    }
}
