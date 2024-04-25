package cvut.fel.cz.logic.model.person;

public class Person {
    private PersonStatus status;
    private int x;
    private int y;
    private int delX;
    private int delY;
    /* можно радиус в конфиг добавить */
    private final double radius;

    public Person(int x, int y, int delX, int delY, double radius) {
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.delX = delX;
        this.delY = delY;
    }

    public Person(int x, int y, int delX, int delY) {
        this.radius = PersonConfig.radius;
        this.x = x;
        this.y = y;
        this.delX = delX;
        this.delY = delY;
    }


//    method moves
    public void move(int newX, int newY) {
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
    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getDelX() {
        return this.delX;
    }

    public int getDelY() {
        return this.delY;
    }
}
