package cvut.fel.cz.UI.view;

import com.google.gson.reflect.TypeToken;
import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.hubs.QuarantineZones;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class SimulationPageView {
    private final PopulationController populationController;
    private final StatisticsController statisticsController;
    private final Population population;
    private final Graph graph;
    private final PublicPlaces publicPlace;
    private final QuarantineZones quarantineZone;
    private long lastUpdate = 0;
    AnchorPane layout;
    StackedAreaChart<Number, Number> diagram;
    private Text sText, iText, rText, dayText, hubText, qText;

    public SimulationPageView(PopulationController populationController, StatisticsController statisticsController) {
        this.populationController = populationController;
        this.statisticsController = statisticsController;
        this.population = this.populationController.createPopulation();// instance of population
        this.graph = this.statisticsController.initGraph(); // instance of graph
        this.publicPlace = this.populationController.getPublicPlaces();
        this.quarantineZone = this.populationController.getQuarantineZone();
    }

    public Scene start() {
        this.layout = this.createSimulationWindow();
        Scene scene = new Scene(layout, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/cvut/fel/cz/simulationPageStyles.css").toExternalForm());
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
        Button endButton = createEndButton();
        Rectangle populationBoard = this.setPopulationBoard();
        this.layout.getChildren().addAll(populationBoard, endButton);
        if (this.publicPlace != null) {
            Rectangle publicPlaceBoard = this.createPublicPlace();
            this.layout.getChildren().add(publicPlaceBoard);
        }
        if (this.quarantineZone != null) {
            Rectangle quarantineZoneBoard = this.createQuarantineZone();
            this.layout.getChildren().add(quarantineZoneBoard);
        }
        AnchorPane.setTopAnchor(populationBoard, 30.0);
        AnchorPane.setRightAnchor(populationBoard, 20.0);

        this.initPopulationCircles();
    }

    private Button createEndButton() {
        Button endButton = new Button("End & Save");
        endButton.setFont(Font.font("Courier New", 23));
        endButton.setLayoutX(615);
        endButton.setLayoutY(430);
        endButton.setOnAction(actionEvent -> {
            this.addDataToJSON();
            LoadSavePageView loadSavePageView = new LoadSavePageView();
            Scene loadScene = loadSavePageView.start();
            Node source = (Node) actionEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.setScene(loadScene);
        });

        return endButton;
    }

    private void addDataToJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<ArrayList<Map<String, Object>>>(){}.getType();

        List<Map<String, Object>> existingData;

        // Read the existing JSON data from the file
        try (FileReader reader = new FileReader("src/main/resources/data/datajson.json")) {
            existingData = gson.fromJson(reader, listType);
            if (existingData == null) {
                existingData = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            existingData = new ArrayList<>();
        }
        // Define the data to be written to the JSON file using a Map
        Map<String, Object> newData = new HashMap<>();
        newData.put("name", this.statisticsController.getSimulationName());
        newData.put("population quantity", this.population.getQuantity());
        newData.put("infection probability", this.populationController.getTransmissionProb());
        newData.put("infectious period", this.populationController.getInfectionPeriod());
        newData.put("infection radius", this.populationController.getInfectionRadius());
        if (this.publicPlace != null) {
            newData.put("hub capacity", this.publicPlace.getCapacity());
        }
        if (this.quarantineZone != null) {
            newData.put("quarantine capacity", this.quarantineZone.getCapacity());
        }

        existingData.add(newData);

        // Write the updated data back to the file
        try (FileWriter fileWriter = new FileWriter("src/main/resources/data/datajson.json")) {
            gson.toJson(existingData, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createStatisticsArea() {
        this.diagram = this.createAreaChart();
        layout.getChildren().add(diagram);

        diagram.setPrefSize(400, 300);
        AnchorPane.setTopAnchor(diagram, 20.0);
        AnchorPane.setLeftAnchor(diagram, -10.0);

        this.addTextStatistics();
        this.updateChart();
        this.addActionInterface();
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

    private Rectangle createQuarantineZone() {
        Rectangle quarantineZoneBoard = new Rectangle();

        quarantineZoneBoard.setX(260);
        quarantineZoneBoard.setY(320);

        quarantineZoneBoard.setHeight(90);
        quarantineZoneBoard.setWidth(90);

        quarantineZoneBoard.setStroke(Color.web("#82ffa5"));
        quarantineZoneBoard.setStrokeWidth(2);

        quarantineZoneBoard.setFill(Color.TRANSPARENT);

        return quarantineZoneBoard;
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

    private void moveToQuarantine(Person person, double targetX, double targetY, double durationSeconds) {
        double startX = person.getX();
        double startY = person.getY();
        double interval = 0.016; // 60 FPS
        int steps = (int) (durationSeconds / interval);

        Timeline timeline = new Timeline();

        // Move to the target position
        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / steps;
            double currentX = startX + (targetX - startX) * progress;
            double currentY = startY + (targetY - startY) * progress;

            KeyFrame keyFrame = new KeyFrame(Duration.seconds(interval * i), event -> {
                person.move(currentX, currentY);
            });
            timeline.getKeyFrames().add(keyFrame);
        }

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

    private void addActionInterface() {
        // TODO set styles to buttons
        if (this.publicPlace != null) {
            Button incrementHubButton = new Button("+");
            incrementHubButton.setFont(Font.font("Courier New", 13));
            incrementHubButton.setMinSize(11, 11);
            incrementHubButton.setLayoutX(190);
            incrementHubButton.setLayoutY(423);

            incrementHubButton.setOnAction(actionEvent -> {
                int updatedValue = Math.min(this.publicPlace.getCapacity() + 1, 100);
                this.publicPlace.updateCapacity(updatedValue);
            });
            this.layout.getChildren().add(incrementHubButton);
        }
        if (this.quarantineZone != null) {
            Button incrementQuarantineButton = new Button("+");
            incrementQuarantineButton.setFont(Font.font("Courier New", 13));
            incrementQuarantineButton.setMinSize(11, 11);
            if (this.publicPlace == null) {
                incrementQuarantineButton.setLayoutX(220);
                incrementQuarantineButton.setLayoutY(423);
            } else {
                incrementQuarantineButton.setLayoutX(220);
                incrementQuarantineButton.setLayoutY(453);
            }

            incrementQuarantineButton.setOnAction(actionEvent -> {
                int updatedValue = Math.min(this.quarantineZone.getCapacity() + 1, 300);
                this.quarantineZone.updateCapacity(updatedValue);
            });
            this.layout.getChildren().add(incrementQuarantineButton);
        }


        Text infectiousPeriodText = new Text("Infectious period");
        Font font = Font.font("Courier New", 17);
        Color color = Color.web("#fa8ecf");
        infectiousPeriodText.setFont(font);
        infectiousPeriodText.setFill(color);
        infectiousPeriodText.setX(405);
        infectiousPeriodText.setY(440);
        Slider infectionPeriodSlider = new Slider(1, 30, 1);
        infectionPeriodSlider.setShowTickLabels(true);
        infectionPeriodSlider.setShowTickMarks(true);
        infectionPeriodSlider.setValue(populationController.getInfectionPeriod());
        infectionPeriodSlider.setMajorTickUnit(10);
        infectionPeriodSlider.setBlockIncrement(1);
        infectionPeriodSlider.setLayoutX(420);
        infectionPeriodSlider.setLayoutY(445);
        infectionPeriodSlider.getStyleClass().add("slider");
        infectionPeriodSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int newValueDouble = newValue.intValue();
                populationController.setInfectiousTimeDays(newValueDouble);
            }
        });

        layout.getChildren().addAll(infectionPeriodSlider, infectiousPeriodText);
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
        dayText.setY(325);
        sText.setX(30);
        sText.setY(355);
        iText.setX(30);
        iText.setY(385);
        rText.setX(30);
        rText.setY(415);

        if (this.publicPlace != null) {
            Text hubText = new Text("Hub: ");
            hubText.setFont(font);
            hubText.setX(30);
            hubText.setY(445);
            hubText.setFill(Color.web("#faa805"));
            this.layout.getChildren().add(hubText);
        }

        if (this.quarantineZone != null) {
            Text qText = new Text("Q Zone: ");
            qText.setFont(font);
            if (publicPlace == null) {
                qText.setX(30);
                qText.setY(445);
            } else {
                qText.setX(30);
                qText.setY(475);
            }

            qText.setFill(Color.web("#82ffa5"));
            this.layout.getChildren().add(qText);
        }

        this.layout.getChildren().add(sText);
        this.layout.getChildren().add(iText);
        this.layout.getChildren().add(rText);
        this.layout.getChildren().add(dayText);
    }

    private void updateTextStatistics() {
        if (sText != null) {
            layout.getChildren().removeAll(sText, iText, rText, dayText, hubText, qText);
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
        dayText.setY(325);
        sText.setX(188);
        sText.setY(355);
        iText.setX(170);
        iText.setY(385);
        rText.setX(165);
        rText.setY(415);

        if (this.publicPlace != null) {
            int hubOccupancy = this.publicPlace.getOccupancy();
            int hubCapacity = this.publicPlace.getCapacity();
            this.hubText = new Text(hubOccupancy + "/" + hubCapacity);
            hubText.setFont(font);
            hubText.setFill(Color.web("#faa805"));
            hubText.setX(90);
            hubText.setY(445);
            this.layout.getChildren().add(hubText);
        }

        if (this.quarantineZone != null) {
            int qOccupancy = this.quarantineZone.getOccupancy();
            int qCapacity = this.quarantineZone.getCapacity();
            this.qText = new Text(qOccupancy + "/" + qCapacity);
            qText.setFont(font);
            qText.setFill(Color.web("#82ffa5"));
            if (publicPlace == null) {
                qText.setX(120);
                qText.setY(445);
            } else {
                qText.setX(120);
                qText.setY(475);
            }

            this.layout.getChildren().add(qText);
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
        populationController.movePeople(this.graph.getLastDayNum());
        int index = 0;
        for (Node node : this.layout.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                Person currentPerson = population.getPerson(index++);

                if (this.publicPlace != null && !currentPerson.getQuarantineStatus() && populationController.moveToPublicPlace(currentPerson)) {
                    moveToPositionAndBack(currentPerson, 590, 220, 0.5); // move to public place
                }

                // if the person is infected already for 3 days, he will be sent to quarantine zone
                if (this.quarantineZone != null && !currentPerson.getMovingStatus() && populationController.moveToQuarantineZone(currentPerson, this.graph.getLastDayNum())) {
                    moveToQuarantine(currentPerson, 305, 365, 0.25);
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
        int tick = (int) Math.round(populationQuantity*0.1);
        NumberAxis yAxis = new NumberAxis(1, populationQuantity, tick);
        yAxis.setLabel("Population");

        StackedAreaChart<Number, Number> areaChart = new StackedAreaChart<>(xAxis, yAxis);
        areaChart.setTitle(this.statisticsController.getSimulationName());
        areaChart.getStylesheets().add(getClass().getResource("/cvut/fel/cz/simulationPageStyles.css").toExternalForm());

        XYChart.Series<Number, Number> susceptibleSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> infectedSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> recoveredSeries = new XYChart.Series<>();

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
