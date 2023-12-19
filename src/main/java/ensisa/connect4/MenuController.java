package ensisa.connect4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button vsPlayerButton;
    @FXML
    private Button vsAIButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button exitButton;
    
    private boolean HALActivated = false; 

    @FXML
    private void handleVsPlayer() throws IOException {
        System.out.println("Play vs Player button clicked");
        App.showMainView(false); 
    }

    @FXML
    private void handleVsAI() throws IOException {
        System.out.println("Play vs AI button clicked");
        App.showMainView(true); 
    }

    @FXML
    private void handleSettings() throws IOException {
        System.out.println("Settings button clicked");
        App.showSettingsView(); 
    }

    @FXML
    private void handleExit() {
        System.out.println("Exit button clicked");
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    public boolean isHALActivated() {
        return HALActivated;
    }
}
