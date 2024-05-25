package com.if2210.app.view;

import com.if2210.app.model.ActiveDeckModel;
import com.if2210.app.model.CardModel;
import com.if2210.app.model.DeckModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;

public class NewCardsView {
    private int currTurn;
    private DeckModel currentDeck;
    private int emptySlot; // emptySlot of activeDeck
    private ActiveDeckModel currentActiveDeck;
    private ArrayList<CardModel> newCards;

    @FXML
    private Label labelPlayer;
    @FXML
    private ImageView imageSatu;
    @FXML
    private Label labelSatu;
    @FXML
    private ImageView imageDua;
    @FXML
    private Label labelDua;
    @FXML
    private ImageView imageTiga;
    @FXML
    private Label labelTiga;
    @FXML
    private ImageView imageEmpat;
    @FXML
    private Label labelEmpat;

    @FXML
    public void initialize() {
        labelPlayer.setText("Player " + Integer.toString(currTurn + 1));
        // Langsung nampilin kartu pas pertama kali di inisialisasi
        this.clickDice();
    }

    // Setter method for attribute newCards
    @FXML
    public void clickDice() {
        System.out.println("Clicked Dice Button");
        // Clear newCards list
        while (newCards.size() > 0) {
            newCards.remove(0);
        }

        // define the range index List of Cards inside Deck
        int max = currentDeck.getDeckSize() - 1;
        int min = 0;
        int range;
        int neededCard = (this.emptySlot > 4) ? 4 : this.emptySlot;

        for (int i = 0; i < neededCard; i++) {
            // Get random index
            range = max - min;
            int randomInt = (int) (Math.random() * range) + min;

            if (randomInt > currentDeck.getDeckSize() - 1) {
                i--;
                break;
            }

            // Kalo card di index randomInt belom ada di newCards, maka akan di add
            if (currentDeck.getCards().get(randomInt) != null
                    && !newCards.contains(currentDeck.getCards().get(randomInt))) {
                // Add card from deck to newCards
                newCards.add(currentDeck.getCards().get(randomInt));

                max--;
                continue;
            }
            i--;
        }

        // Define arrays for ImageView and Label objects
        ImageView[] imageViews = { imageSatu, imageDua, imageTiga, imageEmpat };
        Label[] labels = { labelSatu, labelDua, labelTiga, labelEmpat };

        // Now set the imageView and Label matched as the newCards attribute
        for (int i = 0; i < 4; i++) {
            // Set the imageView as needed amount
            if (i >= neededCard) {
                break;
            }

            Image img = new Image(newCards.get(i).getImage());
            imageViews[i].setImage(img);
            String name = new String(newCards.get(i).getName()); // This creates a new copy of the String value
            labels[i].setText(name);
        }
    }

    @FXML
    public void clickDone() {
        System.out.println("Clicked Done Button");
        // Set Active Deck to be added by the newCards
        for (CardModel card : this.newCards) {
            this.currentActiveDeck.getCards().add(card);
        }

        // Remove card from deck
        for (CardModel card : this.currentActiveDeck.getCards()) {
            currentDeck.getCards().remove(card);
        }

        Stage stage = (Stage) imageSatu.getScene().getWindow();
        stage.close();
    }

    public NewCardsView(int currTurn, DeckModel currentDeck, int emptySlot, ActiveDeckModel currentActiveDeck) {
        this.currTurn = currTurn;
        this.currentDeck = currentDeck;
        this.emptySlot = emptySlot;
        this.currentActiveDeck = currentActiveDeck;
        this.newCards = new ArrayList<CardModel>();
    }

    public DeckModel getCurrentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(DeckModel currentDeck) {
        this.currentDeck = currentDeck;
    }

    public int getEmptySlot() {
        return emptySlot;
    }

    public void setEmptySlot(int emptySlot) {
        this.emptySlot = emptySlot;
    }

    public ActiveDeckModel getCurrentActiveDeck() {
        return currentActiveDeck;
    }

    public void setCurrentActiveDeck(ActiveDeckModel currentActiveDeck) {
        this.currentActiveDeck = currentActiveDeck;
    }

    public ArrayList<CardModel> getNewCards() {
        return newCards;
    }

    public void setNewCards(ArrayList<CardModel> newCards) {
        this.newCards = newCards;
    }
}
