package cvut.fel.cz.logic.model.person;

/**
 * Represents a person in the simulation with attributes such as status, position, movement, and infection details.
 */
public class Person {
    private PersonStatus status;
    private double x;
    private double y;
    private double delX;
    private double delY;
    private int infectiousTimeDays;
    private int receivingInfectionDay;
    private boolean isInQuarantine;
    private boolean isMovingToPlace;

    /**
     * Constructs a Person with the specified position, movement direction, and infectious period.
     *
     * @param x the initial x-coordinate of the person
     * @param y the initial y-coordinate of the person
     * @param delX the change in x-coordinate for movement
     * @param delY the change in y-coordinate for movement
     * @param infectiousTimeDays the number of days the person remains infectious
     */
    public Person(double x, double y, double delX, double delY, int infectiousTimeDays) {
        this.x = x;
        this.y = y;
        this.delX = delX;
        this.delY = delY;
        this.status = PersonStatus.Susceptible;
        this.infectiousTimeDays = infectiousTimeDays;
        this.isInQuarantine = false;
        this.isMovingToPlace = false;
    }

    /**
     * Moves the person to a new position.
     *
     * @param newX the new x-coordinate
     * @param newY the new y-coordinate
     */
    public void move(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }
    public void updateDelX() {
        this.delX = - this.getDelX();
    }
    public void updateDelY() {
        this.delY = - this.getDelY();
    }
    public void changeStatusToSusceptible() {
        this.status = PersonStatus.Susceptible;
    }

    /**
     * Changes the status of the person to infectious and records the day of infection.
     *
     * @param day the day the person became infectious
     */
    public void changeStatusToInfectious(int day) {
        this.status = PersonStatus.Infectious;
        this.receivingInfectionDay = day; // Remembering the day when status is changed to INFECTIOUS
        startRecoveryProcess();
    }

    public int getReceivingInfectionDay() {
        return this.receivingInfectionDay;
    }

    public void moveToQuarantine() {
        this.isInQuarantine = true;
    } // Set when moved into quarantine

    public boolean getQuarantineStatus() {
        return this.isInQuarantine;
    }

    public void moveToHub() {
        this.isMovingToPlace = true;
    } // Set when started moving to hub

    public void stopMoving() {
        this.isMovingToPlace = false;
    } // Set when is on initial position

    public boolean getMovingStatus() {
        return this.isMovingToPlace;
    }
    public void changeRecoveryTime(int newPeriod) {
        this.infectiousTimeDays = newPeriod;
    }

    private void startRecoveryProcess() {
        Thread recoveryThread = new Thread(() -> {
            try {
                Thread.sleep(this.infectiousTimeDays* 1000L); // Sleep for 10 seconds to simulate recovery time
                this.changeStatusToRecovered();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Properly handle thread interruption
            }
        });
        recoveryThread.start();
    }

    public void changeStatusToRecovered() {
        this.status = PersonStatus.Recovered;
    }
    public PersonStatus getStatus() {
        return status;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getDelX() {
        return this.delX;
    }

    public double getDelY() {
        return this.delY;
    }
}
