package cvut.fel.cz.logic.model.population;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a population of individuals in the simulation, managing their addition,
 * retrieval, and status-based quantity calculations.
 */
public class Population {

    private List<Person> population;

    /**
     * Constructs an empty Population.
     */
    public Population() {
        this.population = new ArrayList<>();
    }

    /**
     * Adds a person to the population.
     *
     * @param person the person to be added
     */
    public void addPerson(Person person) {
        this.population.add(person);
    }

    /**
     * Retrieves a person from the population by index.
     *
     * @param index the index of the person to retrieve
     * @return the person at the specified index
     */
    public Person getPerson(int index) {
        return this.population.get(index);
    }
    /*
    * Get population size
    * */
    public int getQuantity() {
        return population.size();
    }

    private int getQuantityByStatus(PersonStatus status) {
        int count = 0;
        for (Person person: this.population) {
            if (person.getStatus() == status) {
                count++;
            }
        }
        return count;
    }

    /**
     * Gets the quantity of susceptible individuals in the population.
     *
     * @return the number of susceptible individuals
     */
    public int getSusceptibleQuantity () {
        return getQuantityByStatus(PersonStatus.Susceptible);
    }

    /**
     * Gets the quantity of infectious individuals in the population.
     *
     * @return the number of infectious individuals
     */
    public int getInfectiousQuantity () {
        return getQuantityByStatus(PersonStatus.Infectious);
    }

    /**
     * Gets the quantity of recovered individuals in the population.
     *
     * @return the number of recovered individuals
     */
    public int getRecoveredQuantity () {
        return getQuantityByStatus(PersonStatus.Recovered);
    }
}
