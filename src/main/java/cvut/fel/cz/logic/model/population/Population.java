package cvut.fel.cz.logic.model.population;

import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;

import java.util.ArrayList;
import java.util.List;

public class Population {
    /*спросить где лучше генерировать персонов */

    private List<Person> population;

    public Population() {
        this.population = new ArrayList<>();
    }

    public void addPerson(Person person) {
        this.population.add(person);
    }

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

    public int getSusceptibleQuantity () {
        return getQuantityByStatus(PersonStatus.Susceptible);
    }

    public int getInfectiousQuantity () {
        return getQuantityByStatus(PersonStatus.Infectious);
    }

    public int getRecoveredQuantity () {
        return getQuantityByStatus(PersonStatus.Recovered);
    }
}
