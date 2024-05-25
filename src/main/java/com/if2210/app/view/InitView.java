package com.if2210.app.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class InitView {
    @FXML
    Button passiveButton;
    
    @FXML
    Button myButton1;
    
    @FXML
    Button myButton2;

    @FXML
    Label response;

    public boolean isLoad;

    @FXML
    public void initialize(){
        passiveButton.setVisible(false);

    }

    public void buttonHandler1(){
        isLoad = false;
        response.setText("Start game without load state");
        Stage stage = (Stage) response.getScene().getWindow();
        stage.close();
    }
    
    public void buttonHandler2(){
        isLoad = true;
        response.setText("Start game from load state");
        Stage stage = (Stage) response.getScene().getWindow();
        stage.close();
    }


}
