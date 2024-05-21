package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;


public class StatisticsController {
    private final Graph graph;
    private final String simulationName;
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
