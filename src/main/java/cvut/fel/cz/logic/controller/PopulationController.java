package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.hubs.QuarantineZones;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

/**
 * Main simulation controller class for updating Population, Public Place and Quarantine Models.
 * The PopulationController class manages the simulation of a population, including
 * infection dynamics, movement of individuals, and interactions with public places
 * and quarantine zones.
 */
public class PopulationController implements PopulationControllerInterface{
    private QuarantineZones quarantineZone;
    Random random = new Random();
    private final int circleSize;
    private final int populationQuantity;
    private final Population population;
    private PublicPlaces publicPlace;
    private double transmissionProb;
    private int infectiousTimeDays;
    private double infectionRadius;

    /**
     * Constructor for initializing the PopulationController with basic parameters.
     *
     * @param population the Population object to be managed
     * @param populationQuantity the total number of individuals in the population
     * @param transmissionProb the probability of infection transmission
     * @param infectiousTimeDays the number of days an individual remains infectious
     * @param infectionRadius the radius within which infection can occur
     */
    public PopulationController(Population population, int populationQuantity, int transmissionProb, int infectiousTimeDays, double infectionRadius) {
        this.population = population;
        this.circleSize = this.countCircleSize(populationQuantity);
        this.populationQuantity = populationQuantity;
        this.transmissionProb = transmissionProb / 100.0;
        this.infectiousTimeDays = infectiousTimeDays;
        this.infectionRadius = infectionRadius;
        this.publicPlace = null;
        this.quarantineZone = null;
    }

    /**
     * Constructor for initializing the PopulationController with additional public places and quarantine zones.
     *
     * @param population the Population object to be managed
     * @param publicPlaces the PublicPlaces object representing public places in the simulation
     * @param quarantineZone the QuarantineZones object representing quarantine zones in the simulation
     * @param populationQuantity the total number of individuals in the population
     * @param transmissionProb the probability of infection transmission
     * @param infectiousPeriod the number of days an individual remains infectious
     * @param infectionRadius the radius within which infection can occur
     */
    public PopulationController(Population population, PublicPlaces publicPlaces, QuarantineZones quarantineZone, int populationQuantity, int transmissionProb, int infectiousPeriod, double infectionRadius) {
        this(population, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
        this.publicPlace = publicPlaces;
        this.quarantineZone = quarantineZone;
    }

    /**
     * Constructor for initializing the PopulationController with public places.
     *
     * @param population the Population object to be managed
     * @param publicPlaces the PublicPlaces object representing public places in the simulation
     * @param populationQuantity the total number of individuals in the population
     * @param transmissionProb the probability of infection transmission
     * @param infectiousPeriod the number of days an individual remains infectious
     * @param infectionRadius the radius within which infection can occur
     */
    public PopulationController(Population population, PublicPlaces publicPlaces, int populationQuantity, int transmissionProb, int infectiousPeriod, double infectionRadius) {
        this(population, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
        this.publicPlace = publicPlaces;
    }

    /**
     * Constructor for initializing the PopulationController with quarantine zones.
     *
     * @param population the Population object to be managed
     * @param quarantineZone the QuarantineZones object representing quarantine zones in the simulation
     * @param populationQuantity the total number of individuals in the population
     * @param transmissionProb the probability of infection transmission
     * @param infectiousPeriod the number of days an individual remains infectious
     * @param infectionRadius the radius within which infection can occur
     */
    public PopulationController(Population population, QuarantineZones quarantineZone, int populationQuantity, int transmissionProb, int infectiousPeriod, double infectionRadius) {
        this(population, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
        this.quarantineZone = quarantineZone;
    }

    public PublicPlaces getPublicPlaces() {
        return this.publicPlace;
    }
    public QuarantineZones getQuarantineZone() {
        return this.quarantineZone;
    }

    /**
     * Creates a population by adding individuals to the Population object.
     * The first individual is set to be infectious.
     *
     * @return the updated Population object
     */
    @Override
    public Population createPopulation() {
        for (int i = 0; i < this.populationQuantity; i++) {
            Person person = this.createPerson();
            if (i == 0) {
                person.changeStatusToInfectious(1);
            }
            this.population.addPerson(person);
        }
        return this.population;
    }

    public double getTransmissionProb() {
        return this.transmissionProb;
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

    /**
     * Creates a new Person object with random initial coordinates and movement vectors.
     *
     * @return a new Person object
     */
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

    /**
     * Generates a random integer between the specified minimum and maximum values.
     *
     * @param coordMin the minimum value
     * @param coordMax the maximum value
     * @return a random integer between coordMin and coordMax
     */
    @Override
    public int throwRandom(int coordMin, int coordMax) {
        int x = this.random.nextInt(coordMax - coordMin) + coordMin;
        return x;
    }

    /**
     * Moves a person to a public place with a certain probability.
     * The person is moved only if the public place has available capacity.
     *
     * @param person the Person object to be moved
     * @return true if the person is moved to the public place, false otherwise
     */
    @Override
    public boolean moveToPublicPlace(Person person) {
        // There is a certain probability that the person will visit the hub
        if (this.random.nextDouble() < 0.001 && this.publicPlace.getOccupancy() < this.publicPlace.getCapacity()) {
            person.moveToHub();
            this.publicPlace.incrementOccupancy();
            return true;
        }
        return false;
    }

    /**
     * Moves a person to a quarantine zone based on certain conditions.
     * The person is moved if they are infectious, have been infectious for at least 4 days,
     * and there is available capacity in the quarantine zone.
     *
     * @param currentPerson the Person object to be moved
     * @param currentDay the current day of the simulation
     * @return true if the person is moved to the quarantine zone, false otherwise
     */
    @Override
    public boolean moveToQuarantineZone(Person currentPerson, int currentDay) {
        // Person is moved to a Quarantine Zone after 4 days of getting infection and only if there is available places
        if (this.quarantineZone.getOccupancy() < this.quarantineZone.getCapacity() && currentPerson.getStatus() == PersonStatus.Infectious && !currentPerson.getQuarantineStatus() && currentPerson.getReceivingInfectionDay() <= currentDay - 4) {
            currentPerson.moveToQuarantine();
            this.quarantineZone.incrementOccupancy();
            return true;
        }
        return false;
    }

    /**
     * Moves people within the population based on their status and updates their infection status if necessary.
     * Infectious individuals can infect susceptible individuals within the infection radius.
     *
     * @param currentday the current day of the simulation
     */
    @Override
    public void movePeople(int currentday) {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            Person currentPerson = this.population.getPerson(i);
            currentPerson.changeRecoveryTime(this.infectiousTimeDays); // update infection period if necessary

            if (currentPerson.getMovingStatus()) {
                continue;
            }

            if (currentPerson.getQuarantineStatus()) {
                this.moveWithinQuarantine(currentPerson);
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
                this.addNewInfectious(i, newX, newY, currentday);
            }
        }

    }


    private void moveWithinQuarantine(Person currentPerson) {
        double newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
        double newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();

        // Move within quarantine zone coordinates
        boolean isValidX = newX > 260 + circleSize && newX < 260 + 90 - circleSize;
        boolean isValidY = newY > 320 + circleSize && newY < 320 + 90 - circleSize;

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

    private void addNewInfectious(int personI, double infectedX, double infectedY, int currentDay) {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            if (i == personI) {
                continue;
            }
            Person personS = this.population.getPerson(i);
            if (personS.getStatus() == PersonStatus.Susceptible) {
                double suscepX = personS.getX();
                double suscepY = personS.getY();

                double distance = Math.sqrt(Math.pow(infectedX - suscepX, 2) + Math.pow(infectedY - suscepY, 2));

                // Check if the person is within the infection radius
                if (distance > this.infectionRadius * this.circleSize) {
                    continue;
                }
                // Get infection according to a certain probability
                if (this.random.nextDouble() <= transmissionProb) {
                    personS.changeStatusToInfectious(currentDay);
                }
            }
        }
    }

    /**
     * Counts the circle size based on the population size.
     *
     * @param N the population size
     * @return the circle size
     */
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
