package ensisa.connect4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {
    private MainGame game;

    @FXML private Pane gridPane;
    @FXML private Label messageLabel;
    @FXML private Button mainMenuButton;
    @FXML private Button restartButton;
    private Circle[][] gridCircles;
    private final Color PLAYER_1_COLOR = Color.valueOf("#92bccd");
    private final Color PLAYER_2_COLOR = Color.valueOf("#e8b18f");
    private List<Button> columnButtons = new ArrayList<>();
    private boolean HALActivated;

//TODO:Add random player at the beginning
    public void initialize() {
        game = new MainGame();
        setupGameGrid();
        setupColumnButtons();
        displayMessage("Player 1's turn", PLAYER_1_COLOR);
    }

    private void setupGameGrid() {
        final int rows = 6;
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;

        gridCircles = new Circle[rows][cols];
        double gridWidth = cols * (2 * radius + spacing);
        double gridHeight = rows * (2 * radius + spacing);
        double startX = (720 - gridWidth) / 2 + radius;
        double startY = (620 - gridHeight) / 2 + radius + 80; 

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
    }

    private void setupColumnButtons() {
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;
        final double buttonWidth = 2 * radius;
        final double buttonHeight = 2 * radius;
        double gridWidth = cols * (2 * radius + spacing);

        HBox buttonBox = new HBox(spacing);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setLayoutY(10);
        buttonBox.setLayoutX((720 - gridWidth) / 2);

        for (int i = 0; i < cols; i++) {
            int col = i;
            Button button = createStyledButton(i);
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
        button.setBorder(new Border(new BorderStroke(Paint.valueOf("#CCCCCC"), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));

        button.setOnMouseEntered(e -> {
            button.setBorder(new Border(new BorderStroke(Paint.valueOf("#AAAAAA"), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
            highlightColumn(col, true);
        });
        button.setOnMouseExited(e -> {
            button.setBorder(new Border(new BorderStroke(Paint.valueOf("#CCCCCC"), BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
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
        Color color = (player == 1) ? PLAYER_1_COLOR : PLAYER_2_COLOR;
        Circle animatedCircle = new Circle(gridCircles[0][col].getCenterX(), gridCircles[0][col].getCenterY(), gridCircles[0][col].getRadius(), color);
        gridPane.getChildren().add(animatedCircle);

        double finalY = gridCircles[row][col].getCenterY();
        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.5), animatedCircle);
        animation.setToY(finalY - animatedCircle.getCenterY());
        animation.setOnFinished(e -> {
            gridPane.getChildren().remove(animatedCircle);
            gridCircles[row][col].setFill(color);
        });

        animation.play();
    }


    public void onColumnClicked(int col) {
        if (!processTurn(col)) {
            return;
        }
        if (HALActivated && game.getCurrentPlayer() == 2) {
            handleAITurn();
        }
    }

    private boolean processTurn(int col) {
        int row = game.placeToken(col);
        if (row == -1) {
            showToast("This column is full. Please choose another column.");
            return false;
        }

        drawToken(col, row, game.getCurrentPlayer());
        return updateGameState();
    }

    private void handleAITurn() {
        int aiColumn;
        do {
            aiColumn = game.decideAITurn();
        } while (!processTurn(aiColumn));
    }

    private boolean updateGameState() {
        boolean win = game.checkForWin();
        if (win) {
            displayMessage("Player " + game.getCurrentPlayer() + " wins!", Color.GREEN);
            disableColumnButtons();
            return false;
        }

        if (game.isDraw()) {
            displayMessage("It's a draw!", Color.ORANGE);
            disableColumnButtons();
            return false;
        }

        game.changeCurrentPlayer();
        displayCurrentPlayerMessage();
        return true;
    }

    private void displayCurrentPlayerMessage() {
        Color currentPlayerColor = (game.getCurrentPlayer() == 1) ? PLAYER_1_COLOR : PLAYER_2_COLOR;
        displayMessage("Player " + game.getCurrentPlayer() + "'s turn", currentPlayerColor);
    }

    private void disableColumnButtons() {
        for (Button button : columnButtons) {
            button.setDisable(true);
        }
    }

    public void displayMessage(String message, Color textColor) {
        messageLabel.setTextFill(textColor);
        messageLabel.setText(message);
    }

    public void showToast(String message) {
        Label toastLabel = new Label(message);
        toastLabel.getStyleClass().add("toast");
        toastLabel.setLayoutX(500);
        toastLabel.setLayoutY(5);
        toastLabel.setStyle("-fx-text-fill: red; -fx-padding: 5px; -fx-font-family: 'Quicksand'; -fx-font-size: 15px;");
        gridPane.getChildren().add(toastLabel);

        toastLabel.applyCss();
        toastLabel.layout();
        toastLabel.setLayoutX(200);
        toastLabel.setOpacity(1.0);

        PauseTransition stayOnScreenDelay = new PauseTransition(Duration.seconds(1));
        stayOnScreenDelay.setOnFinished(e -> {
            Timeline fadeOutTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(toastLabel.opacityProperty(), 0.6)),
                new KeyFrame(Duration.seconds(0.7), new KeyValue(toastLabel.opacityProperty(), 0.0))
            );
            fadeOutTimeline.setOnFinished(event -> gridPane.getChildren().remove(toastLabel));
            fadeOutTimeline.play();
        });

        stayOnScreenDelay.play();
    }

    @FXML
    private void handleMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-fxml.fxml"));
            Parent root = loader.load();
            gridPane.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRestart() {
        resetGame();
        resetUI();
    }

    private void resetGame() {
        game = new MainGame();
    }

    private void resetUI() {
        for (Circle[] row : gridCircles) {
            for (Circle circle : row) {
                circle.setFill(Color.WHITE);
            }
        }

        for (Button button : columnButtons) {
            button.setDisable(false);
        }

        messageLabel.setText("");
        displayMessage("Player 1's turn", PLAYER_1_COLOR);
    }

    public void setHALActivated(boolean HALActivated) {
        this.HALActivated = HALActivated;
    }
}
