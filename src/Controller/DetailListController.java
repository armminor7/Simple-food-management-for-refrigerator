package Controller;

import Model.Food;
import Util.SceneController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DetailListController implements Initializable {
    @FXML
    private ScrollPane scrollPaneRoot;

    @FXML
    private VBox vboxContainer;

    @FXML
    private FontAwesomeIconView backIcon;

    private RefrigeratorController refrigerator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrigerator = new RefrigeratorController(UserAccountController.getActiveUser());
        vboxContainer.setSpacing(0);
        backIcon.setOnMouseClicked(event -> {
            try {
                SceneController.changeSceneTo("/View/open-refrigerator.fxml", SceneController.getCurrentStageOn(event));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't load open-refrigerator page");
            }
        });
    }

    public void viewDetailCardFrom(String selectCell) {
        backIcon.setVisible(false);
        vboxContainer.getChildren().clear();
        refrigerator.loadFoodData();
        List<Food> tray = new ArrayList<>();
        tray = refrigerator.getFoodListFrom(selectCell);

        for (Food food : tray) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/detailCard.fxml"));
                Pane card = loader.load();
                loader.setRoot(this.scrollPaneRoot);
                DetailCardController detailCard = loader.getController();

                detailCard.setFood(food);
                detailCard.loadViewDetail();
                vboxContainer.getChildren().add(card);

            } catch (IOException e) {
//            e.printStackTrace();
                System.err.println("Can't load detail Card page");
            }
        }
    }

    public void editDetailCardFrom(String selectCell) {
        backIcon.setVisible(true);
        vboxContainer.getChildren().clear();
        refrigerator.loadFoodData();
        List<Food> tray = new ArrayList<>();
        tray = refrigerator.getFoodListFrom(selectCell);

        for (Food food : tray) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/detailCard.fxml"));
                Pane card = loader.load();
                loader.setRoot(this.scrollPaneRoot);
                DetailCardController detailCard = loader.getController();

                detailCard.setFood(food);
                detailCard.setSelectedCell(selectCell);
                detailCard.loadEditDetailCard();
                vboxContainer.getChildren().add(card);
                if (vboxContainer.getChildren().size() > 1) detailCard.setDisableAll(true);

            } catch (IOException e) {
//            e.printStackTrace();
                System.err.println("Can't load detail Card page");
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/detailCard.fxml"));
            Pane card = loader.load();
            loader.setRoot(this.scrollPaneRoot);
            DetailCardController detailCard = loader.getController();

            Food food = tray.get(0);
            detailCard.setFood(food);
            detailCard.setSelectedCell(selectCell);
            detailCard.loadAddCard();
            vboxContainer.getChildren().add(card);
        } catch (IOException e){
            System.err.println("Can't load ADD card");
        }

    }



}
