package cvut.fel.cz.logic.model.graph;

import cvut.fel.cz.logic.model.population.Population;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Day> days;

    private int lastDay;
    private Population population;

    public Graph(Population population) {
        days = new ArrayList<>();
        this.population = population;
        this.lastDay = 0;
    }

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