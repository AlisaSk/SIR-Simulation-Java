package cvut.fel.cz.logic.model.graph;

/**
 * Represents a single day in the simulation with statistics on susceptible, infectious, and recovered individuals.
 */
public class Day {

    private final int dayNumber;
    private final int quantitySusceptible;
    private final int quantityInfectious;
    private final int quantityRecovered;

    /**
     * Constructs a Day with the specified statistics.
     *
     * @param dayNumber the day number
     * @param quantitySusceptible the number of susceptible individuals
     * @param quantityInfectious the number of infectious individuals
     * @param quantityRecovered the number of recovered individuals
     */
    public Day(int dayNumber, int quantitySusceptible, int quantityInfectious, int quantityRecovered) {
        this.dayNumber = dayNumber;
        this.quantitySusceptible = quantitySusceptible;
        this.quantityInfectious = quantityInfectious;
        this.quantityRecovered = quantityRecovered;
    }
    public int getDaySusceptible() {return this.quantitySusceptible; }

    public int getDayInfected() {return this.quantityInfectious; }

    public int getDayRecovered() {return this.quantityRecovered; }
}
