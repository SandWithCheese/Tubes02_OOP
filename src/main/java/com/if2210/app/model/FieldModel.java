package com.if2210.app.model;

public class FieldModel {
    private CardModel[][] field;

    public FieldModel() {
        field = new CardModel[4][5];
        // Isi field dengan null
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                field[i][j] = null;
            }
        }
    }

    public CardModel[][] getField() {
        return field;
    }

    public void setField(CardModel[][] field) {
        this.field = field;
    }

    public CardModel getCard(int i, int j) {
        return field[i][j];
    }

    public void setCard(CardModel card, int i, int j) {
        field[i][j] = card;
    }

    public void removeCard(int i, int j) {
        field[i][j] = null;
    }

    public boolean isSlotEmpty(int i, int j) {
        return field[i][j] == null;
    }

    public boolean isFieldEmpty() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (field[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isFieldFull() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (field[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getEffectiveFieldSize() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (field[i][j] != null) {
                    count++;
                }
            }
        }
        return count;
    }
}
