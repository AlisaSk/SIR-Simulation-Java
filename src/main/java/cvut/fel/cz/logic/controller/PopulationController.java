package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

public class PopulationController implements PopulationControllerInterface{
    Random random = new Random();
    private final int circleSize;
    private final int populationQuantity;
    private final Population population;
    private final PublicPlaces publicPlace;
    private double transmissionProb;
    private int infectiousTimeDays;
    private double infectionRadius;

    public PopulationController(Population population, PublicPlaces publicPlaces, int populationQuantity, int transmissionProb, int infectiousPeriod, double infectionRadius) {
        this.circleSize = this.countCircleSize(populationQuantity);
        this.population = population;
        this.publicPlace = publicPlaces;
        this.populationQuantity = populationQuantity;
        this.transmissionProb = transmissionProb;
        this.infectiousTimeDays = infectiousPeriod;
        this.infectionRadius = infectionRadius;
    }

    // Constructor without publicPlaces
    public PopulationController(Population population, int populationQuantity, int transmissionProb, Integer infectiousPeriod, double infectionRadius) {
        this(population, null, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
    }

    // Constructor without infectiousPeriod
    public PopulationController(Population population, PublicPlaces publicPlaces, int populationQuantity, int transmissionProb, double infectionRadius) {
        this(population, publicPlaces, populationQuantity, transmissionProb, 7, infectionRadius);
    }

    // Constructor with only essential parameters
    public PopulationController(Population population, int populationQuantity, int transmissionProb, double infectionRadius) {
        this(population, null, populationQuantity, transmissionProb, 7, infectionRadius);
    }

    public PublicPlaces getPublicPlaces() {
        return this.publicPlace;
    }

    @Override
    public Population createPopulation() {
        for (int i = 0; i < this.populationQuantity; i++) {
            Person person = this.createPerson();
            if (i == 0) {
                person.changeStatusToInfectious();
            }
            this.population.addPerson(person);
        }
        return this.population;
    }

    public void setInfectionRadius(double infectionRadius) {
        this.infectionRadius = infectionRadius;
    }
    public double getInfectionRadius() {
        return this.infectionRadius;
    }
    public void setInfectiousTimeDays(int newPeriod) {
        this.infectiousTimeDays = newPeriod;
    }
    public double getInfectionPeriod() {
        return this.infectiousTimeDays;
    }

    @Override
    public Person createPerson() {
        int xMin = 400 + this.circleSize;
        int xMax = 400 + 380 - this.circleSize;
        int x = throwRandom(xMin, xMax);
        int yMin = 30 + this.circleSize;
        int yMax = 30 + 380 - this.circleSize;
        int y = throwRandom(yMin, yMax);
        double dx = this.random.nextDouble() * 2 - 1;
        double dy = this.random.nextDouble() * 2 - 1;
        Person person = new Person(x, y, dx, dy, this.infectiousTimeDays);
        return person;
    }

    @Override
    public int throwRandom(int coordMin, int coordMax) {
        int x = this.random.nextInt(coordMax - coordMin) + coordMin;
        return x;
    }

    public boolean moveToPublicPlace(Person person) {
        if (this.random.nextDouble() < 0.001 && this.publicPlace.getAvailability() < this.publicPlace.getInitialCapacity()) {
            person.moveToHub();
            this.publicPlace.incrementPlaceCapacity();
            return true;
        }
        return false;
    }

    @Override
    public void movePeople() {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            Person currentPerson = this.population.getPerson(i);
            currentPerson.changeRecoveryTime(this.infectiousTimeDays); // update infection period if necessary

            if (currentPerson.getMovingStatus()) {
                continue;
            }

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
                this.addNewInfectious(i, newX, newY);
            }
        }

    }

    private void addNewInfectious(int personI, double infectedX, double infectedY) {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            if (i == personI) {
                continue;
            }
            Person personS = this.population.getPerson(i);
            if (personS.getStatus() == PersonStatus.Susceptible) {
                double suscepX = personS.getX();
                double suscepY = personS.getY();

                double distance = Math.sqrt(Math.pow(infectedX - suscepX, 2) + Math.pow(infectedY - suscepY, 2));

                if (distance > this.infectionRadius * this.circleSize) {
                    continue;
                }

                if (this.random.nextDouble() <= transmissionProb) {
                    personS.changeStatusToInfectious();
                }
            }
        }
    }

    @Override
    public int countCircleSize(int N) {
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

}
