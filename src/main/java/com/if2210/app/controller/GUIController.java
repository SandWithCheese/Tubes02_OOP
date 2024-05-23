package com.if2210.app.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.if2210.app.model.CardModel;
import com.if2210.app.model.GameManagerModel;
import com.if2210.app.view.LoadView;
import com.if2210.app.view.SaveView;

public class GUIController {
    public static final String BLANK_IMAGE = "/com/if2210/app/assets/blank.png";
    private GameManagerModel gameManagerModel;

    @FXML
    private Label gulden1;

    @FXML
    private Label gulden2;

    @FXML
    private Label deckCount;

    @FXML
    private Label gameTurn;

    @FXML
    private Group activeDeckGroup;

    @FXML
    private Group fieldCardGroup;

    @FXML
    private AnchorPane shopDrop;

    public List<AnchorPane> activeDecks = new ArrayList<>();
    public List<AnchorPane> fieldCards = new ArrayList<>();

    public GUIController() {
        this.gameManagerModel = new GameManagerModel();
    }

    @FXML
    public void initialize() {
        initializeDecks(activeDeckGroup, activeDecks, 6);
        initializeDecks(fieldCardGroup, fieldCards, 20);
        setupDragAndDrop();

        gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
        gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
        deckCount.setText("My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
    }

    // open popup
    public void handleOpenLoad() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Load.fxml"));
            Parent root = loader.load();

            Stage childStage = new Stage();
            childStage.setTitle("Load");
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
            childStage.setScene(new Scene(root));

            // Load the logo image for taskbar logo
            String iconPath = "/com/if2210/app/assets/Anya.png";
            childStage.getIcons().add(new javafx.scene.image.Image(iconPath));

            childStage.showAndWait();
            System.out.println(LoadView.getFolderName());
            System.out.println(LoadView.getExt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleOpenShop() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Shop.fxml"));
            Parent root = loader.load();

            Stage childStage = new Stage();
            childStage.setTitle("Shop");
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
            childStage.setScene(new Scene(root));

            // Load the logo image for taskbar logo
            String iconPath = "/com/if2210/app/assets/Anya.png";
            childStage.getIcons().add(new javafx.scene.image.Image(iconPath));

            childStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleOpenLoadPlugin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/LoadPlugin.fxml"));
            Parent root = loader.load();

            Stage childStage = new Stage();
            childStage.setTitle("Load Plugin");
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
            childStage.setScene(new Scene(root));

            // Load the logo image for taskbar logo
            String iconPath = "/com/if2210/app/assets/Anya.png";
            childStage.getIcons().add(new javafx.scene.image.Image(iconPath));

            childStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleOpenSave() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Save.fxml"));
            Parent root = loader.load();

            Stage childStage = new Stage();
            childStage.setTitle("Save");
            childStage.initModality(Modality.APPLICATION_MODAL);
            childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
            childStage.setScene(new Scene(root));

            // Load the logo image for taskbar logo
            String iconPath = "/com/if2210/app/assets/Anya.png";
            childStage.getIcons().add(new javafx.scene.image.Image(iconPath));

            childStage.showAndWait();
            System.out.println(SaveView.getFolderName());
            System.out.println(SaveView.getExt());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeDecks(Group deckGroup, List<AnchorPane> decks, int count) {
        for (int i = 0; i < count; i++) {
            try {
                AnchorPane anchorPane = (AnchorPane) deckGroup.getChildren().get(i);
                if (!anchorPane.getChildren().isEmpty()) {
                    AnchorPane deck = (AnchorPane) anchorPane.getChildren().get(0);
                    decks.add(deck);
                    CardModel cardData = new CardModel("", "", BLANK_IMAGE);
                    updateCard(deck, cardData);
                }
            } catch (Exception e) {
                // Handle exception
            }
        }
    }

    private void setupDragAndDrop() {
        for (AnchorPane activeDeck : activeDecks) {
            setDragDetected(activeDeck);
        }

        for (AnchorPane activeCardField : fieldCards) {
            setDragDetected(activeCardField);
            setDragOver(activeCardField);
            setDragDropped(activeCardField);
        }

        setDragOver(shopDrop);
        setDragDropped(shopDrop);
    }

    private void setDragDetected(AnchorPane deck) {
        deck.setOnDragDetected(event -> {
            System.out.println("Drag Detected");
            Dragboard dragboard = deck.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(deck.getId());
            dragboard.setContent(content);
            event.consume();
        });
    }

    private void setDragOver(AnchorPane deck) {
        deck.setOnDragOver(event -> {
            if (event.getGestureSource() != deck && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }

    private void setDragDropped(AnchorPane targetCard) {
        targetCard.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            String sourceCardId = dragboard.getString();
            if (targetCard.getId().equals("shopDrop")) {
                if (sourceCardId.startsWith("ActiveDeck")) {
                    System.out.println("SHOPEE COD");
                } else {
                    System.err.println("Illegal Move");
                }
            } else {
                if (dragboard.hasString()) {
                    System.out.println("Dropped from " + sourceCardId + " to " + targetCard.getId());
                    AnchorPane sourceCard = findDeckById(sourceCardId);
                    if (sourceCard != null) {
                        CardModel sourceCardData = (CardModel) sourceCard.getUserData();
                        CardModel targetCardData = (CardModel) targetCard.getUserData();
                        if (!sourceCardData.getImage().equals(BLANK_IMAGE) && !(sourceCardId.startsWith("ActiveDeck")
                                && !targetCardData.getImage().equals(BLANK_IMAGE))) {
                            updateCard(sourceCard, targetCardData);
                            updateCard(targetCard, sourceCardData);
                            success = true;
                        } else {
                            System.err.println("Illegal Move");
                        }
                    } else {
                        System.err.println("Source ActiveDeck not found");
                    }
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    public AnchorPane findDeckById(String id) {
        for (AnchorPane deck : activeDecks) {
            if (deck.getId().equals(id)) {
                return deck;
            }
        }
        for (AnchorPane deck : fieldCards) {
            if (deck.getId().equals(id)) {
                return deck;
            }
        }
        return null;
    }

    public void updateCard(AnchorPane card, CardModel cardData) {
        card.setUserData(new CardModel(cardData.getColor(), cardData.getName(), cardData.getImage()));

        ImageView imageView = (ImageView) card.getChildren().get(0);
        Image image = new Image(getClass().getResourceAsStream(cardData.getImage()));
        imageView.setImage(image != null ? image : new Image(BLANK_IMAGE)); // Use blank image if resource not found
        Label label = (Label) card.getChildren().get(1);
        label.setText(cardData.getName());

        card.setStyle(null);
        // Update AnchorPane background color based on the color attribute of the card
        // model
        String color = cardData.getColor();
        if (color != null && !color.isEmpty()) {
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 7.7px;");
        }
    }

    public void clearCard(AnchorPane source) {
        CardModel emptyData = new CardModel("", "", BLANK_IMAGE);
        source.setUserData(emptyData);
        updateCard(source, emptyData);
    }
}