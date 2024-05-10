package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

public interface PopulationControllerInterface {

    public Population createPopulation();

    /*
    * creates a person
    * */
    public Person createPerson();

    /*
    * generates random int value to set up person position
    * */
    public int throwRandom(int coordMin, int coordMax);

    /*
     * move each person in the city
     * */
    public void movePeople();


    /*
     * change some people status in the population according to their contact with other I
     * (will be used radius of the infection and contacting time)
     * */
    public void addNewInfectious(int personI, double infectedX, double infectedY);


    public int countCircleSize();

}
