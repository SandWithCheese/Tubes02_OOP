package com.if2210.app.view;

import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.io.File;

public class LoadPluginView {
    // attribut
    @FXML
    private Label fileNameLabel;

    // konstruktor
    // method
    @FXML
    public void clickChoosePlugin(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Plugin");

        // Add extension filter for .jar files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JAR files (*.jar)", "*.jar");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        // Show file name to fileNameLabel
        if (selectedFile != null) {
            fileNameLabel.setText(selectedFile.getName());
        }
        else {
            fileNameLabel.setText("No plugin selected");
        }

    }
}
