package cvut.fel.cz.logic.model.graph;

public class Day {

    public int dayNumber;
    public int quantitySusceptible;
    public int quantityInfectious;
    public int quantityRecovered;

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
