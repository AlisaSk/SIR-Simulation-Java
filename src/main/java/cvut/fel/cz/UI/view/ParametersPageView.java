package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ParametersPageView {
    private AnchorPane layout;
    public Scene start() {
        Button startButton = this.createStartButton();
        this.layout = new AnchorPane(startButton);
        Scene scene = new Scene(layout, 800, 500);
        this.addTitle();
        this.addParameters();
        this.addTextFields();
        scene.getStylesheets().add(getClass().getResource("/cvut/fel/cz/paramPage_styles.css").toExternalForm());
        return scene;
    }

    private Button createStartButton() {
        Button startButton = new Button("START");
        startButton.getStyleClass().add("start-button");
        startButton.setLayoutX(600);
        startButton.setLayoutY(400);
        startButton.setPrefHeight(65);
        startButton.setPrefWidth(140);
        startButton.setOnAction(this::handleButtonAction);
        return startButton;
    }

    private void handleButtonAction(ActionEvent event) {
        Population population = new Population(); // model
        Graph graph = new Graph(population);
        int N = 1250;
        PopulationController populationController = new PopulationController(population, N); // Population controller
        StatisticsController statisticsController = new StatisticsController(graph); // Statistics controller
        SimulationPageView simulationPageView = new SimulationPageView(populationController, statisticsController, N); // Simulation Page view
        Scene simulationScene = simulationPageView.start();

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(simulationScene); // closing previous page
    }

    public void addTitle() {
        Text title = new Text("Set up initial parameters of the Simulation");

        title.getStyleClass().add("title-text");

        title.setX(30);
        title.setY(55);

        this.layout.getChildren().add(title);
    }

    public void addParameters() {
        String[] labels = {
                "Simulation name: ", "People quantity in the population: ",
                "Probability of the infection transmission: ", "Time of the infectious period: ",
                "Infection radius: ", "Number of quarantine zones: ",
                "Capacity of quarantine zones: ", "Number of public places: "
        };
        int startY = 123;
        for (String label : labels) {
            Text text = new Text(label);
            text.getStyleClass().add("list-text");
            text.setX(35);
            text.setY(startY);
            startY += 32;
            this.layout.getChildren().add(text);
        }

    }

    public void addTextFields() {
        // maybe later parameters and their text should be added into 1 box
        TextField textField = new TextField();
        textField.setPromptText("700");
        textField.setAlignment(Pos.CENTER);
        textField.setLayoutX(400);
        textField.setLayoutY(96);
        textField.getStyleClass().add("text-field");
        this.layout.getChildren().add(textField);
    }

}
