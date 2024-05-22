package cvut.fel.cz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main class serves as the entry point for the JavaFX application, starting the SIR simulation.
 */
public class Main extends Application {
    /**
     * Starts application
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Initialize and load the start page of the application.
     *
     * @param stage The primary stage of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("start_page"), 800, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();

    }

}