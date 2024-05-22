package cvut.fel.cz.UI.view;

import cvut.fel.cz.logic.controller.PopulationController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersPageViewTest  {

    @BeforeAll
    public static void setUp() {
        Platform.startup(() -> {});
    }

    @AfterAll
    public static void tearDown() {
        Platform.exit();
    }

    @Test
    public void testStartScene() {
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        assertNotNull(scene);
    }

    @Test
    public void testStartButton() {
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        Button startButton = (Button) scene.lookup(".start-button");

        assertNotNull(startButton);

        assertEquals("START", startButton.getText());
        assertEquals(600.0, startButton.getLayoutX());
        assertEquals(400.0, startButton.getLayoutY());
        assertEquals(65.0, startButton.getPrefHeight());
        assertEquals(140.0, startButton.getPrefWidth());
    }

    @Test
    public void testTextFields() {
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        TextField textField = (TextField) scene.lookup(".text-field");

        assertNotNull(textField);
    }

    @Test
    void testChoiceBox() {
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        ChoiceBox<String> choiceBox= (ChoiceBox<String>) scene.lookup(".choice-box");
        List<String> choiceBoxItems = choiceBox.getItems();

        assertNotNull(choiceBoxItems);
        List<String> expectedItems = Arrays.asList("Small", "Medium", "Large");
        assertEquals(choiceBoxItems, expectedItems);
    }

    @Test
    public void testPrametersText() {
        int expectSize = 7;
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        AnchorPane layout = (AnchorPane) scene.getRoot();
        List<HBox> hboxes = new ArrayList<>();
        for (Node node : layout.getChildren()) {
            if (node instanceof HBox) {
                hboxes.add((HBox) node);
            }
        }

        assertNotNull(hboxes);
        assertEquals(expectSize, hboxes.size());


        List<Text> texts= new ArrayList<>();
        for (HBox box: hboxes) {
            for (Node node : box.getChildren()) {
                if (node instanceof Text) {
                    texts.add((Text) node);
                }
            }
        }

        List<String> textContents = new ArrayList<>();
        for (Text text : texts) {
            String textContent = text.getText();
            textContents.add(textContent);
        }

        assertNotNull(texts);
        List<String> expectedItems = Arrays.asList(
                "Simulation name*: ", "People quantity in the population*: ",
                "Probability of the infection transmission (%)*: ", "Time of the infectious period (days): ",
                "Infection radius: ",
                "Capacity of a quarantine zone: ", "Capacity of a public place: "
        );

        assertEquals(expectedItems, textContents);
    }

    @Test
    public void testPrametersFields() {
        int expectSize = 7;
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene scene = parametersPageView.start();

        AnchorPane layout = (AnchorPane) scene.getRoot();
        List<HBox> hboxes = new ArrayList<>();
        for (Node node : layout.getChildren()) {
            if (node instanceof HBox) {
                hboxes.add((HBox) node);
            }
        }

        assertNotNull(hboxes);
        assertEquals(expectSize, hboxes.size());


        List<TextField> textFields= new ArrayList<>();
        for (HBox box: hboxes) {
            for (Node node : box.getChildren()) {
                if (node instanceof TextField) {
                    textFields.add((TextField) node);
                }
            }
        }

        List<String> expectedItems = Arrays.asList(
                "TEST NAME", "1500",
                "100", "10",
                "10", "10"
        );

        for (int i = 0; i < textFields.size(); i++) {
            String txt = expectedItems.get(i);
            textFields.get(i).setText(txt);
        }

        List<String> fieldsContents = new ArrayList<>();
        for (TextField textField : textFields) {
            fieldsContents.add(textField.getText());
        }

        assertNotNull(fieldsContents);

        assertEquals(expectedItems, fieldsContents);
    }
}
