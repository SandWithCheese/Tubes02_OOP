package com.if2210.app.view;

import java.util.Map;

import com.if2210.app.datastore.DataManager;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class SaveView {
    private int currentTurn;
    private Map<ProductCardModel, Integer> productList;
    private PlayerModel player1;
    private PlayerModel player2;
    private DataManager dataManager;

    @FXML
    private ComboBox<String> myCb;

    @FXML
    private TextField folderNameField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loadButton;

    private String[] typeFile = { "txt", "xml", "json" };

    public SaveView(DataManager dataManager, int currentTurn, Map<ProductCardModel, Integer> productList,
            PlayerModel player1, PlayerModel player2) {
        this.dataManager = dataManager;
        this.currentTurn = currentTurn;
        this.productList = productList;
        this.player1 = player1;
        this.player2 = player2;
    }

    @FXML
    public void initialize() {
        System.out.println("Save initialized");
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
                } else {
                    setText(null);
                }
            }
        });

    }

    @FXML
    private void handleSaveButtonAction() {
        String selectedFormat = myCb.getValue();
        String folderName = folderNameField.getText();

        if (folderName.isEmpty()) {
            messageLabel.setText("Folder Name cannot be empty.");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            dataManager.save(folderName, currentTurn, productList, player1, player2);
            
            // Using threading, update the message label every 1 second for 5 seconds and
            // then close the window
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        final int countdown = 5 - i;
                        Platform.runLater(() -> {
                            messageLabel.setText("Berhasil menyimpan di folder " + folderName
                                    + " dengan file berekstensi " + selectedFormat + ".\nWindow akan ditutup dalam "
                                    + countdown + " detik.");
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Platform.runLater(() -> {
                        Stage stage = (Stage) messageLabel.getScene().getWindow();
                        stage.close();
                    });
                }
            };
            Thread thread = new Thread(task);
            thread.start();
        }
    }
}
