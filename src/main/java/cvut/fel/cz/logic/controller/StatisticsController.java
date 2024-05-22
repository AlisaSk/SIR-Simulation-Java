package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;


public class StatisticsController {
    private final Graph graph;
    private final String simulationName;
    /**
     * Controller class for updating Graph model and Simulation Page Statistics area
     */
    public StatisticsController(Graph graph, String simulationName) {
        this.graph = graph;
        this.simulationName = simulationName;
    }

    /**
     * Function is used to save simulation name in json
     */
    public String getSimulationName() {
        return this.simulationName;
    }

    /**
     * Function is used to add new day into Graph model
     */
    public void updateStatistics () {
        this.graph.addNewDay();
    }

    /**
     * Function is used to initialize the model connected to this controller
     */
    public Graph initGraph() {
        return this.graph;
    }
}
