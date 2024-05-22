package cvut.fel.cz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {
    /**
     * Start application
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Loading start page
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