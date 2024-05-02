package cvut.fel.cz.UI.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartPageView {

//    @FXML
//    private ResourceBundle resources;
//
//    @FXML
//    private URL location;

    @FXML
    void start(ActionEvent event) throws IOException {
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene parametersScene = parametersPageView.start();

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(parametersScene); // closing previous page
    }

//    @FXML
//    void initialize() {
//
//    }

}
