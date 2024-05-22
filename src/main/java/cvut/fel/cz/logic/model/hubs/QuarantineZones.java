package cvut.fel.cz.logic.model.hubs;

/**
 * Represents quarantine zones in the simulation where infected individuals can be isolated.
 * This class manages the quantity, capacity, and occupancy of these quarantine zones.
 */
public class QuarantineZones {
    private int placeCapacity;
    private int initialCapacity;
    private final int placeQuantity;

    /**
     * Constructs a QuarantineZones object with the specified quantity and initial capacity.
     *
     * @param placeQuantity the number of quarantine zones
     * @param placeCapacity the initial capacity of each quarantine zone
     */
    public QuarantineZones(int placeQuantity, int placeCapacity) {
        this.placeQuantity = placeQuantity;
        this.placeCapacity = 0;
        this.initialCapacity = placeCapacity;
    }

    public int getOccupancy() {
        return this.placeCapacity;
    }

    public int getCapacity() {
        return this.initialCapacity;
    }
    public void updateCapacity(int newCapacity) {
        this.initialCapacity = newCapacity;
    }

    public void incrementOccupancy() {
        this.placeCapacity++;
    }

    public void decreasePlaceCapacity() {
        this.placeCapacity--;
    }
}
