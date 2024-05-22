package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;

/**
 * Controller class for updating Graph model and Simulation Page Statistics area.
 * The StatisticsController class manages the statistics of the simulation, including
 * updating the graph model and handling the simulation name.
 */
public class StatisticsController {
    private final Graph graph;
    private final String simulationName;


    /**
     * Constructs a StatisticsController with the specified graph model and simulation name.
     *
     * @param graph the Graph object representing the simulation statistics
     * @param simulationName the name of the simulation
     */
    public StatisticsController(Graph graph, String simulationName) {
        this.graph = graph;
        this.simulationName = simulationName;
    }

    /**
     * Gets the name of the simulation.
     * This function is used to save the simulation name in JSON format.
     *
     * @return the name of the simulation
     */
    public String getSimulationName() {
        return this.simulationName;
    }

    /**
     * Adds a new day into the Graph model.
     * This function updates the statistics by recording the passage of a new day in the simulation.
     */
    public void updateStatistics () {
        this.graph.addNewDay();
    }

    /**
     * Initializes the graph model connected to this controller.
     * This function returns the graph model for further operations or manipulations.
     *
     * @return the initialized Graph object
     */
    public Graph initGraph() {
        return this.graph;
    }
}
