package com.if2210.app.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class LoadView {
    private String fileName;

    @FXML
    private ComboBox<String> myCb;

    @FXML
    private TextField filenameField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loadButton;
    
    private String[] typeFile={"txt", "xml", "json"};
    public LoadView() {
        this.fileName = null;
    }

    @FXML
    public void initialize(){
        System.out.println("Load initialized");
        myCb.getItems().addAll(typeFile);

        myCb.setValue("txt");

        // Set font and padding
        myCb.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

        // Ensure the drop-down list is as wide as the ChoiceBox
        myCb.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                final ListCell<String> cell = new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            // setPrefWidth(myCb.getPrefWidth());
                        } else {
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        // Ensure the button cell is also wide enough
        myCb.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    // setPrefWidth(myCb.getPrefWidth());
                } else {
                    setText(null);
                }
            }
        });

        
    }

    @FXML
    private void handleLoadButtonAction() {
        String selectedFormat = myCb.getValue();
        String filename = filenameField.getText();

        this.fileName = filename+"."+selectedFormat;

        if (filename.isEmpty()) {
            messageLabel.setText("Filename cannot be empty.");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            // Handle the loading process (this is just a placeholder)
            messageLabel.setText("Berhasil membaca dari " + filename + "." + selectedFormat);
            messageLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        }
    }

    public String getFilename(){return fileName;}

}
