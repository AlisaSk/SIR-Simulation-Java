package cvut.fel.cz.logic.model.population;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;

import java.util.ArrayList;
import java.util.List;

public class Population {

    private List<Person> population;

    public Population() {
        this.population = new ArrayList<>();
    }


    /*
    * adding person to the population
    * */
    public void addPerson(Person person) {
        this.population.add(person);
    }

    public Person getPerson(int index) {
        return this.population.get(index);
    }
    /*
    * get population size
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

    /*
    * get S quantity
    * */
    public int getSusceptibleQuantity () {
        return getQuantityByStatus(PersonStatus.Susceptible);
    }

    /*
     * get I quantity
     * */
    public int getInfectiousQuantity () {
        return getQuantityByStatus(PersonStatus.Infectious);
    }

    /*
     * get R quantity
     * */
    public int getRecoveredQuantity () {
        return getQuantityByStatus(PersonStatus.Recovered);
    }
}
