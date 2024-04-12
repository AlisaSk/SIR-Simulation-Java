package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Day;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

public class PopulationController implements PopulationControllerInterface{
    private Population population;
    private Graph graph;
    private final int populationSize;
    public PopulationController() {
        this.population = new Population();
        this.populationSize = population.getQuantity();
        this.graph = new Graph();
    }

    @Override
    public void createPopulation() {
        for (int i = 0; i < this.populationSize; i++) {
            Person person = this.createPerson();
            this.population.addPerson(person);
        }
    }

    @Override
    public Person createPerson() {
        int x = throwRandom(500);
        int y = throwRandom(500);
        Person person = new Person(x, y);
        return person;
    }

    @Override
    public int throwRandom(int length) {
        Random random = new Random();
        int x = random.nextInt(length);
        return x;
    }

    @Override
    public void movePeople() {

    }

    @Override
    public void addNewInfectious() {

    }

    @Override
    public void addNewRecovered() {

    }

    @Override
    public void updateStatistics(int dayNum) {
        int countS = population.getSusceptibleQuantity();
        int countI = population.getInfectiousQuantity();
        int countR = population.getRecoveredQuantity();
        Day day = new Day(dayNum, countS, countI, countR);
        this.graph.addNewDay(day);
    }
}
