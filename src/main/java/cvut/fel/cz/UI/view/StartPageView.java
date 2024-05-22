package cvut.fel.cz.UI.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

public class StartPageView {
    public static final Logger LOGGER = Logger.getLogger(StartPageView.class.getName());
    @FXML
    void start(ActionEvent event){
        LOGGER.info("Start button is clicked");
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene parametersScene = parametersPageView.start(); // loading parameters page

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(parametersScene); // closing previous page
    }

}
