package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.person.Person;
import cvut.fel.cz.logic.model.person.PersonStatus;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    private Text sText, iText, rText, dayText;


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
        // вынести лайаут в поле класса
        AnchorPane layout = new AnchorPane();
        Image image = new Image("file:src/main/resources/cvut/fel/cz/background_1.jpg");
//        BackgroundImage backgroundImage = new BackgroundImage(
//                image,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT
//        );
//
//        layout.setBackground(new Background(backgroundImage));
        layout.setStyle("-fx-background-color: #232324;");
        this.drawLines(layout);

        Rectangle populationBoard = this.setPopulationBoard();
        layout.getChildren().add(populationBoard);

        StackedAreaChart diagram = this.createAreaChart();
        layout.getChildren().add(diagram);

        diagram.setPrefSize(400, 300); // Preferred size
        AnchorPane.setTopAnchor(diagram, 20.0);
        AnchorPane.setLeftAnchor(diagram, -10.0);

        this.initPopulation(layout);
        this.addTextStatistics(layout);
        this.updateChart(diagram);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateCircles(layout);
                if (now - lastUpdate >= 1_000_000_000L) { // 1 second in nanoseconds
                    updateChart(diagram);
                    updateTextStatistics(layout);
                    lastUpdate = now;
                }
            }
        };
        timer.start();

        return layout;
    }



    private Rectangle setPopulationBoard() {
        Rectangle populationBoard = new Rectangle();

        populationBoard.setHeight(380);
        populationBoard.setWidth(380);

        populationBoard.setX(400);
        populationBoard.setY(30);

        populationBoard.setStroke(Color.web("#9b9c9e"));
        populationBoard.setStrokeWidth(2);

        populationBoard.setFill(Color.TRANSPARENT);

        return populationBoard;
    }

    private void drawLines(AnchorPane layout) {
        int cellSize = 30; // Размер ячейки сетки
        int width = 800; // Ширина сцены
        int height = 500; // Высота сцены

        // Рисование вертикальных линий
        for (int i = 0; i < width; i += cellSize) {
            Line line = new Line(i, 0, i, height);
            line.setStroke(Color.rgb(155, 156, 158, 0.3)); // Цвет линий
            layout.getChildren().add(line);
        }

        // Рисование горизонтальных линий
        for (int i = 0; i < height; i += cellSize) {
            Line line = new Line(0, i, width, i);
            line.setStroke(Color.rgb(155, 156, 158, 0.3));
            layout.getChildren().add(line);
        }

    }

    private void addTextStatistics(AnchorPane layout) {
        Text sText = new Text("Susceptible: ");
        Text iText = new Text("Infectious: ");
        Text rText = new Text("Recovered: ");
        Text dayText = new Text("Day: ");

        Font font = Font.font("Times New Roman", 20);
        Color color = Color.web("#9b9c9e");

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

        layout.getChildren().add(sText);
        layout.getChildren().add(iText);
        layout.getChildren().add(rText);
        layout.getChildren().add(dayText);
    }

    private void updateTextStatistics(AnchorPane layout) {
        if (sText != null) {
            layout.getChildren().removeAll(sText, iText, rText, dayText);
        }
        int sCount = this.graph.getLastDay().getDaySusceptible();
        int iCount = this.graph.getLastDay().getDayInfected();
        int rCount = this.graph.getLastDay().getDayRecovered();
        int dayCount = this.graph.getLastDayNum();

        this.dayText = new Text(String.valueOf(dayCount));
        this.sText = new Text(String.valueOf(sCount));
        this.iText = new Text(String.valueOf(iCount));
        this.rText = new Text(String.valueOf(rCount));

        Font font = Font.font("Times New Roman", 20);
        Color color = Color.web("#9b9c9e");

        dayText.setFont(font);
        sText.setFont(font);
        iText.setFont(font);
        rText.setFont(font);

        dayText.setFill(color);
        sText.setFill(color);
        iText.setFill(color);
        rText.setFill(color);

        dayText.setX(90);  // Координата X текста
        dayText.setY(355);
        sText.setX(140);  // Координата X текста
        sText.setY(385);  // Координата Y текста
        iText.setX(130);  // Координата X текста
        iText.setY(415);  // Координата Y текста
        rText.setX(135);  // Координата X текста
        rText.setY(445);  // Координата Y текста

        layout.getChildren().add(sText);
        layout.getChildren().add(iText);
        layout.getChildren().add(rText);
        layout.getChildren().add(dayText);
    }

    private void initPopulation(AnchorPane layout) {
        int circleSize = populationController.countCircleSize(this.N);
        for (int i = 0; i < this.N; i++) {
            Person currentPerson = population.getPerson(i);
            double x = currentPerson.getX();
            double y = currentPerson.getY();

            Color personColor = getColorByStatus(currentPerson.getStatus());

            Circle circle = new Circle(x, y, circleSize, personColor);
            layout.getChildren().add(circle);
        }
    }

    private void updateCircles(AnchorPane layout) {
        populationController.movePeople();
        int index = 0;

        for (Node node : layout.getChildren()) {
            if (node instanceof Circle) {
                Circle circle = (Circle) node;
                Person currentPerson = population.getPerson(index++);

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

    private void updateChart(StackedAreaChart<Number, Number> areaChart) {
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

        NumberAxis xAxis = (NumberAxis) areaChart.getXAxis();
        xAxis.setUpperBound(currentDay + 1);

        if (currentDay < 10) {
            xAxis.setTickUnit(1);
        } else if (currentDay < 25) {
            xAxis.setTickUnit(2);
        } else if (currentDay < 50) {
            xAxis.setTickUnit(5);
        } else {
            xAxis.setTickUnit(10);
        }
    }

    private StackedAreaChart<Number, Number> createAreaChart() {
        // Creating X-Axis for representing day's flow
        NumberAxis xAxis = new NumberAxis(1, 5, 1);
        xAxis.setLabel("Days");

        // Y-Axis for population
        NumberAxis yAxis = new NumberAxis(1, this.N, this.N*0.1);
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
