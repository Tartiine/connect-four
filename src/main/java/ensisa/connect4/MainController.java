package ensisa.connect4;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
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

public class MainController {

    @FXML
    private Pane gridPane;
    private Circle[][] gridCircles;

    public void initialize() {
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
        double startY = (620 - gridHeight) / 2 + radius + buttonHeight + 10; // Ajouter 10 pixels de marge

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
            int col = i; // Variable finale pour l'utilisation dans l'expression lambda
            Button button = createStyledButton(col);
            button.setPrefSize(buttonWidth, buttonHeight);
            button.setOnAction(event -> {
                // Logic to handle token drop in column i
            });
            buttonBox.getChildren().add(button);
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

        // Style au survol
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
}
