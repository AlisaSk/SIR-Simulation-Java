package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.graph.Day;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;

import java.util.List;
import java.util.Random;

public class PopulationController implements PopulationControllerInterface{
    Random random = new Random();
    private final int circleSize;
    private final int N;
    private final Population population;
    private final Graph graph;
    public PopulationController(Population population, int N) {
        this.population = population;
        this.graph = new Graph();
        this.circleSize = this.countCircleSize(N);
        this.N = N;
    }

    @Override
    public Population createPopulation() {
        for (int i = 0; i < this.N; i++) {
            Person person = this.createPerson();
            if (i == 0) {
                person.changeStatusToInfectious();
            }
            this.population.addPerson(person);
        }
        return this.population;
    }

    @Override
    public Person createPerson() {
        int xMin = 400 + this.circleSize;
        int xMax = 400 + 380 - this.circleSize;
        int x = throwRandom(xMin, xMax);
        int yMin = 30 + this.circleSize;
        int yMax = 30 + 380 - this.circleSize;
        int y = throwRandom(yMin, yMax);
//        !!!!!!!!!!!!!!!!!! change delta in moves so person will have different directions
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
        for (int i = 0; i < this.population.getQuantity(); i++) {
            Person currentPerson = this.population.getPerson(i);

            double newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            double newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();

            boolean isValidX = newX > 400 + circleSize && newX < 400 + 380 - circleSize;
            boolean isValidY = newY > 30 + circleSize && newY < 30 + 380 - circleSize;

            if (!isValidX) {
                currentPerson.updateDelX();
                newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            }
            if (!isValidY) {
                currentPerson.updateDelY();
                newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();
            }

            currentPerson.move(newX, newY);

            if (currentPerson.getStatus() == PersonStatus.Infectious) {
                this.addNewInfectious(i, newX, newY, 1.6);
            }
        }

    }

    @Override
    public void addNewInfectious(int personI, double infectedX, double infectedY, double radius) {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            if (i == personI) {
                continue;
            }
            Person personS = this.population.getPerson(i);
            if (personS.getStatus() == PersonStatus.Susceptible) {
                double suscepX = personS.getX();
                double suscepY = personS.getY();

                double distance = Math.sqrt(Math.pow(infectedX - suscepX, 2) + Math.pow(infectedY - suscepY, 2));

                // TODO здесь должно быть расстояние которое зависит от размера кружка
                if (distance > radius * this.circleSize) {
                    continue;
                }

                if (this.random.nextDouble() < 0.2) {
                    personS.changeStatusToInfectious();
                }
            }
        }
    }

    @Override
    public void addNewRecovered() {

    }

    @Override
    public int countCircleSize(int N) {
        // TODO add the circleSize as the parameter for user
        int largeSize = 25;
        int bigSize = 15;
        int mediumSize = 10;
        int smallSize = 4;
        int tinySize = 2;

        if (N <= 20) {
            return largeSize;
        }
        if (N <= 65) {
            return bigSize;
        }
        if (N <= 150) {
            return  mediumSize;
        }
        if (N <= 400) {
            return smallSize;
        }

        return tinySize;
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
