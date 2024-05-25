package com.if2210.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.if2210.app.model.CardModel;
import com.if2210.app.model.ProductCardModel;
import com.if2210.app.view.CardInfoView;
import com.if2210.app.view.InitView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class App extends Application {
    private List<Media> songs = new ArrayList<>();
    private int currentSongIndex = 0;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    private void playMusic(Stage stage){
        
        // Load the logo image for taskbar logo
        String iconPath = "/com/if2210/app/assets/Anya.png";
        stage.getIcons().add(new javafx.scene.image.Image(iconPath));
        
        // Add song to the scene
        String songPath1 = "/com/if2210/app/music/KingdomDance.mp3";
        // String songPath2 = "/com/if2210/app/music/comedy.mp3";
        
        try {
            songs.add(new Media(new File(getClass().getResource(songPath1).toURI()).toURI().toString()));
        } catch (Exception e) {
            // TODO: handle exception
        }
        // songs.add(new Media(new File(getClass().getResource(songPath2).toURI()).toURI().toString()));

        mediaPlayer = new MediaPlayer(songs.get(currentSongIndex));
        // Setel agar lagu diputar berulang
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(songs.get((currentSongIndex + 1) % songs.size()));
            mediaPlayer.play();
            currentSongIndex = (currentSongIndex + 1) % songs.size();
        });
        mediaPlayer.play();
    }

    @Override
    public void start(Stage stage) throws Exception {

        playMusic(stage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/if2210/app/fxml/GUI.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1228, 768);


        stage.setTitle("MoliNana");
        stage.setScene(scene);
        stage.show();
    }
}
