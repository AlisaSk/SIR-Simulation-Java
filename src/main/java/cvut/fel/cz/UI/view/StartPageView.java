package cvut.fel.cz.UI.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartPageView {

    @FXML
    void start(ActionEvent event){
        // loading parameters page
        ParametersPageView parametersPageView = new ParametersPageView();
        Scene parametersScene = parametersPageView.start();

        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        currentStage.setScene(parametersScene); // closing previous page
    }

}
