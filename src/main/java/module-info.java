module cvut.fel.cz {
    requires javafx.controls;
    requires javafx.fxml;

    opens cvut.fel.cz to javafx.fxml;
    exports cvut.fel.cz;

    exports cvut.fel.cz.logic.controller to javafx.fxml;
    opens cvut.fel.cz.logic.controller to javafx.fxml;
    exports cvut.fel.cz.UI.view to javafx.fxml;
    opens cvut.fel.cz.UI.view to javafx.fxml;
}