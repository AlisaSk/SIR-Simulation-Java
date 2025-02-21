package cvut.fel.cz.logic.controller;

import static org.junit.jupiter.api.Assertions.*;
import cvut.fel.cz.logic.model.hubs.QuarantineZones;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;
import org.junit.jupiter.api.Test;

class PopulationControllerTest {

    @Test
    void createPopulation_checkQuantity() {
        int expected = 5;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 5, 0, 0, 0.0);
        populationController.createPopulation();
        int actual = population.getQuantity(); // check if all people were added

        assertEquals(expected, actual);
    }

    @Test
    void createPopulation_zeroPatient() {
        int expected = 1;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 100, 0, 0, 0.0);
        populationController.createPopulation();
        int actual = population.getInfectiousQuantity(); // check if initialized population has zero patient

        assertEquals(expected, actual);
    }

    @Test
    void createPerson_validCoords() {
        int expected = 1;
        int actual = 1;
        int circleSize = 10;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 100, 0, 0, 0.0);
        populationController.createPopulation();
        for (int i = 0; i < 100; i++) {
            Person currentPerson = population.getPerson(i);
            double x = currentPerson.getX();
            double y = currentPerson.getY();

            boolean isValidX = x >= 400 + circleSize && x <= 400 + 380 - circleSize; // checking the range of X (according to populationBoarder coords)
            boolean isValidY = y >= 30 + circleSize && y <= 30 + 380 - circleSize; // checking the range of Y (according to populationBoarder coords)
            if (!isValidX || !isValidY) {
                actual = -1;
                break;
            }
        }

        assertEquals(expected, actual);
    }

    @Test
    void throwRandom_validValue() {
        int expected = 1;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 5, 0, 0, 0.0);
        int xMin = 100;
        int xMax = 200;
        int value = populationController.throwRandom(xMin, xMax);
        int actual = value >= 100 && value <= 200 ? 1: 0; // checking the range

        assertEquals(expected, actual);
    }

    @Test
    void movePeople() {
        int circleSize = 10;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 100, 0, 0, 0.0);
        populationController.createPopulation();
        populationController.movePeople(1);

        for (int i = 0; i < 100; i++) {
            Person currentPerson = population.getPerson(i);
            double x = currentPerson.getX();
            double y = currentPerson.getY();

            boolean isValidX = x >= 400 + circleSize && x <= 400 + 380 - circleSize;
            boolean isValidY = y >= 30 + circleSize && y <= 30 + 380 - circleSize;

            assertTrue(isValidX && isValidY);
        }
    }

    @Test
    void moveToQuarantineZone_validMovement() {
        Population population = new Population();
        QuarantineZones quarantineZones = new QuarantineZones(1, 50); // assuming capacity is 50
        PopulationController populationController = new PopulationController(population, quarantineZones, 10, 50, 10, 1.0);

        Person person = populationController.createPerson();
        person.changeStatusToInfectious(1);
        boolean moved = populationController.moveToQuarantineZone(person, 5);

        assertTrue(moved);
        assertEquals(1, quarantineZones.getOccupancy());
        assertTrue(person.getQuarantineStatus());
    }

    @Test
    void changeInfectionPeriod() {
        int newPeriod = 14;

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 10, 50, 7, 1.0);
        populationController.setInfectiousTimeDays(newPeriod);

        assertEquals(newPeriod, populationController.getInfectionPeriod());
    }

    @Test
    void addNewInfectious() {
        int expected = 2;
        int actual = 1; // one infectious is here by default (zero patient always is in the population)

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 2, 0, 0, 0.0);
        populationController.createPopulation();
        populationController.movePeople(1);

        for (int i = 0; i < 2; i++) {
            Person currentPerson = population.getPerson(i);
            if (currentPerson.getStatus() == PersonStatus.Susceptible) {
                currentPerson.changeStatusToInfectious(1);
                actual++;
            }
        }

        assertEquals(expected, actual);
    }

    @Test
    void countCircleSize() {
        int expected = 10; // medium size for populationQ = 100

        Population population = new Population();
        PopulationController populationController = new PopulationController(population, 100, 0, 0, 0.0);
        int actual = populationController.countCircleSize(100);

        assertEquals(expected, actual);
    }
}