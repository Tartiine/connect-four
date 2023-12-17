package ensisa.connect4;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class MainController {
    private MainGame game;

    @FXML
    private Pane gridPane;
    @FXML
    private Label messageLabel;
    private Circle[][] gridCircles;
    private final Color PLAYER_1_COLOR = Color.valueOf("#92bccd");
    private final Color PLAYER_2_COLOR = Color.valueOf("#e8b18f");
    private List<Button> columnButtons = new ArrayList<>();

    public void initialize() {
        game = new MainGame();

        final int rows = 6;
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;
        final double buttonWidth = 2 * radius;
        final double buttonHeight = 2 * radius;

        double gridWidth = cols * (2 * radius + spacing);
        double gridHeight = rows * (2 * radius + spacing);

        gridCircles = new Circle[rows][cols];

        double startX = (720 - gridWidth) / 2 + radius;
        double startY = (620 - gridHeight) / 2 + radius + buttonHeight + 10; 

        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Circle circle = new Circle(radius, Color.WHITE);
                double centerX = startX + (j * (2 * radius + spacing));
                double centerY = startY + (i * (2 * radius + spacing));
                circle.setCenterX(centerX);
                circle.setCenterY(centerY);
                gridPane.getChildren().add(circle);
                gridCircles[i][j] = circle;
            }
        }

        HBox buttonBox = new HBox(spacing);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setLayoutY(10);
        buttonBox.setLayoutX(startX - radius);

        for (int i = 0; i < cols; i++) {
            int col = i; 
            Button button = createStyledButton(col);
            button.setPrefSize(buttonWidth, buttonHeight);
            button.setOnAction(event -> onColumnClicked(col));
            buttonBox.getChildren().add(button);
            columnButtons.add(button);
        }

        gridPane.getChildren().add(buttonBox);
    }

    private Button createStyledButton(int col) {
        Button button = new Button();
        ImageView arrowIcon = new ImageView(new Image(getClass().getResourceAsStream("/ensisa/connect4/assets/images/down-arrow.png")));
        arrowIcon.setFitWidth(40);
        arrowIcon.setFitHeight(40);

        button.setGraphic(arrowIcon);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
        button.setBorder(new Border(new BorderStroke(Paint.valueOf("#CCCCCC"), 
            BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

        button.setOnMouseEntered(e -> {
            button.setBorder(new Border(new BorderStroke(Paint.valueOf("#AAAAAA"), 
                BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
            highlightColumn(col, true);
        });
        button.setOnMouseExited(e -> {
            button.setBorder(new Border(new BorderStroke(Paint.valueOf("#CCCCCC"), 
                BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
            highlightColumn(col, false);
        });

        return button;
    }

    private void highlightColumn(int col, boolean highlight) {
        for (int i = 0; i < gridCircles.length; i++) {
            if (highlight) {
                gridCircles[i][col].setStroke(Paint.valueOf("#AAAAAA"));
                gridCircles[i][col].setStrokeWidth(2);
            } else {
                gridCircles[i][col].setStroke(null);
            }
        }
    }

    public void drawToken(int col, int row, int player) {
        if (player == 1){
            gridCircles[row][col].setFill(PLAYER_1_COLOR);
        }else{
           gridCircles[row][col].setFill(PLAYER_2_COLOR); 
        }
    }

    public void onColumnClicked(int col) {
        int row = game.placeToken(col);
        if (row != -1) {
            drawToken(col, row, game.getCurrentPlayer());
            boolean win = game.checkForWin();
            if (win) {
                displayMessage("Player " + game.getCurrentPlayer() + " wins!", Color.GREEN);
                disableColumnButtons(); 
            } else if (game.isDraw()) {
                displayMessage("It's a draw!", Color.ORANGE);
                disableColumnButtons();
            } else {
                game.changeCurrentPlayer();
                Color currentPlayerColor = (game.getCurrentPlayer() == 1) ? PLAYER_1_COLOR : PLAYER_2_COLOR;
                displayMessage("Player " + game.getCurrentPlayer() + "'s turn", currentPlayerColor);
                
            }
        } else {
            showToast("This column is full. Please choose another column.");
        }
    }

    public void displayMessage(String message, Color textColor) {
        messageLabel.setTextFill(textColor); 
        messageLabel.setText(message);      
    }

    private void disableColumnButtons() {
        for (Button button : columnButtons) {
            button.setDisable(true);
        }
    }
    //TODO:Change color to red and position to middle top
    public void showToast(String message) {
        Label toastLabel = new Label(message);
        toastLabel.getStyleClass().add("toast");
        toastLabel.setLayoutX(50); // Center horizontally, modify to suit your layout
        toastLabel.setLayoutY(gridPane.getHeight() - 30); // Position at the bottom

        gridPane.getChildren().add(toastLabel);

        FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(0.5), toastLabel);
        fadeInTransition.setFromValue(0.0);
        fadeInTransition.setToValue(1.0);
        fadeInTransition.play();


        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> {
            FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(0.5), toastLabel);
            fadeOutTransition.setFromValue(1.0);
            fadeOutTransition.setToValue(0.0);
            fadeOutTransition.setOnFinished(event -> gridPane.getChildren().remove(toastLabel));
            fadeOutTransition.play();
        }));

        timeline.play();
    }

    
}
