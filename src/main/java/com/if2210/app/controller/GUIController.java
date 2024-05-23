package com.if2210.app.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
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

import com.if2210.app.view.CardInfoView;

import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.PlayerModel;

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
    private Button myFieldButton;

    @FXML
    private Button enemyFieldButton;

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

        setupClickCard();


        gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
        gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
        deckCount.setText("My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
        loadActiveDeck(gameManagerModel.getPlayer1());
        loadField(gameManagerModel.getPlayer1());

        myFieldButton.setOnMouseClicked(this::handleMyFieldButtonClick);
        enemyFieldButton.setOnMouseClicked(this::handleEnemyFieldButtonClick);
    }

    private void handleMyFieldButtonClick(MouseEvent event) {
        System.out.println("My Field Button Clicked!");
        loadField(gameManagerModel.getWhoseTurn());
        // Add your logic for handling the click event here
    }

    private void handleEnemyFieldButtonClick(MouseEvent event) {
        System.out.println("Enemy Field Button Clicked!");
        loadField(gameManagerModel.getEnemy());
        // Add your logic for handling the click event here
    }

    private void loadActiveDeck(PlayerModel player) {
        for (int i = 0; i < 6; i++) {
            CardModel cardData = player.getActiveDeck().getCard(i);
            if (cardData != null) {
                updateCard(activeDecks.get(i), cardData, false);
            }
        }
    }

    private void loadField(PlayerModel player) {
        clearField(fieldCards, false);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                CardModel cardData = player.getField().getCard(i, j);
                if (cardData != null) {
                    updateCard(fieldCards.get(i * 5 + j), cardData, false);
                }
            }
        }
    }

    // Open popup methods

    private void initializeDecks(Group deckGroup, List<AnchorPane> decks, int count) {
        for (int i = 0; i < count; i++) {
            try {
                AnchorPane anchorPane = (AnchorPane) deckGroup.getChildren().get(i);
                if (!anchorPane.getChildren().isEmpty()) {
                    AnchorPane deck = (AnchorPane) anchorPane.getChildren().get(0);
                    decks.add(deck);
                    CardModel cardData = new CardModel("", "", BLANK_IMAGE);
                    updateCard(deck, cardData, false);
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
                }
            } else {
                if (dragboard.hasString()) {
                    AnchorPane sourceCard = findDeckById(sourceCardId);
                    if (sourceCard != null) {
                        CardModel sourceCardData = (CardModel) sourceCard.getUserData();
                        CardModel targetCardData = (CardModel) targetCard.getUserData();
                        if (!sourceCardData.getImage().equals(BLANK_IMAGE) && !(sourceCardId.startsWith("ActiveDeck")
                                && !targetCardData.getImage().equals(BLANK_IMAGE))) {
                            updateCard(sourceCard, targetCardData, true);
                            updateCard(targetCard, sourceCardData, true);
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

    public void updateCard(AnchorPane card, CardModel cardData, boolean updateField) {
        card.setUserData(new CardModel(cardData.getColor(), cardData.getName(), cardData.getImage()));

        if (updateField) {
            // Update Player Field
            String cardId = card.getId();
            if (cardId.startsWith("FieldCard")) {
                int id = Integer.parseInt(cardId.substring(9)) - 1;
                int i = id / 5;
                int j = id % 5;

                if (cardData.getImage().equals(BLANK_IMAGE)) {
                    gameManagerModel.getWhoseTurn().getField().removeCard(i, j);
                } else {
                    gameManagerModel.getWhoseTurn().getField().setCard(cardData, i, j);
                }
            }
        }

        ImageView imageView = (ImageView) card.getChildren().get(0);
        Label label = (Label) card.getChildren().get(1);

        Image image = new Image(getClass().getResourceAsStream(cardData.getImage()));
        if (image != null && !cardData.getImage().equals(BLANK_IMAGE)) {
            imageView.setImage(image);
            imageView.setVisible(true);
            label.setText(cardData.getName());
            label.setVisible(true);
        } else {
            imageView.setImage(null);
            imageView.setVisible(false);
            label.setText("");
            label.setVisible(false);
        }

        card.setStyle(null);
        String color = cardData.getColor();
        if (color != null && !color.isEmpty()) {
            card.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 7.7px;");
        }
    }

    public void clearCard(AnchorPane source) {
        CardModel emptyData = new CardModel("", "", BLANK_IMAGE);
        source.setUserData(emptyData);
        updateCard(source, emptyData, true);
    }

    private void clearField(List<AnchorPane> decks, boolean updateField) {
        for (int i = 0; i < decks.size(); i++) {
            try {
                updateCard(decks.get(i), new CardModel("", "", BLANK_IMAGE), updateField);
            } catch (Exception e) {
                // Handle exception
            }
        }

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
            if (LoadView.getPlayer1() != null) {
                this.gameManagerModel.setCurrentTurn(LoadView.getCurrentTurn());
                this.gameManagerModel.setPlayer1(LoadView.getPlayer1());
                this.gameManagerModel.setPlayer2(LoadView.getPlayer2());
                this.gameManagerModel.getShop().setProductList(LoadView.getProductList());
                gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
                gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
                deckCount.setText(
                        "My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
                gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
                loadActiveDeck(gameManagerModel.getPlayer1());
                loadField(gameManagerModel.getPlayer1());
            }
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
            SaveView saveView = new SaveView(gameManagerModel.getDataManager(), gameManagerModel.getCurrentTurn(),
                    gameManagerModel.getShop().getProductList(), gameManagerModel.getPlayer1(),
                    gameManagerModel.getPlayer2());
            loader.setController(saveView);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleOpenCardInfo(AnchorPane deck){
        CardModel sourceCardData = (CardModel) deck.getUserData();

        if(!sourceCardData.getImage().equals(BLANK_IMAGE)){
            System.out.println("ini ada gambar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/CardInfo.fxml"));
                CardInfoView cardView = new CardInfoView(deck);
                loader.setController(cardView);
                Parent root = loader.load();
    
                Stage childStage = new Stage();
                childStage.setTitle("Card Info");
                childStage.initModality(Modality.APPLICATION_MODAL);
                childStage.initOwner(null);  // Replace 'null' with reference to the primary stage if needed
                childStage.setScene(new Scene(root));
                childStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("ini tidak ada gambar");

        }
    } 

    

    

    private void setupClickCard(){
        for(AnchorPane activeDeck: activeDecks){
            activeDeck.setOnMouseClicked(event -> handleOpenCardInfo(activeDeck));
        }
    }



    public void updateCard(AnchorPane card, CardModel cardData) {
        card.setUserData(cardData);

        System.out.println("a");
        ImageView imageView = (ImageView) card.getChildren().get(0);
        System.out.println("b");
        Image image = new Image(getClass().getResourceAsStream(cardData.getImage()));
        System.out.println("c");
        imageView.setImage(image != null ? image : new Image(BLANK_IMAGE)); // Use blank image if resource not found
        System.out.println("d");
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
}