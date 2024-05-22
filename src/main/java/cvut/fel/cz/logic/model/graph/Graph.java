package cvut.fel.cz.logic.model.graph;

import cvut.fel.cz.logic.model.population.Population;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the graph model that keeps track of the simulation statistics over time.
 */
public class Graph {
    private final List<Day> days;
    private int lastDay;
    private final Population population;

    /**
     * Constructs a Graph object with the specified population.
     *
     * @param population the Population object representing the simulation population
     */
    public Graph(Population population) {
        days = new ArrayList<>();
        this.population = population;
        this.lastDay = 0;
    }

    /**
     * Adds a new day to the graph with the current statistics from the population.
     * This increments the day counter and records the number of susceptible, infectious, and recovered individuals.
     */
    public void addNewDay() {
        this.lastDay++;
        int qS = this.population.getSusceptibleQuantity();
        int qI = this.population.getInfectiousQuantity();
        int qR = this.population.getRecoveredQuantity();
        Day currentDay = new Day(lastDay, qS, qI, qR);
        this.days.add(currentDay);
    }
    public int getLastDayNum() {
        return lastDay;
    }

    public Day getLastDay() {
        return this.days.get(lastDay-1);
    }

}