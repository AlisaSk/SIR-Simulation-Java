package cvut.fel.cz.logic.model.hubs;

public class PublicPlaces {
    private final int placeQuantity;
    private int placeCapacity;
    private int initialCapacity;

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
