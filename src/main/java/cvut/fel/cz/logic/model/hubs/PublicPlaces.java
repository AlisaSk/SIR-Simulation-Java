package cvut.fel.cz.logic.model.hubs;

/**
 * Represents public places in the simulation where people can gather.
 * This class manages the quantity, capacity, and occupancy of these public places.
 */
public class PublicPlaces {
    private final int placeQuantity;
    private int placeCapacity;
    private int initialCapacity;

    /**
     * Constructs a PublicPlaces object with the specified quantity and initial capacity.
     *
     * @param placeQuantity the number of public places
     * @param placeCapacity the initial capacity of each public place
     */
    public PublicPlaces(int placeQuantity, int placeCapacity) {
        this.placeQuantity = placeQuantity;
        this.placeCapacity = 0;
        this.initialCapacity = placeCapacity;
    }

    public int getPublicPlacesQuantity() {
        return this.placeQuantity;
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

    public void decreasePlaceOccupancy() {
        this.placeCapacity--;
    }
}
