package cvut.fel.cz.logic.model.hubs;

public class PublicPlaces {
    private final int placeQuantity;
    private int placeCapacity;
    private final int initialCapacity;

    public PublicPlaces(int placeQuantity, int placeCapacity) {
        this.placeQuantity = placeQuantity;
        this.placeCapacity = 0;
        this.initialCapacity = placeCapacity;
    }

    public int getPublicPlacesQuantity() {
        return this.placeQuantity;
    }

    public int getPlacesCapacity() {
        return this.placeCapacity;
    }

    public int getInitialCapacity() {
        return this.initialCapacity;
    }

    public void incrementPlaceCapacity() {
        this.placeCapacity++;
    }

    public void decreasePlaceCapacity() {
        this.placeCapacity--;
    }
}
