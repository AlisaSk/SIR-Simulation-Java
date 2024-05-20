package cvut.fel.cz.logic.controller;

import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.person.Person;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PublicPlaceController {
    private Random random;
    private PublicPlaces publicPlace;
    public PublicPlaceController(PublicPlaces publicPlace) {
        this.publicPlace = publicPlace;
    }

    private void moveToPublicPlace(Person person) {
        if (this.random.nextDouble() < 0.02 && !person.getVisitHubStatus() && this.publicPlace.getPlacesCapacity() < 12) {
            person.moveToHub();
            moveToPositionAndBack(person, 590.0, 210.0, 0.2);
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

        // Move back to the start position
        for (int i = 0; i < stepsBack; i++) {
            double progress = (double) i / stepsBack;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(halfDuration + interval * i), event -> {
                person.move(startX + (targetX - startX) * progress, startY + (targetY - startY) * progress);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

        timeline.play();
    }

}
