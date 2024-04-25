package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Day;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;

import java.util.List;
import java.util.Random;

public class PopulationController implements PopulationControllerInterface{
    Random random = new Random();
    private final Population population;
    private final Graph graph;
    public PopulationController(Population population) {
        this.population = population;
        this.graph = new Graph();
    }

    @Override
    public Population createPopulation(int N) {
        for (int i = 0; i < N; i++) {
            Person person = this.createPerson();
            if (i % 4 == 0) {
                person.changeStatusToInfectious();
            }
            else if (i % 10 == 0) {
                person.changeStatusToRecovered();
            }
            this.population.addPerson(person);
        }
        return this.population;
    }

    @Override
    public Person createPerson() {
//        криво, нужно обосновать откуда эти цифры
        int xMin = 405;
        int xMax = 775;
        int x = throwRandom(xMin, xMax);
        int yMin = 35;
        int yMax = 405;
        int y = throwRandom(yMin, yMax);
//        !!!!!!!!!!!!!!!!!!
        double dx = this.random.nextDouble() * 2 - 1; // Скорость от -5 до 5
        double dy = this.random.nextDouble() * 2 - 1;
        Person person = new Person(x, y, dx, dy);
        return person;
    }

    @Override
    public int throwRandom(int coordMin, int coordMax) {
        int x = this.random.nextInt(coordMax - coordMin) + coordMin;
        return x;
    }

    @Override
    public void movePeople() {
//        криво, нужно добавлять скорость к каждому персону
        for (int i = 0; i < this.population.getQuantity(); i++) {
            Person currentPerson = this.population.getPerson(i);
//            сделать движение по дабл, не по интам
            double newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            double newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();

            boolean isValidX = newX > 405 && newX < 775;
            boolean isValidY = newY > 35 && newY < 405;

            if (!isValidX) {
                currentPerson.updateDelX();
                newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            }
            if (!isValidY) {
                currentPerson.updateDelY();
                newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();
            }

            currentPerson.move(newX, newY);
        }

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
