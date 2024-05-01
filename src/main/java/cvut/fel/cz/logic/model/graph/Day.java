package cvut.fel.cz.logic.model.graph;

public class Day {

    private final int dayNumber;
    private final int quantitySusceptible;
    private final int quantityInfectious;
    private final int quantityRecovered;

    /*
    * initialize the day
    * */
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
