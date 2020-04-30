package Util;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public final class SceneController {
    public static void changeSceneTo(String path, Stage currentStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(path));
        currentStage.setScene(new Scene(loader.load()));
        currentStage.show();
    }

    public static Stage getCurrentStageOn(Event event) {
        Node temp = (Node) event.getSource();
        return (Stage) temp.getScene().getWindow();
    }

    public static void createModalWindow(String title, String path, Event event) throws IOException {
        Stage parent = getCurrentStageOn(event);
        Stage modalStage = new Stage();
        FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(path));

        modalStage.setScene(new Scene(loader.load()));
        modalStage.setTitle(title);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(parent);
        modalStage.showAndWait();
    }

}
