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
        Population population = new Population(); // model
        Graph graph = new Graph(population);
        int N = 250;
        PopulationController populationController = new PopulationController(population, N); // Population controller
        StatisticsController statisticsController = new StatisticsController(graph); // Statistics controller
        SimulationPageView simulationPageView = new SimulationPageView(populationController, statisticsController, N); // Simulation Page view
        Scene simulationScene = simulationPageView.start();

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(simulationScene); // closing previous page
    }

//    @FXML
//    void initialize() {
//
//    }

}
