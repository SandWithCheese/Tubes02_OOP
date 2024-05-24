package com.if2210.app.controller;

import java.util.List;

import javafx.application.Platform;
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

import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.factory.ItemCardFactory;
import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.AnimalCardModel;
import com.if2210.app.model.CardModel;

import com.if2210.app.view.CardInfoView;

import com.if2210.app.model.GameManagerModel;
import com.if2210.app.model.ItemCardModel;
import com.if2210.app.model.PlantCardModel;
import com.if2210.app.model.PlayerModel;
import com.if2210.app.model.ProductCardModel;
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
    private Button shopButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private Button pluginButton;

    @FXML
    private Button nextTurnButton;

    @FXML
    private AnchorPane shopDrop;

    @FXML
    private Label messageLabel;

    private boolean isEnemyField = false;

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

        updateCard(activeDecks.get(0), AnimalCardFactory.createAnimalCard("Sapi"), false);
        updateCard(activeDecks.get(1), PlantCardFactory.createPlantCard("Biji Jagung"), false);
        updateCard(activeDecks.get(2), ItemCardFactory.createItemCard("Accelerate"), false);
        updateCard(activeDecks.get(3), ItemCardFactory.createItemCard("Destroy"), false);
        updateCard(activeDecks.get(4), ItemCardFactory.createItemCard("Protect"), false);
        updateCard(activeDecks.get(5), ProductCardFactory.createProductCard("Daging Beruang"), false);
        // updateCard(activeDecks.get(5), ProductCardFactory.createProductCard("Susu"), false);
    }

    private void handleMyFieldButtonClick(MouseEvent event) {
        isEnemyField = false;
        loadField(gameManagerModel.getActivePlayer());
        toggleDragDetectionOnFieldCards(true); // Enable drag detection
    }

    private void handleEnemyFieldButtonClick(MouseEvent event) {
        isEnemyField = true;
        loadField(gameManagerModel.getEnemy());
        toggleDragDetectionOnFieldCards(false); // Disable drag detection
    }

    private void loadActiveDeck(PlayerModel player) {
        clearField(activeDecks, false);
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

    // Method to toggle drag detection on field cards
    private void toggleDragDetectionOnFieldCards(boolean enable) {
        for (AnchorPane fieldCard : fieldCards) {
            if (enable) {
                setDragDetected(fieldCard);
            } else {
                fieldCard.setOnDragDetected(null);
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
            if (dragboard.hasString()) {
                AnchorPane sourceCard = findDeckById(sourceCardId);
                if (sourceCard != null) {
                    CardModel sourceCardData = (CardModel) sourceCard.getUserData();
                    CardModel targetCardData = (CardModel) targetCard.getUserData();
                    // Check if source card is not empty
                    if (!sourceCardData.getImage().equals(BLANK_IMAGE)) {
                        // target is the shop
                        if (targetCard.getId().equals("shopDrop")) {
                            // Source is a Product card
                            if (sourceCardData instanceof ProductCardModel){
                                sellCard(sourceCard);
                                success = true; // Implement your logic here if needed
                            } else {
                                System.err.println("Illegal move: Source card is not a Product card");
                            }
                        }
                        // Source is an Animal or Plant card
                        else if (sourceCardData instanceof AnimalCardModel
                                || sourceCardData instanceof PlantCardModel) {
                            if (targetCardData.getImage().equals(BLANK_IMAGE) && !isEnemyField) {
                                // Switch color
                                String tempColor = sourceCardData.getColor();
                                sourceCardData.setColor(targetCardData.getColor());
                                targetCardData.setColor(tempColor);
                                updateCard(sourceCard, targetCardData, true);
                                updateCard(targetCard, sourceCardData, true);
                                success = true;
                            } else if (!sourceCardId.startsWith("ActiveDeck")) {
                                String tempColor = sourceCardData.getColor();
                                sourceCardData.setColor(targetCardData.getColor());
                                targetCardData.setColor(tempColor);
                                updateCard(sourceCard, targetCardData, true);
                                updateCard(targetCard, sourceCardData, true);
                                success = true;
                            }
                        }
                        // Source is an Item card
                        else if (sourceCardData instanceof ItemCardModel && ((targetCardData instanceof AnimalCardModel
                                || targetCardData instanceof PlantCardModel))) {
                            // 1 Accelerate
                            if (sourceCardData.getName().equals("Accelerate")) {
                                // Implement your logic here if needed
                                System.out.println("ACCELERATE");
                                if (targetCardData instanceof AnimalCardModel) {
                                    // +8 weight
                                    AnimalCardModel temp = (AnimalCardModel) targetCardData;
                                    temp.setCurrentWeight(temp.getCurrentWeight() + 8);
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                                else{
                                    PlantCardModel temp = (PlantCardModel) targetCardData;
                                    temp.setCurrentAge(temp.getCurrentAge() + 2);
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                            }
                            // 2 Delay
                            else if (sourceCardData.getName().equals("Delay")) {
                                // Implement your logic here if needed
                                System.out.println("DELAY");
                                if (targetCardData instanceof AnimalCardModel) {
                                    // -5 weight, but not below 0
                                    AnimalCardModel temp = (AnimalCardModel) targetCardData;
                                    temp.setCurrentWeight(
                                            temp.getCurrentWeight() - 5 < 0 ? 0 : temp.getCurrentWeight() - 5);
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                                else{
                                    // -2 age, but not below 0
                                    PlantCardModel temp = (PlantCardModel) targetCardData;
                                    temp.setCurrentAge(temp.getCurrentAge() - 2 < 0 ? 0 : temp.getCurrentAge() - 2);
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                            }
                            // 3 Instant Harvest
                            else if (sourceCardData.getName().equals("Instant Harvest")) {
                                // Implement your logic here if needed
                                System.out.println("INSTANT HARVEST");
                            }
                            // 4 Destroy
                            else if (sourceCardData.getName().equals("Destroy")) {
                                if (targetCardData.getImage().equals(BLANK_IMAGE)) {
                                    System.err.println("Illegal move: Target must not be empty to destroy");
                                } else {
                                    System.out.println("DESTROY");
                                    if (targetCardData instanceof AnimalCardModel) {
                                        // +8 weight
                                        AnimalCardModel temp = (AnimalCardModel) targetCardData;
                                        ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                        boolean foundProtect = false;
                                        for (ItemCardModel item : activeItems) {
                                            if (item.getName().equals("Protect")) {
                                                foundProtect = true;
                                                break;
                                            }
                                        }

                                        if (!foundProtect){
                                            deleteCard(targetCard);
                                        }
                                    } else {
                                        PlantCardModel temp = (PlantCardModel) targetCardData;
                                        ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                        boolean foundProtect = false;
                                        for (ItemCardModel item : activeItems) {
                                            if (item.getName().equals("Protect")) {
                                                foundProtect = true;
                                                break;
                                            }
                                        }

                                        if (!foundProtect){
                                            deleteCard(targetCard);
                                        }
                                    }
                                    deleteCard(sourceCard);
                                    success = true;
                                }
                                // Implement your logic here if needed
                            }
                            // 5 Protect
                            else if (sourceCardData.getName().equals("Protect")) {
                                // Implement your logic here if needed
                                System.out.println("PROTECT");
                                if (targetCardData instanceof AnimalCardModel) {
                                    // +8 weight
                                    AnimalCardModel temp = (AnimalCardModel) targetCardData;
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                                else{
                                    PlantCardModel temp = (PlantCardModel) targetCardData;
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                    deleteCard(sourceCard);
                                }
                            }
                            // 6 Trap
                            else if (sourceCardData.getName().equals("Trap")) {
                                // Implement your logic here if needed
                                System.out.println("TRAP");
                                if (targetCardData instanceof AnimalCardModel) {
                                    // +8 weight
                                    AnimalCardModel temp = (AnimalCardModel) targetCardData;
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                } else {
                                    PlantCardModel temp = (PlantCardModel) targetCardData;
                                    ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                    activeItems.add((ItemCardModel) sourceCardData); // Add sourceCardData to
                                                                                     // activeItems
                                    temp.setActiveItems(activeItems); // Update activeItems in temp
                                    updateCard(targetCard, temp, true);
                                }
                            }
                            // Assuming item cards have a special handling logic with animal or plant cards
                            success = true; // Implement your logic here if needed
                        }
                    } else {
                        System.err.println("Illegal move: Source card is empty");
                    }
                } else {
                    System.err.println("Source ActiveDeck not found");
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
        card.setUserData(cardData);

        if (updateField) {
            // Update Player Field
            String cardId = card.getId();
            if (cardId.startsWith("FieldCard")) {
                int id = Integer.parseInt(cardId.substring(9)) - 1;
                int i = id / 5;
                int j = id % 5;

                if (cardData.getImage().equals(BLANK_IMAGE)) {
                    gameManagerModel.getActivePlayer().getField().removeCard(i, j);
                } else {
                    gameManagerModel.getActivePlayer().getField().setCard(cardData, i, j);
                }
            } else if (cardId.startsWith("ActiveDeck")) {
                int id = Integer.parseInt(cardId.substring(10)) - 1;
                if (cardData.getImage().equals(BLANK_IMAGE)) {
                    gameManagerModel.getActivePlayer().getActiveDeck().removeCard(id);
                } else {
                    gameManagerModel.getActivePlayer().getActiveDeck().setCard(id, cardData);
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

    public void deleteCard(AnchorPane source) {
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

    private void sellCard(AnchorPane card){
        if (card.getUserData() instanceof ProductCardModel){
            ProductCardModel productCard = (ProductCardModel) card.getUserData();
            gameManagerModel.getActivePlayer().setMoney(gameManagerModel.getActivePlayer().getMoney() + productCard.getPrice());
            gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
            gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
            deleteCard(card);
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

    public void handleOpenCardInfo(AnchorPane deck) {
        CardModel sourceCardData = (CardModel) deck.getUserData();

        if (!sourceCardData.getImage().equals(BLANK_IMAGE)) {
            System.out.println("ini ada gambar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/CardInfo.fxml"));
                CardInfoView cardView = new CardInfoView(deck);
                loader.setController(cardView);
                Parent root = loader.load();

                Stage childStage = new Stage();
                childStage.setTitle("Card Info");
                childStage.initModality(Modality.APPLICATION_MODAL);
                childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
                childStage.setScene(new Scene(root));
                childStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ini tidak ada gambar");

        }
    }

    public void handleNextTurn() {
        gameManagerModel.setWhoseTurn(gameManagerModel.getWhoseTurn() == 0 ? 1 : 0);
        if (gameManagerModel.getWhoseTurn() == 0) {
            gameManagerModel.setCurrentTurn(gameManagerModel.getCurrentTurn() + 1);
        }
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
        loadActiveDeck(gameManagerModel.getActivePlayer());
        loadField(gameManagerModel.getActivePlayer());
        bearAttack();
    }

    private void bearAttack() {
        int chance = (int) (Math.random() * 100);
        if (chance > 30) {
            return;
        }

        // Get 2 random coordinate in the field, loop until the area <= 6
        int x1 = (int) (Math.random() * 4);
        int y1 = (int) (Math.random() * 5);
        int x2 = (int) (Math.random() * 4);
        int y2 = (int) (Math.random() * 5);

        while (((Math.abs(x1 - x2) + 1) * (Math.abs(y1 - y2) + 1)) > 6) {
            x1 = (int) (Math.random() * 4);
            y1 = (int) (Math.random() * 5);
            x2 = (int) (Math.random() * 4);
            y2 = (int) (Math.random() * 5);
        }

        final int x1Final = x1;
        final int y1Final = y1;
        final int x2Final = x2;
        final int y2Final = y2;

        // Make all cards in the area color red
        for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
            for (int j = Math.min(y1, y2); j <= Math.max(y1, y2); j++) {
                AnchorPane card = fieldCards.get(i * 5 + j);
                CardModel cardData = (CardModel) card.getUserData();
                cardData.setColor("red");
                updateCard(card, cardData, true);
            }
        }

        // Make all button disabled
        myFieldButton.setDisable(true);
        enemyFieldButton.setDisable(true);
        shopButton.setDisable(true);
        saveButton.setDisable(true);
        loadButton.setDisable(true);
        pluginButton.setDisable(true);
        nextTurnButton.setDisable(true);
        for (AnchorPane activeDeck : activeDecks) {
            activeDeck.setDisable(true);
        }

        // Using threading, update the message label every 0.1 second for 30-60 seconds
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int duration = (int) (Math.random() * 300) + 300;
                for (int i = 0; i < duration; i++) {
                    final int countdown = duration - i;
                    Platform.runLater(() -> {
                        messageLabel.setText(
                                "Watch out for bears! Remaining time to move your cards : " + (float) countdown / 10
                                        + " seconds.");
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Platform.runLater(() -> {
                    messageLabel.setText("Bear attack! All cards in the field are destroyed.");
                    // Check if a card inside the area have a trap item
                    boolean foundProtect = false;
                    for (int i = Math.min(x1Final, x2Final); i <= Math.max(x1Final, x2Final); i++) {
                        for (int j = Math.min(y1Final, y2Final); j <= Math.max(y1Final, y2Final); j++) {
                            AnchorPane card = fieldCards.get(i * 5 + j);
                            CardModel cardData = (CardModel) card.getUserData();
                            if (cardData instanceof AnimalCardModel) {
                                AnimalCardModel temp = (AnimalCardModel) cardData;
                                ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                for (ItemCardModel item : activeItems) {
                                    if (item.getName().equals("Trap")) {
                                        foundProtect = true;
                                        break;
                                    }
                                }
                            } else if (cardData instanceof PlantCardModel) {
                                PlantCardModel temp = (PlantCardModel) cardData;
                                ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
                                for (ItemCardModel item : activeItems) {
                                    if (item.getName().equals("Trap")) {
                                        foundProtect = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    // Clear all cards inside the area if there are no card with protect item
                    if (!foundProtect) {
                        for (int i = Math.min(x1Final, x2Final); i <= Math.max(x1Final, x2Final); i++) {
                            for (int j = Math.min(y1Final, y2Final); j <= Math.max(y1Final, y2Final); j++) {
                                AnchorPane card = fieldCards.get(i * 5 + j);
                                clearCard(card);
                            }
                        }
                    } else {
                        messageLabel.setText("Bear attack! But your cards are safe because of the trap item.");

                        // If current player active deck is not full, add bear card to the active deck
                        if (!gameManagerModel.getActivePlayer().getActiveDeck().isFull()) {
                            CardModel bear = AnimalCardFactory.createAnimalCard("Beruang");
                            for (int i = 0; i < 6; i++) {
                                if (gameManagerModel.getActivePlayer().getActiveDeck().getCard(i) == null) {
                                    gameManagerModel.getActivePlayer().getActiveDeck().setCard(i, bear);
                                    updateCard(activeDecks.get(i), bear, true);
                                    break;
                                }
                            }
                        }
                    }

                    // Set all cards color back to normal
                    for (int i = Math.min(x1Final, x2Final); i <= Math.max(x1Final, x2Final); i++) {
                        for (int j = Math.min(y1Final, y2Final); j <= Math.max(y1Final, y2Final); j++) {
                            AnchorPane card = fieldCards.get(i * 5 + j);
                            CardModel cardData = (CardModel) card.getUserData();
                            cardData.setColor("#D4E3FC");
                            updateCard(card, cardData, true);
                        }
                    }

                    // Set all button enabled
                    myFieldButton.setDisable(false);
                    enemyFieldButton.setDisable(false);
                    shopButton.setDisable(false);
                    saveButton.setDisable(false);
                    loadButton.setDisable(false);
                    pluginButton.setDisable(false);
                    nextTurnButton.setDisable(false);
                    for (AnchorPane activeDeck : activeDecks) {
                        activeDeck.setDisable(false);
                    }
                });
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    private void setupClickCard() {
        for (AnchorPane activeDeck : activeDecks) {
            activeDeck.setOnMouseClicked(event -> handleOpenCardInfo(activeDeck));
        }

        for (AnchorPane fieldCard : fieldCards) {
            fieldCard.setOnMouseClicked(event -> handleOpenCardInfo(fieldCard));
        }
    }

    // public void updateCard(AnchorPane card, CardModel cardData) {
    // card.setUserData(cardData);

    // ImageView imageView = (ImageView) card.getChildren().get(0);
    // Image image = new Image(getClass().getResourceAsStream(cardData.getImage()));
    // imageView.setImage(image != null ? image : new Image(BLANK_IMAGE)); // Use
    // blank image if resource not found
    // Label label = (Label) card.getChildren().get(1);
    // label.setText(cardData.getName());

    // card.setStyle(null);
    // // Update AnchorPane background color based on the color attribute of the
    // card
    // // model
    // String color = cardData.getColor();
    // if (color != null && !color.isEmpty()) {
    // card.setStyle("-fx-background-color: " + color + "; -fx-background-radius:
    // 7.7px;");
    // }
    // }
}