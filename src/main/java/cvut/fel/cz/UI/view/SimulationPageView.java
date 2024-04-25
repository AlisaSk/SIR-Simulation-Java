package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.stream.Collectors;

public class SimulationPageView {
    private final PopulationController populationController;
    private Population population;

    private Rectangle populationBoard;
    public SimulationPageView(PopulationController populationController) {
        this.populationController = populationController;
        this.population = this.populationController.createPopulation(100);
    }

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
        this.populationBoard = this.showPopulationMoves();
        layout.getChildren().add(this.populationBoard);
        this.initPopulation(layout);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCircles(layout);
            }
        };
        timer.start();
        Scene scene = new Scene(layout, 800, 500);
        return scene;
    }

    private Rectangle showPopulationMoves() {
        Rectangle populationBoard = new Rectangle();

        populationBoard.setHeight(380);
        populationBoard.setWidth(380);

        populationBoard.setX(400);
        populationBoard.setY(30);

        populationBoard.setStroke(Color.WHITE);

        // Установка толщины оконтовки
        populationBoard.setStrokeWidth(5);
        populationBoard.setFill(Color.TRANSPARENT);

        return populationBoard;
    }

    public void initPopulation(AnchorPane layout) {
        // N должна передаваться ИЗ ВЬЮ (задает пользователь, но пока так)
        for (int i = 0; i < 100; i++) {
            int x = population.getPerson(i).getX();
            int y = population.getPerson(i).getY();
            Circle circle = new Circle(x, y, 3, Color.WHITE);
            layout.getChildren().add(circle);
        }
    }

    public void updateCircles(AnchorPane layout){
        populationController.movePeople();
        // TODO !!!!!!!!!!!!!!!!!!!!!
        List<Node> circlesToRemove = layout.getChildren().stream()
                .filter(node -> node instanceof Circle)
                .collect(Collectors.toList());

        // Remove all collected circles from the layout
        layout.getChildren().removeAll(circlesToRemove);
        this.initPopulation(layout);

    }
}
