package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class SimulationPageView {
    private final PopulationController populationController;
    private final StatisticsController statisticsController;
    private Population population;
    private Graph graph;
    private long lastUpdate = 0;


    private final int N;

    public SimulationPageView(PopulationController populationController, StatisticsController statisticsController, int N) {
        // after adding parameters page they will be given as the parameters here
        // the parameters: N (populationQuantity), R (radius)
        this.populationController = populationController;
        this.statisticsController = statisticsController;
        this.N = N;
        this.population = this.populationController.createPopulation();// instance of population
        this.graph = this.statisticsController.initGraph(); // instance of graph
    }

    public Scene start() {
        new AnchorPane();
        AnchorPane layout = this.createSimulationWindow();
        Scene scene = new Scene(layout, 800, 500);
        return scene;
    }

    public AnchorPane createSimulationWindow() {
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

        Rectangle populationBoard = this.setPopulationBoard();
        layout.getChildren().add(populationBoard);

        StackedAreaChart diagram = this.createSIRDiagram();
        layout.getChildren().add(diagram);

        diagram.setPrefSize(400, 300); // Preferred size
        AnchorPane.setTopAnchor(diagram, 20.0);
        AnchorPane.setLeftAnchor(diagram, -10.0);

        this.initPopulation(layout);

        this.updateChart(layout, diagram);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCircles(layout);
                if (now - lastUpdate >= 1_000_000_000L) { // 10 seconds in nanoseconds
                    updateChart(layout, diagram);
                    lastUpdate = now;
                }
            }
        };
        timer.start();

//        AnimationTimer timer2 = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                try {
//                    sleep(10000);
//                } catch (InterruptedException e) {
//                    // throw new RuntimeException(e);
//                    updateChart(layout, diagram);
//                }
//                // updateChart(layout, diagram);
//            }
//        };
//        timer2.start();

        return layout;
    }

    private Rectangle setPopulationBoard() {
        Rectangle populationBoard = new Rectangle();

        populationBoard.setHeight(380);
        populationBoard.setWidth(380);

        populationBoard.setX(400);
        populationBoard.setY(30);

        populationBoard.setStroke(Color.WHITE);

        populationBoard.setStrokeWidth(5);
        populationBoard.setFill(Color.TRANSPARENT);

        return populationBoard;
    }

    public void initPopulation(AnchorPane layout) {
        int circleSize = populationController.countCircleSize(this.N);
        for (int i = 0; i < this.N; i++) {
            Person currentPerson = population.getPerson(i);
            double x = currentPerson.getX();
            double y = currentPerson.getY();

            String hex;
            double opacity;

            // set up color for each person
            switch (currentPerson.getStatus()){
                case Susceptible:
                    hex = "#6098f7";
                    opacity = 0.8;
                    break;
                case Infectious:
                    hex = "#f7406e";
                    opacity = 0.8;
                    break;
                case Recovered:
                    hex = "#9e9e9d";
                    opacity = 0.8;
                    break;
                default:
                    hex = "#fcfcfc";
                    opacity = 1;
                    break;
            }

            Color personColor = Color.web(hex, opacity);

            Circle circle = new Circle(x, y, circleSize, personColor);
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

    public void updateChart(AnchorPane layout, StackedAreaChart<Number, Number> areaChart) {
        this.statisticsController.updateStatistics();

        XYChart.Series<Number, Number> susceptibleSeries = areaChart.getData().get(0);
        XYChart.Series<Number, Number> infectiousSeries = areaChart.getData().get(1);
        XYChart.Series<Number, Number> recoveredSeries = areaChart.getData().get(2);

        int currentDay = this.graph.getLastDayNum();
        int sCount = this.graph.getLastDay().getDaySusceptible();
        int iCount = this.graph.getLastDay().getDayInfected();
        int rCount = this.graph.getLastDay().getDayRecovered();


        susceptibleSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,sCount));
        infectiousSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,iCount));
        recoveredSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,rCount));

        // areaChart.getData().addAll(susceptibleSeries,infectiousSeries,recoveredSeries);
    }

    private StackedAreaChart<Number, Number> createSIRDiagram() {
        // Creating X-Axis for representing day's flow
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days");

        // Y-Axis for population
        NumberAxis yAxis = new NumberAxis(1, this.N, 50);
        yAxis.setLabel("Population");

        // Создаем AreaChart
        StackedAreaChart<Number, Number> areaChart = new StackedAreaChart<>(xAxis, yAxis);
        areaChart.getStylesheets().add(getClass().getResource("/cvut/fel/cz/chart_style.css").toExternalForm());

        // Создаем серии данных для каждой категории
         XYChart.Series<Number, Number> susceptibleSeries = new XYChart.Series<>();
         XYChart.Series<Number, Number> infectedSeries = new XYChart.Series<>();
         XYChart.Series<Number, Number> recoveredSeries = new XYChart.Series<>();

        // Добавляем серии в диаграмму
        areaChart.getData().addAll(susceptibleSeries, infectedSeries, recoveredSeries);

        return areaChart;
    }


}
