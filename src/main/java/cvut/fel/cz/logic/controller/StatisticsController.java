package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;


public class StatisticsController {
    private final Graph graph;
    private final String simulationName;
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
