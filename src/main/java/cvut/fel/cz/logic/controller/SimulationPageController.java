package cvut.fel.cz.logic.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.*;
import javafx.scene.image.Image;


public class SimulationPageController {
    public Scene showSimulationWindow() {
        AnchorPane layout = new AnchorPane();
        Image image = new Image("file:src/main/resources/cvut/fel/cz/background_1.jpg");
        BackgroundImage backgroundImage = new BackgroundImage(
                image,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );

        layout.setBackground(new Background(backgroundImage));
        Rectangle populationRec = this.showPopulationMoves();
        layout.getChildren().add(populationRec);
        Scene scene = new Scene(layout, 800, 500);
        return scene;
    }

    private Rectangle showPopulationMoves() {
        Rectangle populationBoarder = new Rectangle();

        populationBoarder.setHeight(380);
        populationBoarder.setWidth(380);

        populationBoarder.setX(400);
        populationBoarder.setY(30);

        populationBoarder.setStroke(Color.WHITE);

        // Установка толщины оконтовки
        populationBoarder.setStrokeWidth(5);
        populationBoarder.setFill(Color.TRANSPARENT);

        return populationBoarder;
    }
}
