package cvut.fel.cz.logic.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cvut.fel.cz.Main;
import cvut.fel.cz.UI.view.SimulationPageView;
import cvut.fel.cz.logic.model.population.Population;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void start(ActionEvent event) throws IOException {
        Population population = new Population();
        PopulationController populationController = new PopulationController(population);
        SimulationPageView simulationPageView = new SimulationPageView(populationController);
        Scene simulationScene = simulationPageView.showSimulationWindow();

        Node source = (Node) event.getSource(); // Источник события - кнопка, на которую нажали
        Stage currentStage = (Stage) source.getScene().getWindow(); // Получаем Stage текущего окна
        currentStage.setScene(simulationScene); // Закрываем текущее окно
    }

    @FXML
    void initialize() {

    }

}
