package com.if2210.app.model;

public class FieldModel {
    private CardModel[][] field;

    public FieldModel(){
        field = new CardModel[4][5];
    }

    public CardModel[][] getField() {
        return field;
    }

    public void setField(CardModel[][] field) {
        this.field = field;
    }
}
