package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static java.lang.Thread.sleep;

public class SimulationPageView {
    private final PopulationController populationController;
    private final StatisticsController statisticsController;
    private final Population population;
    private final Graph graph;
    private final PublicPlaces publicPlace;
    private long lastUpdate = 0;
    AnchorPane layout;
    StackedAreaChart<Number, Number> diagram;
    private Timeline moveToPublicPlaceTimeline;
    private Text sText, iText, rText, dayText, hubText;

    public SimulationPageView(PopulationController populationController, StatisticsController statisticsController) {
        this.populationController = populationController;
        this.statisticsController = statisticsController;
        this.population = this.populationController.createPopulation();// instance of population
        this.graph = this.statisticsController.initGraph(); // instance of graph
        this.publicPlace = this.populationController.getPublicPlaces();
    }

    public Scene start() {
        this.layout = this.createSimulationWindow();
        Scene scene = new Scene(layout, 800, 500);
        return scene;
    }

    public AnchorPane createSimulationWindow() {
        this.layout = new AnchorPane();
        layout.setStyle("-fx-background-color: #232324;");
        this.drawLines();

        // creating simulation area
        this.createSimulationArea();

        // creating statistics area
        this.createStatisticsArea();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCircles();
                if (now - lastUpdate >= 1_000_000_000L) { // 1 second in nanoseconds
                    updateChart();
                    updateTextStatistics();
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        return layout;
    }



    private void createSimulationArea() {
        Rectangle populationBoard = this.setPopulationBoard();
        this.layout.getChildren().add(populationBoard);
        if (this.publicPlace != null) {
            Rectangle publicPlaceBoard = this.createPublicPlace();
            this.layout.getChildren().add(publicPlaceBoard);
        }
        AnchorPane.setTopAnchor(populationBoard, 30.0);
        AnchorPane.setRightAnchor(populationBoard, 20.0);

        this.initPopulationCircles();
    }

    private void createStatisticsArea() {
        this.diagram = this.createAreaChart();
        layout.getChildren().add(diagram);

        diagram.setPrefSize(400, 300);
        AnchorPane.setTopAnchor(diagram, 20.0);
        AnchorPane.setLeftAnchor(diagram, -10.0);

        this.addTextStatistics();
        this.updateChart();
    }

    private Rectangle setPopulationBoard() {
        Rectangle populationBoard = new Rectangle();

        populationBoard.setHeight(380);
        populationBoard.setWidth(380);

        populationBoard.setStroke(Color.web("#9b9c9e"));
        populationBoard.setStrokeWidth(2);

        populationBoard.setFill(Color.TRANSPARENT);

        return populationBoard;
    }

    private Rectangle createPublicPlace() {
        Rectangle publicPlaceBoard = new Rectangle();

        int circleSize = this.populationController.countCircleSize(this.population.getQuantity());

        int placeX = 400 + 190 - circleSize;
        int placeY = 30 + 190 - circleSize;

        publicPlaceBoard.setX(placeX);
        publicPlaceBoard.setY(placeY);

        int height = Math.max(circleSize * 2, 5);
        int width = Math.max(circleSize * 2, 5);

        publicPlaceBoard.setHeight(height);
        publicPlaceBoard.setWidth(width);

        publicPlaceBoard.setStroke(Color.web("#faa805"));
        publicPlaceBoard.setStrokeWidth(2);

        publicPlaceBoard.setFill(Color.TRANSPARENT);

        return publicPlaceBoard;
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

        KeyFrame finalKeyFrameToTarget = new KeyFrame(Duration.seconds(halfDuration), event -> {
            person.move(targetX, targetY);
        });
        timeline.getKeyFrames().add(finalKeyFrameToTarget);

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
                this.publicPlace.decreasePlaceCapacity();
            });

            timelineBack.play();
        });

        timeline.setOnFinished(event -> pause.play());
        timeline.play();
    }

    private void drawLines() {
        int cellSize = 30;
        int width = 800;
        int height = 500;


        for (int i = 0; i < width; i += cellSize) {
            Line line = new Line(i, 0, i, height);
            line.setStroke(Color.rgb(155, 156, 158, 0.3)); // Цвет линий
            this.layout.getChildren().add(line);
        }


        for (int i = 0; i < height; i += cellSize) {
            Line line = new Line(0, i, width, i);
            line.setStroke(Color.rgb(155, 156, 158, 0.3));
            this.layout.getChildren().add(line);
        }

    }

    private void addTextStatistics() {
        Text sText = new Text("Susceptible: ");
        Text iText = new Text("Infectious: ");
        Text rText = new Text("Recovered: ");
        Text dayText = new Text("Day: ");

        Font font = Font.font("Courier New", 20);
        Color color = Color.web("#f2f0f0");

        dayText.setFont(font);
        sText.setFont(font);
        iText.setFont(font);
        rText.setFont(font);

        dayText.setFill(color);
        sText.setFill(color);
        iText.setFill(color);
        rText.setFill(color);

        dayText.setX(30);
        dayText.setY(355);
        sText.setX(30);
        sText.setY(385);
        iText.setX(30);
        iText.setY(415);
        rText.setX(30);
        rText.setY(445);

        if (this.publicPlace != null) {
            Text hubText = new Text("Hub: ");
            hubText.setFont(font);
            hubText.setX(30);
            hubText.setY(475);
            hubText.setFill(Color.web("#faa805"));
            this.layout.getChildren().add(hubText);
        }

        this.layout.getChildren().add(sText);
        this.layout.getChildren().add(iText);
        this.layout.getChildren().add(rText);
        this.layout.getChildren().add(dayText);
    }

    private void updateTextStatistics() {
        if (sText != null) {
            layout.getChildren().removeAll(sText, iText, rText, dayText, hubText);
        }
        int sCount = this.graph.getLastDay().getDaySusceptible();
        int iCount = this.graph.getLastDay().getDayInfected();
        int rCount = this.graph.getLastDay().getDayRecovered();
        int dayCount = this.graph.getLastDayNum();

        this.dayText = new Text(String.valueOf(dayCount));
        this.sText = new Text(String.valueOf(sCount));
        this.iText = new Text(String.valueOf(iCount));
        this.rText = new Text(String.valueOf(rCount));

        Font font = Font.font("Courier New", 20);
        Color color = Color.web("#f2f0f0");

        dayText.setFont(font);
        sText.setFont(font);
        iText.setFont(font);
        rText.setFont(font);

        dayText.setFill(color);
        sText.setFill(color);
        iText.setFill(color);
        rText.setFill(color);

        dayText.setX(90);
        dayText.setY(355);
        sText.setX(188);
        sText.setY(385);
        iText.setX(170);
        iText.setY(415);
        rText.setX(165);
        rText.setY(445);

        if (this.publicPlace != null) {
            int hubOccupancy = this.publicPlace.getAvailability();
            this.hubText = new Text(String.valueOf(hubOccupancy));
            hubText.setFont(font);
            hubText.setFill(Color.web("#faa805"));
            hubText.setX(90);
            hubText.setY(475);
            this.layout.getChildren().add(hubText);
        }

        this.layout.getChildren().add(sText);
        this.layout.getChildren().add(iText);
        this.layout.getChildren().add(rText);
        this.layout.getChildren().add(dayText);
    }

    private void initPopulationCircles() {
        int populationQuantity = this.population.getQuantity();
        int circleSize = populationController.countCircleSize(populationQuantity);
        for (int i = 0; i < populationQuantity; i++) {
            Person currentPerson = population.getPerson(i);
            double x = currentPerson.getX();
            double y = currentPerson.getY();

            Color personColor = getColorByStatus(currentPerson.getStatus());

            Circle circle = new Circle(x, y, circleSize, personColor);
            this.layout.getChildren().add(circle);
        }
    }

    private void updateCircles() {
        populationController.movePeople();
        int index = 0;
        for (Node node : this.layout.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                Person currentPerson = population.getPerson(index++);

                if (populationController.getPublicPlaces() != null && populationController.moveToPublicPlace(currentPerson)) {
                    moveToPositionAndBack(currentPerson, 590, 220, 0.5); // move to public place
                }

                circle.setCenterX(currentPerson.getX());
                circle.setCenterY(currentPerson.getY());

                Color personColor = getColorByStatus(currentPerson.getStatus());

                circle.setFill(personColor);
            }
        }
    }

    private Color getColorByStatus(PersonStatus status) {
        switch (status) {
            case Susceptible:
                return Color.web("#6098f7", 0.8);
            case Infectious:
                return Color.web("#f7406e", 0.8);
            case Recovered:
                return Color.web("#9e9e9d", 0.8);
            default:
                return Color.web("#fcfcfc", 1.0); // В случае неопределенного статуса
        }
    }

    private StackedAreaChart<Number, Number> createAreaChart() {
        // Creating X-Axis for representing day's flow
        NumberAxis xAxis = new NumberAxis(1, 5, 1);
        xAxis.setLabel("Days");

        int populationQuantity = this.population.getQuantity();
        // Y-Axis for population
        NumberAxis yAxis = new NumberAxis(1, populationQuantity, populationQuantity*0.1);
        yAxis.setLabel("Population");

        // Создаем AreaChart
        StackedAreaChart<Number, Number> areaChart = new StackedAreaChart<>(xAxis, yAxis);
        areaChart.setTitle(this.statisticsController.getSimulationName());
        areaChart.getStylesheets().add(getClass().getResource("/cvut/fel/cz/simulationPageStyles.css").toExternalForm());

        // Создаем серии данных для каждой категории
        XYChart.Series<Number, Number> susceptibleSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> infectedSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> recoveredSeries = new XYChart.Series<>();

        // Добавляем серии в диаграмму
        areaChart.getData().addAll(susceptibleSeries, infectedSeries, recoveredSeries);

        return areaChart;
    }

    private void updateChart() {
        this.statisticsController.updateStatistics();

        XYChart.Series<Number, Number> susceptibleSeries = this.diagram.getData().get(0);
        XYChart.Series<Number, Number> infectiousSeries = this.diagram.getData().get(1);
        XYChart.Series<Number, Number> recoveredSeries = this.diagram.getData().get(2);

        int currentDay = this.graph.getLastDayNum();
        int sCount = this.graph.getLastDay().getDaySusceptible();
        int iCount = this.graph.getLastDay().getDayInfected();
        int rCount = this.graph.getLastDay().getDayRecovered();


        susceptibleSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,sCount));
        infectiousSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,iCount));
        recoveredSeries.getData().add(new XYChart.Data<Number, Number>(currentDay,rCount));

        NumberAxis xAxis = (NumberAxis) this.diagram.getXAxis();
        xAxis.setUpperBound(currentDay + 1);

        if (currentDay < 10) {
            xAxis.setTickUnit(1);
        } else if (currentDay < 25) {
            xAxis.setTickUnit(2);
        } else if (currentDay < 50) {
            xAxis.setTickUnit(5);
        }else if (currentDay < 100) {
            xAxis.setTickUnit(10);
        } else {
            xAxis.setTickUnit(50);
        }
    }
}
