package com.if2210.app.controller;

import java.util.List;
import java.util.Map;

import com.if2210.app.factory.PlantCardFactory;
import com.if2210.app.factory.ProductCardFactory;
import com.if2210.app.model.*;
import com.if2210.app.model.AnimalCardModel.AnimalType;
import com.if2210.app.view.*;
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
import java.util.HashMap;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;

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

        handleNextTurn();
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
                            if (sourceCardData instanceof ProductCardModel) {
                                sellCard(sourceCard);
                                success = true; // Implement your logic here if needed
                            }
                            else {
                                System.out.println("Illegal move: Source card is not a Product card");
                            }
                        }
                        // Source is an Animal or Plant card
                        else if (sourceCardData instanceof AnimalCardModel
                                || sourceCardData instanceof PlantCardModel) {
                            if (targetCardData.getImage().equals(BLANK_IMAGE) && !isEnemyField) {
                                // Berladang atau Bertanam
                                updateCard(sourceCard, targetCardData, true);
                                updateCard(targetCard, sourceCardData, true);
                                success = true;
                            }
                            else if (!sourceCardId.startsWith("ActiveDeck")) {
                                // Swapping in the field
                                updateCard(sourceCard, targetCardData, true);
                                updateCard(targetCard, sourceCardData, true);
                                success = true;
                            }
                        }
                        // Source is an Item card
                        else if (sourceCardData instanceof ItemCardModel &&
                                (targetCardData instanceof AnimalCardModel
                                        || targetCardData instanceof PlantCardModel)) {
                            if (isEnemyField){
                                // Kalo di lapangan lawan
                                if (sourceCardData.getName().equals("Delay")){
                                    applyItemEffect(sourceCardData, targetCardData, targetCard);
                                    deleteCard(sourceCard);
                                    success = true; // Implement your logic here if needed

                                    // change to plant auto
                                    Map<String, String> resProd = new HashMap<>();
                                    resProd.put("Hiu Darat", "Sirip Hiu");
                                    resProd.put("Sapi", "Susu");
                                    resProd.put("Domba", "Daging Domba");
                                    resProd.put("Kuda", "Daging Kuda");
                                    resProd.put("Ayam", "Telur");
                                    resProd.put("Beruang", "Daging Beruang");
                                    resProd.put("Biji Jagung", "Jagung");
                                    resProd.put("Biji Labu", "Labu");
                                    resProd.put("Biji Stroberi", "Stroberi");
                                    if(targetCardData instanceof PlantCardModel && (((PlantCardModel)targetCardData).getCurrentAge()-2)<((PlantCardModel)targetCardData).getHarvestAge()){
                                        if(resProd.containsValue(targetCardData.getName())){
                                            for (Map.Entry<String, String> entry : resProd.entrySet()) {
                                                System.out.println(entry.getValue());
                                                System.out.println(targetCardData.getName());
                                                if (targetCardData.getName().equals(entry.getValue())) {
                                                    PlantCardModel plant = PlantCardFactory.createPlantCard(entry.getKey());
                                                    plant.setCurrentAge(((PlantCardModel)targetCardData).getCurrentAge());
                                                    plant.setActiveItems(((PlantCardModel)targetCardData).getActiveItems());
                                                    updateCard(targetCard, plant, true);
                                                }
                                            }
                                        }
                                    }

                                }
                                else if (sourceCardData.getName().equals("Destroy") && isEnemyField) {
                                    applyDestroyEffect(sourceCardData, targetCardData, targetCard); //source itu item, target itu cardModel
                                    deleteCard(sourceCard);
                                    success = true; // Implement your logic here if needed
                                }
                            }
                            else{
                                if (sourceCardData.getName().equals("Accelerate") || sourceCardData.getName().equals("Protect")) {
                                    applyItemEffect(sourceCardData, targetCardData, targetCard);
                                    deleteCard(sourceCard);
                                    success = true; // Implement your logic here if needed
                                }
                                else if (sourceCardData.getName().equals("Instant Harvest")) {
                                    // Implement your logic here if needed
                                    System.out.println("INSTANT HARVEST");
                                    applyInstantHarvest(targetCardData, targetCard);
                                    deleteCard(sourceCard);
                                    success = true; // Implement your logic here if needed
                                }
                                else if (sourceCardData.getName().equals("Trap")) {
                                    System.out.println("TRAP");
                                    applyItemEffect(sourceCardData, targetCardData, targetCard);
                                    deleteCard(sourceCard);
                                    success = true; // Implement your logic here if needed
                                }
                            }
                        }
                        // Source is product
                        else if (sourceCardData instanceof ProductCardModel && targetCardData instanceof AnimalCardModel && !isEnemyField){
                            appplyFeedEffect(sourceCardData, targetCardData, targetCard, sourceCard);
                            success = true;
                        }
                    }
                    else {
                        System.out.println("Illegal move: Source card is empty");
                    }
                }
                else {
                    System.out.println("Source ActiveDeck not found");
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

    private void sellCard(AnchorPane card) {
        if (card.getUserData() instanceof ProductCardModel) {
            ProductCardModel productCard = (ProductCardModel) card.getUserData();
            gameManagerModel.getActivePlayer()
                    .setMoney(gameManagerModel.getActivePlayer().getMoney() + productCard.getPrice());
            gulden1.setText(Integer.toString(gameManagerModel.getPlayer1().getMoney()));
            gulden2.setText(Integer.toString(gameManagerModel.getPlayer2().getMoney()));
            
            boolean found = false;

            for (Map.Entry<ProductCardModel, Integer> entry : gameManagerModel.getShop().getProductList().entrySet()) {
                ProductCardModel product = entry.getKey();
                String productName = product.getName();

                if (productName.equals(productCard.getName())) {
                    try {
                        gameManagerModel.getShop().addProduct(product);
                    } catch (Exception e) {
                        // Exception handling
                    }
                    found = true;
                    break;
                }
            }

            if (!found) {
                gameManagerModel.getShop().getProductList().put(productCard, 1);
            }
            
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
                    updateCard(activeDecks.get(i), cardData, true);
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

        if (!sourceCardData.getImage().equals(BLANK_IMAGE)) {
            System.out.println("ini ada gambar");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/CardInfo.fxml"));
                CardInfoView cardView = new CardInfoView(deck, this, gameManagerModel);
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
                            updateCard(activeDecks.get(i), productItem, true);
                            break;
                        }
                    }
                    deleteCard(deck);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ini tidak ada gambar");

        }
    }

    public void handleVictory(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/Victory.fxml"));
            String winner;
            if(gameManagerModel.getPlayer1().getMoney()>gameManagerModel.getPlayer2().getMoney()){
                winner ="1";
            }else if(gameManagerModel.getPlayer1().getMoney()<gameManagerModel.getPlayer2().getMoney()){
                winner ="2";
            }else{
                winner ="0";
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
        System.out.println("Next Turn");
        gameManagerModel.setWhoseTurn(gameManagerModel.getWhoseTurn() == 0 ? 1 : 0);
        if (gameManagerModel.getWhoseTurn() == 0) {
            gameManagerModel.setCurrentTurn(gameManagerModel.getCurrentTurn() + 1);
        } else if (gameManagerModel.getWhoseTurn() == -1) {
            gameManagerModel.setCurrentTurn(0);
        }
        gameTurn.setText(String.format("%02d", gameManagerModel.getCurrentTurn()));
        loadActiveDeck(gameManagerModel.getActivePlayer());
        loadField(gameManagerModel.getActivePlayer());
        FieldController.incrementAllCards(gameManagerModel.getActivePlayer().getField());
        isEnemyField = false;
        toggleDragDetectionOnFieldCards(true); // Enable drag detection
        FieldController.updatePlantHarvested(fieldCards, gameManagerModel, this);

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
                        updateCard(this.activeDecks.get(i), newCards.getNewCards().get(0), true);
                        newCards.getNewCards().remove(0);
                    }
                    if (newCards.getNewCards().size() == 0) {
                        break;
                    }
                }
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
    private void applyItemEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard) {
        if (targetCardData instanceof AnimalCardModel) {
            AnimalCardModel temp = (AnimalCardModel) targetCardData;
            ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
            activeItems.add((ItemCardModel) sourceCardData);
            temp.setActiveItems(activeItems);
            updateCard(targetCard, temp, true);
        } else {
            PlantCardModel temp = (PlantCardModel) targetCardData;
            ArrayList<ItemCardModel> activeItems = temp.getActiveItems();
            activeItems.add((ItemCardModel) sourceCardData);
            temp.setActiveItems(activeItems);
            updateCard(targetCard, temp, true);
        }
    }

    private void applyInstantHarvest(CardModel sourceCardData, AnchorPane sourceCard){
        if(!sourceCardData.getImage().equals(BLANK_IMAGE)){
            if(!gameManagerModel.getActivePlayer().getActiveDeck().isFull()){
                Map<String, String> resProd = new HashMap<>();
                resProd.put("Hiu Darat", "Sirip Hiu");
                resProd.put("Sapi", "Susu");
                resProd.put("Domba", "Daging Domba");
                resProd.put("Kuda", "Daging Kuda");
                resProd.put("Ayam", "Telur");
                resProd.put("Beruang", "Daging Beruang");
                resProd.put("Biji Jagung", "Jagung");
                resProd.put("Biji Labu", "Labu");
                resProd.put("Biji Stroberi", "Stroberi");
                
                ProductCardModel produk = ProductCardFactory.createProductCard(resProd.get(((CardModel) sourceCardData).getName()));
                for (int i = 0; i < 6; i++) {
                    if(gameManagerModel.getActivePlayer().getActiveDeck().getCard(i) == null){
                        gameManagerModel.getActivePlayer().getActiveDeck().setCard(i, produk);
                        updateCard(activeDecks.get(i), produk, true);
                        break;
                    }
                }
            }
            deleteCard(sourceCard);

        }
    }

    private void applyDestroyEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard) {
        if (!targetCardData.getImage().equals(BLANK_IMAGE)) {
            if (targetCardData instanceof AnimalCardModel) {
                ArrayList<ItemCardModel> activeItems = ((AnimalCardModel) targetCardData).getActiveItems();
                boolean foundProtect = false;
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        break;
                    }
                }

                if (!foundProtect) {
                    deleteCard(targetCard);
                }
            }
            else{
                ArrayList<ItemCardModel> activeItems = ((PlantCardModel) targetCardData).getActiveItems();
                boolean foundProtect = false;
                for (ItemCardModel item : activeItems) {
                    if (item.getName().equals("Protect")) {
                        foundProtect = true;
                        break;
                    }
                }

                if (!foundProtect) {
                    deleteCard(targetCard);
                }
            }
        }
    }

    private void appplyFeedEffect(CardModel sourceCardData, CardModel targetCardData, AnchorPane targetCard, AnchorPane sourceCard) {
        AnimalCardModel temp = (AnimalCardModel) targetCardData;

        AnimalType tipe = temp.getType();
        ProductCardModel food = (ProductCardModel) sourceCardData;
        switch (tipe) {
            case OMNIVORE:
                temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                updateCard(targetCard, temp, true);
                deleteCard(sourceCard);
                break;
            case HERBIVORE:
                if (food.getName().equals("Jagung") || food.getName().equals("Labu") || food.getName().equals("Stroberi")) {
                    temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                    updateCard(targetCard, temp, true);
                    deleteCard(sourceCard);
                }
                break;
            case CARNIVORE:
                if (food.getName().equals("Daging Domba") || food.getName().equals("Daging Kuda") || food.getName().equals("Daging Beruang") || food.getName().equals("Sirip Hiu") || food.getName().equals("Telur") || food.getName().equals("Susu")){
                    temp.setCurrentWeight(temp.getCurrentWeight() + food.getAddedWeight());
                    updateCard(targetCard, temp, true);
                    deleteCard(sourceCard);
                }
                break;
            default:
                break;
        }
    }
}