module ensisa.connect4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens ensisa.connect4 to javafx.fxml;
    exports ensisa.connect4;
}
