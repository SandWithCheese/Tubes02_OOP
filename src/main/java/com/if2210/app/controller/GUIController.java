package com.if2210.app.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import javafx.scene.Group;




public class GUIController {

    @FXML
    private List<AnchorPane> activeDecks = new ArrayList<>();

    @FXML
    private AnchorPane FieldCard;

    @FXML
    private Group activeDeckGroup;

    @FXML
    public void initialize() {
        System.out.println(FieldCard);
        for (int i = 0; i < 6; i++) {
            try{
                AnchorPane anchorPane = (AnchorPane) activeDeckGroup.getChildren().get(i);
                if (!anchorPane.getChildren().isEmpty()) {
                    AnchorPane activeDeck = (AnchorPane) anchorPane.getChildren().get(0);
                    activeDecks.add(activeDeck);
                }
            }
            catch(Exception e){
                // ignore
            }
        }

        // Add drag detection for each ActiveDeck
        for (AnchorPane activeDeck : activeDecks) {
            activeDeck.setOnDragDetected(event -> {
                Dragboard dragboard = activeDeck.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(activeDeck.getId());
                dragboard.setContent(content);
                event.consume();
            });
        }

        // Add drag over for FieldCard, while hovering not releasing
        FieldCard.setOnDragOver(event -> {
            // System.out.println("Drag Over");
            if (event.getGestureSource() != FieldCard && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        // Add drop for FieldCard, when releasing
        FieldCard.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                String sourceDeckId = dragboard.getString();
                System.out.println("Dropped from " + sourceDeckId);
                AnchorPane sourceDeck = null;
                for (AnchorPane activeDeck : activeDecks) {
                    if (activeDeck.getId().equals(sourceDeckId)) {
                        sourceDeck = activeDeck;
                        break;
                    }
                }
                if (sourceDeck != null) {
                    ImageView sourceImageView = (ImageView) sourceDeck.getChildren().get(0);
                    Image image = sourceImageView.getImage();
                    Label sourceLabel = (Label) sourceDeck.getChildren().get(1);
                    String labelText = sourceLabel.getText();
                    updateCard(FieldCard, image, labelText);
                    clearCard(sourceDeck);
                    success = true;
                } else {
                    System.err.println("Source ActiveDeck not found");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void updateCard(AnchorPane card, Image image, String labelText) {
        card.setUserData(new CardData(image, labelText));
        ImageView imageView = (ImageView) card.getChildren().get(0);
        imageView.setImage(image);
        Label label = (Label) card.getChildren().get(1);
        label.setText(labelText);
    }

    private void clearCard(AnchorPane deck) {
        ImageView imageView = (ImageView) deck.getChildren().get(0);
        imageView.setImage(null);
        Label label = (Label) deck.getChildren().get(1);
        label.setText("");
    }

    private static class CardData {
        private Image image;
        private String labelText;

        public CardData(Image image, String labelText) {
            this.image = image;
            this.labelText = labelText;
        }
    }
}