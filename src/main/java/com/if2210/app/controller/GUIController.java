package com.if2210.app.controller;

import java.util.List;
import java.util.Map;

import com.if2210.app.factory.PlantCardFactory;
import javafx.application.Platform;

import com.if2210.app.constant.Constant;
import com.if2210.app.factory.AnimalCardFactory;
import com.if2210.app.model.*;
import com.if2210.app.view.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class GUIController {
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
    private Label fieldLabel;

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

    public boolean isEnemyField = false;

    public List<AnchorPane> activeDecks = new ArrayList<>();
    public List<AnchorPane> fieldCards = new ArrayList<>();

    public GUIController() {
        this.gameManagerModel = new GameManagerModel();
    }

    @FXML
    public void initialize() {
        AnchorPaneController.initializeDecks(activeDeckGroup, activeDecks, 6, isEnemyField, gameManagerModel);
        AnchorPaneController.initializeDecks(fieldCardGroup, fieldCards, 20, isEnemyField, gameManagerModel);
        setupDragAndDrop();

        setupClickCard();

        gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
        gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
        deckCount.setText("My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
        fieldLabel.setText("Player 1 Field");
        ActiveDeckController.loadActiveDeck(gameManagerModel.getPlayer1(), activeDecks, gameManagerModel, isEnemyField);
        FieldController.loadField(gameManagerModel.getPlayer1(), fieldCards, gameManagerModel, isEnemyField);

        myFieldButton.setOnMouseClicked(this::handleMyFieldButtonClick);
        enemyFieldButton.setOnMouseClicked(this::handleEnemyFieldButtonClick);

        handleNextTurn();
        gameManagerModel.setCurrentTurn(1);
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
    }

    private void handleMyFieldButtonClick(MouseEvent event) {
        isEnemyField = false;
        FieldController.loadField(gameManagerModel.getActivePlayer(), fieldCards, gameManagerModel, isEnemyField);
        toggleDragDetectionOnFieldCards(true); // Enable drag detection
        fieldLabel.setText("Player " + (gameManagerModel.getWhoseTurn() + 1) + " Field");
    }

    private void handleEnemyFieldButtonClick(MouseEvent event) {
        isEnemyField = true;
        FieldController.loadField(gameManagerModel.getEnemy(), fieldCards, gameManagerModel, isEnemyField);
        toggleDragDetectionOnFieldCards(false); // Disable drag detection
        fieldLabel.setText("Player " + (gameManagerModel.getWhoseTurn() == 0 ? 2 : 1) + " Field");
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
                AnchorPane sourceCard = AnchorPaneController.findDeckById(sourceCardId, activeDecks, fieldCards);
                if (sourceCard != null) {
                    CardModel sourceCardData = (CardModel) sourceCard.getUserData();
                    CardModel targetCardData = (CardModel) targetCard.getUserData();
                    // Check if source card is not empty
                    if (!sourceCardData.getImage().equals(Constant.BLANK_IMAGE)) {
                        // target is the shop
                        if (targetCard.getId().equals("shopDrop")) {
                            // Source is a Product card
                            if (sourceCardData instanceof ProductCardModel) {
                                Integer gulden = CardController.sellCard(sourceCard, isEnemyField, gameManagerModel);
                                if (gulden != null) {
                                    int player = gameManagerModel.getWhoseTurn();
                                    if (player == 0) {
                                        gulden1.setText(Integer.toString(gulden));
                                    } else {
                                        gulden2.setText(Integer.toString(gulden));
                                    }
                                    success = true; // Implement your logic here if needed
                                    messageLabel.setText("Product card has been sold");
                                }
                            } else {
                                System.out.println("Illegal move: Source card is not a Product card");
                            }
                        }
                        // Source is an Animal or Plant card
                        else if (sourceCardData instanceof AnimalCardModel
                                || sourceCardData instanceof PlantCardModel) {
                            if (targetCardData.getImage().equals(Constant.BLANK_IMAGE) && !isEnemyField) {
                                // Switch color
                                String tempColor = sourceCardData.getColor();
                                sourceCardData.setColor(targetCardData.getColor());
                                targetCardData.setColor(tempColor);
                                // Berladang atau Bertanam
                                CardController.updateCard(sourceCard, targetCardData, true, isEnemyField,
                                        gameManagerModel);
                                CardController.updateCard(targetCard, sourceCardData, true, isEnemyField,
                                        gameManagerModel);
                                success = true;
                                messageLabel.setText("Animal/Plant card has been moved to field");
                            } else if (!sourceCardId.startsWith("ActiveDeck")) {
                                String tempColor = sourceCardData.getColor();
                                sourceCardData.setColor(targetCardData.getColor());
                                targetCardData.setColor(tempColor);
                                // Swapping in the field
                                CardController.updateCard(sourceCard, targetCardData, true, isEnemyField,
                                        gameManagerModel);
                                CardController.updateCard(targetCard, sourceCardData, true, isEnemyField,
                                        gameManagerModel);
                                success = true;
                                messageLabel.setText("Animal/Plant card has been swapped");
                            }
                        }
                        // Source is an Item card
                        else if (sourceCardData instanceof ItemCardModel &&
                                (targetCardData instanceof AnimalCardModel
                                        || targetCardData instanceof PlantCardModel)) {
                            if (isEnemyField) {
                                // Kalo di lapangan lawan
                                if (sourceCardData.getName().equals("Delay")) {
                                    ItemCardController.applyItemEffect(sourceCardData, targetCardData, targetCard,
                                            isEnemyField, gameManagerModel);
                                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                                    success = true; // Implement your logic here if needed
                                    messageLabel.setText("Delay card has been used");

                                    // change to plant automatically
                                    if (targetCardData instanceof PlantCardModel
                                            && (((PlantCardModel) targetCardData).getCurrentAge()
                                                    - 2) < ((PlantCardModel) targetCardData).getHarvestAge()) {
                                        if (Constant.RES_PROD.containsValue(targetCardData.getName())) {
                                            for (Map.Entry<String, String> entry : Constant.RES_PROD.entrySet()) {
                                                if (targetCardData.getName().equals(entry.getValue())) {
                                                    PlantCardModel plant = PlantCardFactory
                                                            .createPlantCard(entry.getKey());
                                                    plant.setCurrentAge(
                                                            ((PlantCardModel) targetCardData).getCurrentAge());
                                                    plant.setActiveItems(
                                                            ((PlantCardModel) targetCardData).getActiveItems());
                                                    CardController.updateCard(targetCard, plant, true, isEnemyField,
                                                            gameManagerModel);
                                                }
                                            }
                                        }
                                    }

                                } else if (sourceCardData.getName().equals("Destroy") && isEnemyField) {
                                    // source itu item, target itu cardModel
                                    messageLabel.setText(ItemCardController.applyDestroyEffect(sourceCardData,
                                            targetCardData, targetCard, isEnemyField, gameManagerModel));
                                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                                    success = true; // Implement your logic here if needed
                                }
                            }
                            // In my field
                            else {
                                if (sourceCardData.getName().equals("Accelerate")
                                        || sourceCardData.getName().equals("Protect")) {
                                    ItemCardController.applyItemEffect(sourceCardData, targetCardData, targetCard,
                                            isEnemyField, gameManagerModel);
                                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                                    success = true; // Implement your logic here if needed
                                    messageLabel.setText(sourceCardData.getName() + " has been used");
                                } else if (sourceCardData.getName().equals("Instant Harvest")) {
                                    // Implement your logic here if needed
                                    ItemCardController.applyInstantHarvest(targetCardData, targetCard, sourceCard,
                                            isEnemyField, gameManagerModel, activeDecks);
                                    success = true; // Implement your logic here if needed
                                    messageLabel.setText("Instant Harvest card has been used");
                                } else if (sourceCardData.getName().equals("Trap")) {
                                    System.out.println("TRAP");
                                    ItemCardController.applyItemEffect(sourceCardData, targetCardData, targetCard,
                                            isEnemyField, gameManagerModel);
                                    CardController.deleteCard(sourceCard, isEnemyField, gameManagerModel);
                                    success = true; // Implement your logic here if needed
                                    messageLabel.setText("Trap card has been used");
                                }
                            }
                        }
                        // Source is product
                        else if (sourceCardData instanceof ProductCardModel && targetCardData instanceof AnimalCardModel
                                && !isEnemyField) {
                            messageLabel.setText(ItemCardController.applyFeedEffect(sourceCardData, targetCardData,
                                    targetCard, sourceCard, isEnemyField, gameManagerModel));
                            success = true;
                        }
                    } else {
                        System.out.println("Illegal move: Source card is empty");
                    }
                } else {
                    System.out.println("Source ActiveDeck not found");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
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
                this.gameManagerModel.setWhoseTurn(-1);
                this.gameManagerModel.setPlayer1(LoadView.getPlayer1());
                this.gameManagerModel.setPlayer2(LoadView.getPlayer2());
                this.gameManagerModel.getShop().setProductList(LoadView.getProductList());
                gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
                gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
                ActiveDeckController.loadActiveDeck(gameManagerModel.getPlayer1(), activeDecks, gameManagerModel,
                        isEnemyField);
                FieldController.loadField(gameManagerModel.getPlayer1(), fieldCards, gameManagerModel, isEnemyField);
                handleNextTurn();
                gameManagerModel.setCurrentTurn(gameManagerModel.getCurrentTurn() - 1);
                deckCount.setText(
                        "My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize()) + "/40");
                gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleOpenShop() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Shop.fxml"));
            ShopView shopView = new ShopView(gameManagerModel.getShop(), gameManagerModel.getActivePlayer());
            loader.setController(shopView);
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
            gameManagerModel.setShop(ShopView.getShop());
            gameManagerModel.getActivePlayer().setMoney(ShopView.getPlayer().getMoney());
            if (gameManagerModel.getWhoseTurn() == 0) {
                gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
            } else {
                gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
            }
            for (int i = 0; i < 6; i++) {
                CardModel cardData = ShopView.getPlayer().getActiveDeck().getCard(i);
                if (cardData != null) {
                    CardController.updateCard(activeDecks.get(i), cardData, true, isEnemyField, gameManagerModel);
                }
            }
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

        if (!sourceCardData.getImage().equals(Constant.BLANK_IMAGE)) {
            System.out.println("ini ada gambar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/CardInfo.fxml"));
                CardInfoView cardView = new CardInfoView(deck, gameManagerModel, isEnemyField);
                loader.setController(cardView);
                Parent root = loader.load();

                Stage childStage = new Stage();
                childStage.setTitle("Card Info");
                childStage.initModality(Modality.APPLICATION_MODAL);
                childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
                childStage.setScene(new Scene(root));
                childStage.showAndWait();

                ProductCardModel productItem = CardInfoView.getProductItem();
                if (productItem != null) {
                    for (int i = 0; i < 6; i++) {
                        // System.out.println(gameManagerModel.getActivePlayer().getActiveDeck().getCard(i).getName());
                        if (gameManagerModel.getActivePlayer().getActiveDeck().getCard(i) == null) {
                            gameManagerModel.getActivePlayer().getActiveDeck().setCard(i, productItem);
                            CardController.updateCard(activeDecks.get(i), productItem, true, isEnemyField,
                                    gameManagerModel);
                            break;
                        }
                    }
                    CardController.deleteCard(deck, isEnemyField, gameManagerModel);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ini tidak ada gambar");

        }
    }

    public void handleVictory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Victory.fxml"));
            String winner;
            if (gameManagerModel.getPlayer1().getMoney() > gameManagerModel.getPlayer2().getMoney()) {
                winner = "1";
            } else if (gameManagerModel.getPlayer1().getMoney() < gameManagerModel.getPlayer2().getMoney()) {
                winner = "2";
            } else {
                winner = "0";
            }
            VictoryView vic = new VictoryView(winner);
            loader.setController(vic);
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
    }

    public void handleNextTurn() {
        gameManagerModel.setWhoseTurn(gameManagerModel.getWhoseTurn() == 0 ? 1 : 0);
        if (gameManagerModel.getWhoseTurn() == 0) {
            gameManagerModel.setCurrentTurn(gameManagerModel.getCurrentTurn() + 1);
        } else if (gameManagerModel.getWhoseTurn() == -1) {
            gameManagerModel.setCurrentTurn(0);
        }
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
        fieldLabel.setText("Player " + (gameManagerModel.getWhoseTurn() + 1) + " Field");
        ActiveDeckController.loadActiveDeck(gameManagerModel.getActivePlayer(), activeDecks, gameManagerModel,
                isEnemyField);
        FieldController.loadField(gameManagerModel.getActivePlayer(), fieldCards, gameManagerModel, isEnemyField);

        FieldController.incrementAllCards(gameManagerModel.getActivePlayer().getField());
        isEnemyField = false;
        toggleDragDetectionOnFieldCards(true); // Enable drag detection
        FieldController.updatePlantHarvested(fieldCards, gameManagerModel, this, gameManagerModel, isEnemyField);

        ActiveDeckModel currentActiveDeck = gameManagerModel.getActivePlayer().getActiveDeck();
        DeckModel currentDeck = gameManagerModel.getActivePlayer().getDeck();
        int currTurn = gameManagerModel.getWhoseTurn(); // 0 atau 1

        // Calculate how many empty slot exist
        int emptySlot = 6 - this.gameManagerModel.getActivePlayer().getActiveDeck().getEffectiveDeckSize();

        // Implement shuffle new card if currentActiveDeck is not full and currentDeck
        // not empty
        if (!currentActiveDeck.isFull() && !currentDeck.isEmpty()) {
            try {
                // Load pop up window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/NewCards.fxml"));

                // Setting controller for fxml
                NewCardsView newCards = new NewCardsView(currTurn, currentDeck, emptySlot,
                        new ActiveDeckModel(currentActiveDeck));
                loader.setController(newCards);

                Parent root = loader.load();

                Stage childStage = new Stage();
                childStage.setTitle("New Random Card From Your Deck");
                childStage.initModality(Modality.APPLICATION_MODAL);
                childStage.initOwner(null); // Replace 'null' with reference to the primary stage if needed
                childStage.setScene(new Scene(root));

                // Load the logo image for taskbar logo
                String iconPath = "/com/if2210/app/assets/Anya.png";
                childStage.getIcons().add(new javafx.scene.image.Image(iconPath));

                childStage.showAndWait();

                // Remove choosen card from Deck
                for (CardModel card : newCards.getNewCards()) {
                    this.gameManagerModel.getActivePlayer().getDeck().removeCard(card);
                }

                // Set active deck in main page to be same as currentActiveDeck attribute in
                // NewCards
                for (int i = 0; i < 6; i++) {
                    if (this.gameManagerModel.getActivePlayer().getActiveDeck().getCards().get(i) == null) {
                        CardController.updateCard(this.activeDecks.get(i), newCards.getNewCards().get(0), true,
                                isEnemyField, gameManagerModel);
                        newCards.getNewCards().remove(0);
                    }
                    if (newCards.getNewCards().size() == 0) {
                        break;
                    }
                }

                deckCount.setText("My Deck " + Integer.toString(gameManagerModel.getPlayer1().getDeck().getDeckSize())
                        + "/40");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (currentActiveDeck.isFull()) {
                System.out.println("Can't shuffle new cards: Active Deck is Full");
            }
            if (currentDeck.isEmpty()) {
                System.out.println("Can't shuffle new cards: Deck is Empty");
            }
        }

        handleBearAttack();
    }

    private void handleBearAttack() {
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
                CardController.updateCard(card, cardData, true, isEnemyField, gameManagerModel);
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
                                CardController.deleteCard(card, isEnemyField, gameManagerModel);
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
                                    CardController.updateCard(activeDecks.get(i), bear, true, isEnemyField,
                                            gameManagerModel);
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
                            CardController.updateCard(card, cardData, true, isEnemyField, gameManagerModel);
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

        for (AnchorPane activeCardField : fieldCards) {
            setDragDetected(activeCardField);
            setDragOver(activeCardField);
            setDragDropped(activeCardField);
        }

        for (AnchorPane fieldCard : fieldCards) {
            fieldCard.setOnMouseClicked(event -> handleOpenCardInfo(fieldCard));
        }

        for (AnchorPane activeCardField : fieldCards) {
            activeCardField.setOnMouseClicked(event -> handleOpenCardInfo(activeCardField));
        }
    }
}