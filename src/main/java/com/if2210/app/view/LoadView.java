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

public class LoadView {
    private static int currentTurn;
    private static Map<ProductCardModel, Integer> productList;
    private static PlayerModel player1;
    private static PlayerModel player2;
    private DataManager dataManager;

    @FXML
    private ComboBox<String> myCb;

    @FXML
    private TextField folderNameField;

    @FXML
    private Label messageLabel;

    @FXML
    private Button loadButton;

    private String[] typeFiles = { "txt", "xml", "json" };

    public LoadView() {
        currentTurn = 0;
        productList = null;
        player1 = null;
        player2 = null;
        this.dataManager = new DataManager();
    }

    @FXML
    public void initialize() {
        System.out.println("Load initialized");
        myCb.getItems().addAll(typeFiles);

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
    private void handleLoadButtonAction() {
        String selectedFormat = myCb.getValue();
        String foldername = folderNameField.getText();

        if (foldername.isEmpty()) {
            messageLabel.setText("Folder name cannot be empty.");
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            try {
                dataManager.load(foldername+selectedFormat);
                currentTurn = dataManager.getCurrentTurn();
                productList = dataManager.getProductList();
                player1 = dataManager.getPlayer1();
                player2 = dataManager.getPlayer2();

                // Using threading, update the message label every 1 second for 5 seconds and
                // then close the window
                Runnable task = new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 5; i++) {
                            final int countdown = 5 - i;
                            Platform.runLater(() -> {
                                messageLabel.setText("Berhasil membaca dari folder " + foldername
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
            } catch (Exception e) {
                messageLabel.setText("Folder tidak ditemukan.");
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
        }
    }

    public static int getCurrentTurn() {
        return currentTurn;
    }

    public static Map<ProductCardModel, Integer> getProductList() {
        return productList;
    }

    public static PlayerModel getPlayer1() {
        return player1;
    }

    public static PlayerModel getPlayer2() {
        return player2;
    }
}
