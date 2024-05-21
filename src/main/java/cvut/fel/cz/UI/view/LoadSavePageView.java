package cvut.fel.cz.UI.view;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class LoadSavePageView {
    AnchorPane layout;
    private List<String> simulationNames;
    Random random = new Random();
    private ArrayList<String> randomColors;
    public LoadSavePageView() {
    }

    public Scene start() {
        this.layout = new AnchorPane();
        this.createLoadWindow();
        Scene scene = new Scene(this.layout, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/cvut/fel/cz/loadPageStyles.css").toExternalForm());
        return scene;
    }

    private void createLoadWindow() {
        this.setStaticText();
        this.loadSimulationNames();
        this.createSavedSimulations();
        this.createGoBackButton();
    }

    private void buttonAnimation(Button button) {
        button.setOnMousePressed(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });
        button.setOnMouseReleased(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    private void setStaticText() {
        Text title = new Text("Load your previous simulation ;)");
        title.getStyleClass().add("title-text");
        title.setX(30);
        title.setY(55);

        Text clickText1 = new Text("Click on button to load");
        Text clickText2 = new Text("your saved simulation or...");
        clickText1.getStyleClass().add("click-text");
        clickText2.getStyleClass().add("click-text");
        clickText1.setX(370);
        clickText1.setY(370);
        clickText2.setX(480);
        clickText2.setY(400);

        this.layout.getChildren().addAll(title, clickText1, clickText2);
    }

    private void createGoBackButton() {
        Button goBackButton = new Button("GO BACK");
        goBackButton.getStyleClass().add("back-button");
        goBackButton.setLayoutX(510);
        goBackButton.setLayoutY(420);
        layout.getChildren().add(goBackButton);
        this.buttonAnimation(goBackButton);

        goBackButton.setOnAction(actionEvent -> {
            ParametersPageView parametersPageView = new ParametersPageView();
            Scene parametersScene = parametersPageView.start();

            Node source = (Node) actionEvent.getSource();
            Stage currentStage = (Stage) source.getScene().getWindow();
            currentStage.setScene(parametersScene);
        });
    }
    private void createSavedSimulations() {
        randomColors = new ArrayList<>();
        randomColors.add("#aa2fcc");
        randomColors.add("#2dbef7");
        randomColors.add("#f03cfa");
        randomColors.add("#fa5c3c");
        randomColors.add("#5f67f5");

        int startY = 90;
        int lineCounter = 0;
        int length = this.simulationNames.size();
        String[] reversedArray = new String[length];
        for (int i = 0; i < length; i++) {
            reversedArray[length - i - 1] = this.simulationNames.get(i);
        }

        for (String simulationName: reversedArray) {
            // user is able to load only last 5 simulations
            if (lineCounter == 5) {
                break;
            }
            lineCounter++;
            Button simulationButton = new Button(simulationName);
            simulationButton.getStyleClass().add("list-button");
            int colorIndex = random.nextInt(4);
            simulationButton.setStyle("-fx-background-color:" + randomColors.get(colorIndex));
            simulationButton.setLayoutX(50);
            simulationButton.setLayoutY(startY);
            startY+=50;
            this.buttonAnimation(simulationButton);
            this.layout.getChildren().add(simulationButton);

            simulationButton.setOnAction(actionEvent -> {
                this.loadSimulationByName(simulationName);
            });
        }
    }

    private void loadSimulationNames() {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Map<String, Object>>>() {}.getType();
        this.simulationNames = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/resources/data/datajson.json")) {
            List<Map<String, Object>> data = gson.fromJson(reader, listType);
            if (data != null) {
                for (Map<String, Object> simulation : data) {
                    String name = (String) simulation.get("name");
                    simulationNames.add(name);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSimulationByName(String simulationName) {
        String filePath = "src/main/resources/data/datajson.json";

        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);

            JsonArray jsonArray = jsonElement.getAsJsonArray();

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();


                // Access the simulation's name and other properties
                String name = jsonObject.get("name").getAsString();
                if (!Objects.equals(name, simulationName)) {
                    continue;
                }
                int populationQuantity = jsonObject.get("population quantity").getAsInt();
                int infectiousPeriod = jsonObject.get("infectious period").getAsInt();
                double infectionProbability = jsonObject.get("infection probability").getAsDouble();
                double infectionRadius = jsonObject.get("infection radius").getAsDouble();

                if (jsonObject.has("hub capacity")) {
                    int hubCapacity = jsonObject.get("hub capacity").getAsInt();
                }
                if (jsonObject.has("quarantine capacity")) {
                    int quarantineCapacity = jsonObject.get("quarantine capacity").getAsInt();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSimulationPage() {

    }
}
