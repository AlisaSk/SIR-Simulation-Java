package cvut.fel.cz.logic.model.person;

public class Person {
    private PersonStatus status;
    private double x;
    private double y;
    /* можно радиус в конфиг добавить */
    private final double radius;

    public Person(double x, double y, double radius) {
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public Person(double x, double y) {
        this.radius = PersonConfig.radius;
        this.x = x;
        this.y = y;
    }


//    method moves
    public void move(double newX, double newY) {
        this.x = newX;
        this.y = newY;
    }

    public void changeStatusToSusceptible() {
        this.status = PersonStatus.Susceptible;
    }

    public void changeStatusToInfectious() {
        this.status = PersonStatus.Infectious;
    }

    public void changeStatusToRecovered() {
        this.status = PersonStatus.Recovered;
    }
    public PersonStatus getStatus() {
        return status;
    }

    /*
    * спросить у шереди что лучше: сделать паблик поля или геттеры для переменных
    * */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
