package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;

public class StatisticsController {

    /*
    * some functions of the PopulationController will be placed here in order to
    * separate the functions of managing statistics and population
    * (Statistics will be responsible for graph)
    * */
    private Graph graph;

    public StatisticsController(Graph graph) {
        this.graph = graph;
    }

}
