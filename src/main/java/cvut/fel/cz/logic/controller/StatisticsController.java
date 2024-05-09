package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Day;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;
import javafx.scene.chart.StackedAreaChart;

public class StatisticsController {
    private Graph graph;
    private String simulationName;
    /*
     * some functions of the PopulationController will be placed here in order to
     * separate the functions of managing statistics and population
     * (Statistics will be responsible for graph)
     * */
    public StatisticsController(Graph graph, String simulationName) {
        this.graph = graph;
        this.simulationName = simulationName;
    }

    public String getSimulationName() {
        return this.simulationName;
    }

    public void updateStatistics () {
        this.graph.addNewDay();
    }

    public Graph initGraph() {
        return this.graph;
    }
}
