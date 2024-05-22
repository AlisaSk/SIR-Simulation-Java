package cvut.fel.cz.UI.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * The StartPageView class represents the view for the start page of the application. It contains start
 * button to access the parameters page.
 */
public class StartPageView {
    public static final Logger LOGGER = Logger.getLogger(StartPageView.class.getName());

    /**
     * Handles the action event when the start button is clicked. It initializes the parameters page view
     * and switches the scene to the parameters page.
     *
     * @param event The action event triggered by clicking the start button.
     */
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
