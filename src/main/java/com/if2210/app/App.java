package com.if2210.app;

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

        // Load the logo image for taskbar logo
        String iconPath = "/com/if2210/app/assets/Anya.png";
        stage.getIcons().add(new javafx.scene.image.Image(iconPath));

        stage.setTitle("MoliNana");
        stage.setScene(scene);
        stage.show();
    }
}
