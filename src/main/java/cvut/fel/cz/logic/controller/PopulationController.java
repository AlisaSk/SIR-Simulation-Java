package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PopulationController implements PopulationControllerInterface{
    Random random = new Random();
    private final int circleSize;
    private final int populationQuantity;
    private final Population population;
    private final PublicPlaces publicPlaces;
    private final double transmissionProb;
    private final int infectiousTimeDays;
    private final double infectionRadius;
    public PopulationController(Population population, PublicPlaces publicPlaces, int populationQuantity, int transmissionProbPercentage, int infectiousTimeDays, double infectionRadius) {
        this.population = population;
        this.circleSize = this.countCircleSize(populationQuantity);
        this.populationQuantity = populationQuantity;
        this.transmissionProb = (double) transmissionProbPercentage / 100;
        this.infectiousTimeDays = infectiousTimeDays;
        this.infectionRadius = infectionRadius;
        //**********
        this.publicPlaces =publicPlaces;
    }

    public PopulationController(Population population, PublicPlaces publicPlaces, int populationQuantity, int transmissionProbPercentage, double infectionRadius) {
        this.population = population;
        this.circleSize = this.countCircleSize(populationQuantity);
        this.populationQuantity = populationQuantity;
        this.transmissionProb = (double) transmissionProbPercentage / 100;
        this.infectiousTimeDays = 7;
        this.infectionRadius = infectionRadius;
        //**********
        this.publicPlaces =publicPlaces;
    }

    @Override
    public Population createPopulation() {
        for (int i = 0; i < this.populationQuantity; i++) {
            Person person = this.createPerson();
            if (i == 0) {
                person.changeStatusToInfectious();
            }
            this.population.addPerson(person);
        }
        return this.population;
    }

    @Override
    public Person createPerson() {
        int xMin = 400 + this.circleSize;
        int xMax = 400 + 380 - this.circleSize;
        int x = throwRandom(xMin, xMax);
        int yMin = 30 + this.circleSize;
        int yMax = 30 + 380 - this.circleSize;
        int y = throwRandom(yMin, yMax);
        double dx = this.random.nextDouble() * 2 - 1;
        double dy = this.random.nextDouble() * 2 - 1;
        Person person = new Person(x, y, dx, dy, this.infectiousTimeDays);
        return person;
    }

    @Override
    public int throwRandom(int coordMin, int coordMax) {
        int x = this.random.nextInt(coordMax - coordMin) + coordMin;
        return x;
    }

    public boolean moveToPublicPlace(Person person) {
        if (this.random.nextDouble() < 0.001 && !person.getVisitHubStatus() && this.publicPlaces.getPlacesCapacity() < this.publicPlaces.getInitialCapacity()) {
            person.moveToHub();
            this.publicPlaces.incrementPlaceCapacity();
            //moveToPositionAndBack(person, 590.0, 210.0, 0.3);
            return true;
        }
        return false;
    }

    @Override
    public void movePeople() {
        int counterHubsVisits = 0;
        for (int i = 0; i < this.population.getQuantity(); i++) {
            Person currentPerson = this.population.getPerson(i);

            if (currentPerson.getMovingStatus()) {
                continue;
            }
//
//            if (moveToPublicPlace(currentPerson)) {
//                counterHubsVisits++;
//                System.out.println("CounterVisits: " + counterHubsVisits);
//                continue;
//            };

            double newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            double newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();

            boolean isValidX = newX > 400 + circleSize && newX < 400 + 380 - circleSize;
            boolean isValidY = newY > 30 + circleSize && newY < 30 + 380 - circleSize;

            if (!isValidX) {
                currentPerson.updateDelX();
                newX = currentPerson.getX() + currentPerson.getDelX()*this.random.nextDouble();
            }
            if (!isValidY) {
                currentPerson.updateDelY();
                newY = currentPerson.getY() + currentPerson.getDelY()*this.random.nextDouble();
            }

            currentPerson.move(newX, newY);

            if (currentPerson.getStatus() == PersonStatus.Infectious) {
                this.addNewInfectious(i, newX, newY);
            }
        }

    }
    private void moveToPositionAndBack(Person person, double targetX, double targetY, double durationSeconds) {
        double startX = person.getX();
        double startY = person.getY();
        double halfDuration = durationSeconds / 2;
        double interval = 0.016; // 60 FPS
        int stepsToTarget = (int) (halfDuration / interval);
        int stepsBack = (int) (halfDuration / interval);

        Timeline timeline = new Timeline();

        // Move to the target position
        for (int i = 0; i < stepsToTarget; i++) {
            double progress = (double) i / stepsToTarget;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(interval * i), event -> {
                person.move(startX + (targetX - startX) * progress, startY + (targetY - startY) * progress);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        // Add a pause transition for 1 second at the target position
        PauseTransition pause = new PauseTransition(Duration.seconds(0.75));
        pause.setOnFinished(event -> {
            Timeline timelineBack = new Timeline();

            // Move back to the start position
            for (int i = 0; i < stepsBack; i++) {
                double progress = (double) i / stepsBack;
                KeyFrame keyFrameBack = new KeyFrame(Duration.seconds(interval * i), e -> {
                    person.move(targetX + (startX - targetX) * progress, targetY + (startY - targetY) * progress);
                });
                timelineBack.getKeyFrames().add(keyFrameBack);
            }

            timelineBack.setOnFinished(e -> {
                person.stopMoving(); // Call stopMoving() when the timeline is finished
            });

            timelineBack.play();
        });

        timeline.setOnFinished(event -> pause.play());
        timeline.play();
    }



    private void addNewInfectious(int personI, double infectedX, double infectedY) {
        for (int i = 0; i < this.population.getQuantity(); i++) {
            if (i == personI) {
                continue;
            }
            Person personS = this.population.getPerson(i);
            if (personS.getStatus() == PersonStatus.Susceptible) {
                double suscepX = personS.getX();
                double suscepY = personS.getY();

                double distance = Math.sqrt(Math.pow(infectedX - suscepX, 2) + Math.pow(infectedY - suscepY, 2));

                if (distance > this.infectionRadius * this.circleSize) {
                    continue;
                }

                if (this.random.nextDouble() <= transmissionProb) {
                    personS.changeStatusToInfectious();
                }
            }
        }
    }

    @Override
    public int countCircleSize(int N) {
        int largeSize = 25;
        int bigSize = 15;
        int mediumSize = 10;
        int smallSize = 4;
        int tinySize = 2;

        if (N <= 20) {
            return largeSize;
        }
        if (N <= 65) {
            return bigSize;
        }
        if (N <= 150) {
            return  mediumSize;
        }
        if (N <= 400) {
            return smallSize;
        }

        return tinySize;
    }

}
