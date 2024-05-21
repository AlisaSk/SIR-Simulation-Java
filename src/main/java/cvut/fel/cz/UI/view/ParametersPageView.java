package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import cvut.fel.cz.logic.controller.StatisticsController;
import cvut.fel.cz.logic.model.graph.Graph;
import cvut.fel.cz.logic.model.hubs.PublicPlaces;
import cvut.fel.cz.logic.model.hubs.QuarantineZones;
import cvut.fel.cz.logic.model.population.Population;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ParametersPageView {
    private AnchorPane layout;
    private final Map<String, Control> inputFields = new HashMap<>();
    private final Map<String, Text> labelsText = new HashMap<>();

    public Scene start() {
        Button startButton = this.createStartButton();
        this.layout = new AnchorPane(startButton);
        Scene scene = new Scene(layout, 800, 500);
        this.addTitle();
        this.addParameters();
        scene.getStylesheets().add(getClass().getResource("/cvut/fel/cz/parametersPageStyles.css").toExternalForm());
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
        // button animation when it is pressed
        startButton.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), startButton);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });
        startButton.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), startButton);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        return startButton;
    }

    private void handleButtonAction(ActionEvent event) {
        String simulationName = getSimulationName();
        int populationQuantity = this.getParameterValue("People quantity in the population*", 2, 1500, true);
        int transmissionProb = this.getParameterValue("Probability of the infection transmission (%)*", 1, 100, true);
        int infectiousPeriod = this.getParameterValue("Time of the infectious period (days)", 1, 30, false);
        double infectionRadius = this.getRadius();
        int publicPlaceCapacity = this.getParameterValue("Capacity of a public place", 1, 100, false);
        int quarantineZoneCapacity = this.getParameterValue("Capacity of a quarantine zone", 1, 300, false);

        //checking if fields are filled correctly
        boolean errorCondition = Objects.equals(simulationName, "") || populationQuantity == -1 || transmissionProb == -1 || infectiousPeriod == -1 || publicPlaceCapacity == -1 || quarantineZoneCapacity == -1;
        if (errorCondition) {
            return;
        }

        Scene simulationScene = setSimulationScene(populationQuantity, transmissionProb, simulationName, infectiousPeriod, infectionRadius, publicPlaceCapacity, quarantineZoneCapacity);

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(simulationScene); // closing previous page
    }

    private static Scene setSimulationScene(int populationQuantity, int transmissionProb, String simulationName, int infectiousPeriod, double infectionRadius, int publicPlaceCapacity, int quarantineZoneCapacity) {
        Population population = new Population(); // model
        Graph graph = new Graph(population);
        PublicPlaces publicPlaces = null;
        QuarantineZones quarantineZone = null;

        if (publicPlaceCapacity > 0) {
            publicPlaces = new PublicPlaces(1, publicPlaceCapacity);
        }

        if (quarantineZoneCapacity > 0) {
            quarantineZone = new QuarantineZones(1, quarantineZoneCapacity);
        }

        PopulationController populationController;

        if (infectiousPeriod > 0) {
            if (publicPlaces != null && quarantineZone != null) {
                populationController = new PopulationController(population, publicPlaces, quarantineZone, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
            } else if (publicPlaces != null) {
                populationController = new PopulationController(population, publicPlaces, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
            } else if (quarantineZone != null) {
                populationController = new PopulationController(population, publicPlaces, quarantineZone, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
            } else {
                populationController = new PopulationController(population, populationQuantity, transmissionProb, infectiousPeriod, infectionRadius);
            }
        } else {
            if (publicPlaces != null && quarantineZone != null) {
                populationController = new PopulationController(population, publicPlaces, quarantineZone, populationQuantity, transmissionProb, infectionRadius);
            } else if (publicPlaces != null) {
                populationController = new PopulationController(population, publicPlaces, populationQuantity, transmissionProb, infectionRadius);
            } else if (quarantineZone != null) {
                populationController = new PopulationController(population, publicPlaces, quarantineZone, populationQuantity, transmissionProb, infectionRadius);
            } else {
                populationController = new PopulationController(population, populationQuantity, transmissionProb, infectionRadius);
            }
        }
        StatisticsController statisticsController = new StatisticsController(graph, simulationName); // Statistics controller
        SimulationPageView simulationPageView = new SimulationPageView(populationController, statisticsController); // Simulation Page view
        Scene simulationScene = simulationPageView.start();
        return simulationScene;
    }

    private String getSimulationName(){
        String key = "Simulation name*";
        String simulationName = ((TextField) inputFields.get(key)).getText();
        if (simulationName.isEmpty() || simulationName.length() >= 20 || !simulationName.matches("^[a-zA-Z0-9-_]+$")) {
            this.setErrorStyles(key);
            return "";
        }
        this.setFineStyles(key);
        return simulationName;
    }

    private double getRadius() {
        String key = "Infection radius";
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) inputFields.get(key);
        String radiusString = choiceBox.getValue(); // Retrieves the currently selected value

        switch (radiusString) {
            case "Small": return 1.1;
            case "Large": return 2.5;
            default: return 1.6;
        }
    }

    private void setErrorStyles(String key) {
        labelsText.get(key).getStyleClass().removeAll("list-text");
        labelsText.get(key).getStyleClass().add("error-text");
        inputFields.get(key).getStyleClass().add("text-field-error");
    }

    private void setFineStyles(String key) {
        labelsText.get(key).getStyleClass().removeAll("error-text");
        labelsText.get(key).getStyleClass().add("list-text");
        inputFields.get(key).getStyleClass().removeAll("text-field-error");
        inputFields.get(key).getStyleClass().add("text-field");
    }

    private int getParameterValue(String key, int fromInclusive, int toInclusive, boolean isMandatory) {
        String populationQuantityStr = ((TextField) inputFields.get(key)).getText();
        if (Objects.equals(populationQuantityStr, "") && !isMandatory) {
            this.setFineStyles(key);
            return 0;
        }
        try {
            int populationQuantity = Integer.parseInt(populationQuantityStr);
            if (populationQuantity < fromInclusive || populationQuantity > toInclusive) {
                this.setErrorStyles(key);
                return -1;
            }
            this.setFineStyles(key);
            return populationQuantity;
        } catch (NumberFormatException e) {
            this.setErrorStyles(key);
            return -1;
        }
    }

    private void addTitle() {
        Text title = new Text("Set up initial parameters of the Simulation");

        title.getStyleClass().add("title-text");

        title.setX(30);
        title.setY(55);

        this.layout.getChildren().add(title);
    }

    private void addParameters() {
        String[] labels = {
                "Simulation name*", "People quantity in the population*",
                "Probability of the infection transmission (%)*", "Time of the infectious period (days)",
                "Infection radius",
                "Capacity of a quarantine zone", "Capacity of a public place"
        };

        int currentY = 110;
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            Text textParameter = new Text(label + ": ");
            textParameter.getStyleClass().add("list-text");
            this.labelsText.put(label, textParameter);

            this.createTooltip(textParameter, i);

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

    private void createTooltip (Text label, int index) {
        String[] descriptions = {
                "REQUIRED: Use only latin, <= 20 symbols", "REQUIRED: Required range is <2, 1500>",
                "REQUIRED: Percentage must be in range <1, 100>", "OPTIONAL: Infectious period must be in range <1, 30>. DEFAULT: 7 days",
                "OPTIONAL: Choose the radius :). DEFAULT: medium",
                "OPTIONAL: Quarantine Zone capacity must be in range <1, 200>. DEFAULT: -", "OPTIONAL: Central Hub capacity must be in range <1, 100>. DEFAULT: -"
        };

        Tooltip tooltip = new Tooltip(descriptions[index]);
        // tooltip.setStyle("tooltip") -- this one has too many warnings, probably due to the .setStyle method
        tooltip.setStyle("-fx-background-color: rgba(173, 167, 194, 0.6); -fx-text-fill: #282829;");
        Tooltip.install(label, tooltip);
    }

    private void setFieldStyles(String[] labels, String currentLabel, TextField textField) {
        if (Objects.equals(currentLabel, labels[0]) || Objects.equals(currentLabel, labels[1]) || Objects.equals(currentLabel, labels[2])) {
            textField.getStyleClass().add("text-field-mandatory");
        }
        else {
            textField.getStyleClass().add("text-field-optional");
        }
        textField.getStyleClass().add("text-field");
    }

    private ChoiceBox<String> setChoiceBox() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll("Small", "Medium", "Large");
        choiceBox.getStyleClass().add("choice-box");
        choiceBox.setValue("Medium");
        return choiceBox;
    }

}
