package cvut.fel.cz.logic.model.hubs;

public class QuarantineZones {
    private int placeCapacity;
    private int initialCapacity;
    private final int placeQuantity;
    public QuarantineZones(int placeQuantity, int placeCapacity) {
        this.placeQuantity = placeQuantity;
        this.placeCapacity = 0;
        this.initialCapacity = placeCapacity;
    }

    public int getAvailability() {
        return this.placeCapacity;
    }

    public int getInitialCapacity() {
        return this.initialCapacity;
    }
    public void changeInitialCapacity(int newCapacity) {
        this.initialCapacity = newCapacity;
    }

    public void incrementPlaceCapacity() {
        this.placeCapacity++;
    }

    public void decreasePlaceCapacity() {
        this.placeCapacity--;
    }
}
