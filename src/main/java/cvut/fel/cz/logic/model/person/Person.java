package cvut.fel.cz.logic.model.person;

public class Person {
    private PersonStatus status;
    private double x;
    private double y;
    private double delX;
    private double delY;
    /* можно радиус в конфиг добавить */
    private final double radius;

    public Person(double x, double y, double delX, double delY, double radius) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.delX = delX;
        this.delY = delY;
        this.status = PersonStatus.Susceptible;
    }

    public Person(double x, double y, double delX, double delY) {
        this.radius = PersonConfig.radius;
        this.x = x;
        this.y = y;
        this.delX = delX;
        this.delY = delY;
        this.status = PersonStatus.Susceptible;
    }


//    method moves
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
