package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

public interface PopulationControllerInterface {

    /*
     * Creates a population with a zero patient
     * */
    public Population createPopulation();

    /*
    * Creates a person with valid coordinates
    * */
    public Person createPerson();

    /*
    * Generates random int value to set up person position
    * */
    public int throwRandom(int coordMin, int coordMax);

    /*
     * Move person to a Central Hub with a certain probability
     * */
    public boolean moveToPublicPlace(Person person);

    /*
     * Move person to a Quarantine Zone after 4 days of getting infection
     * */
    public boolean moveToQuarantineZone(Person currentPerson, int currentDay);

    /*
     * Move a single person on the main board
     * */
    public void movePeople(int currentDay);

    /*
     * Move a single person within quarantine zone
     * */
    public void moveWithinQuarantine(Person currentPerson);

    /*
     * Circle size is counted according to the quantity of the population
     * */
    public int countCircleSize(int N);

}
