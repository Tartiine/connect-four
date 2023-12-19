package ensisa.connect4;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;

public class SettingsController {

    @FXML
    private Slider volumeSlider;
    private MediaPlayer mediaPlayer;

    public void initialize() {
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100.0); 
            }
        });
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ensisa/connect4/menu-fxml.fxml")); 
            volumeSlider.getScene().setRoot(root); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

}
