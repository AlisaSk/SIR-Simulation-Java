package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.*;
import javafx.scene.image.Image;


public class SimulationPageController {
    Population population;
    PopulationController populationController;
    StatisticsController statisticsController;
    int quantity;
    public SimulationPageController(Population population) {
        this.quantity = 100;
    }

    public void updateSimulation() {

    }
}
