package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.population.Population;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParametersPageView {
    private AnchorPane layout;
    private Map<String, Control> inputFields = new HashMap<>();

    public Scene start() {
        Button startButton = this.createStartButton();
        this.layout = new AnchorPane(startButton);
        Scene scene = new Scene(layout, 800, 500);
        this.addTitle();
        this.addParameters();
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

    private void getSimulationName(){

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
                "Simulation name*", "People quantity in the population*",
                "Probability of the infection transmission (%)", "Time of the infectious period",
                "Infection radius", "Number of quarantine zones",
                "Capacity of quarantine zones", "Number of public places"
        };

        int currentY = 110;
        for (String label : labels) {
            Text textParameter = new Text(label + ": ");
            textParameter.getStyleClass().add("list-text");

            HBox hbox = new HBox(15);
            hbox.setLayoutX(35);
            hbox.setLayoutY(currentY);
            currentY += 32;

            if (Objects.equals(label, labels[4])) {
                ChoiceBox<String> choiceBox = setChoiceBox();
                hbox.getChildren().addAll(textParameter, choiceBox);
                HBox.setMargin(choiceBox, new Insets(-10, 0, 0, 0));
                this.inputFields.put(label, choiceBox);
            } else {
                TextField textField = new TextField();
                textField.setAlignment(Pos.CENTER);
                this.setFieldStyles(labels, label, textField);
                hbox.getChildren().addAll(textParameter, textField);
                HBox.setMargin(textField, new Insets(-10, 0, 0, 0));
                inputFields.put(label, textField);
            }

            this.layout.getChildren().add(hbox);
        }
    }

    public void setFieldStyles(String[] labels, String currentLabel, TextField textField) {
        if (Objects.equals(currentLabel, labels[0]) || Objects.equals(currentLabel, labels[1])) {
            textField.getStyleClass().add("text-field-mandatory");
        }
        else {
            textField.getStyleClass().add("text-field-optional");
        }
        textField.getStyleClass().add("text-field");
    }

    public ChoiceBox<String> setChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Small", "Medium", "Large");
        choiceBox.getStyleClass().add("choice-box");
        choiceBox.setValue("Medium");
        return choiceBox;
    }

}
