package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;

import java.util.Random;

public interface PopulationControllerInterface {

    public Population createPopulation();

    public Person createPerson();

    public int throwRandom(int coordMin, int coordMax);

    public boolean moveToPublicPlace(Person person);

    public boolean moveToQuarantineZone(Person currentPerson, int currentDay);

    public void movePeople(int currentDay);

    public int countCircleSize(int N);

}
