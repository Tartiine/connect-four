package ensisa.connect4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    //TODO: Bloquer plein ecran
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Quicksand-VariableFont_wght.ttf"), 14);
        scene = new Scene(loadFXML("menu-fxml"), 700, 750);
        stage.setScene(scene);
        stage.setTitle("Connect 4 Game - Menu");
        stage.setMinWidth(750);
        stage.setMinHeight(700);
        stage.setMaxWidth(750);
        stage.setMaxHeight(700);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    static void showMainView(boolean HALActivated) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("main-fxml.fxml"));
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.setHALActivated(HALActivated);

        scene.setRoot(root);
    }

    static void showSettingsView() throws IOException {
        Parent settingsRoot = loadFXML("settings-fxml"); 
        scene.setRoot(settingsRoot);
    }

    
}
