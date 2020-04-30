package Controller;

import Model.Food;
import Util.SceneController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class OpenRefrigeController implements Initializable {
    @FXML
    private ImageView bgOpenRefrige;
    @FXML
    private Text closeBtn;
    @FXML
    private Rectangle cell00, cell01, cell10, cell11, cell20, cell21, cell30, cell31;
    @FXML
    private FontAwesomeIconView addIcon00, addIcon01, addIcon10, addIcon11, addIcon20, addIcon21, addIcon30, addIcon31;
    @FXML
    private ImageView imageCell00, imageCell01, imageCell10, imageCell11, imageCell20, imageCell21, imageCell30, imageCell31;

    private RefrigeratorController refrigerator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrigerator = new RefrigeratorController(UserAccountController.getActiveUser());
        initPopupDetailWhenHover();
        initSetInvisibleImageView();
        initHandleAddIcon();
        initFoodImage();
        handleClickOnImageView();

        bgOpenRefrige.setImage(new Image("images/refrigerator-open.png"));
        closeBtn.setOnMouseClicked(event -> {
            try {
                SceneController.changeSceneTo("/View/home.fxml", SceneController.getCurrentStageOn(event));
            } catch (IOException e) {
                System.err.println("Can't Load Home page");
                e.printStackTrace();
            }
        });


    }

    private void initPopupDetailWhenHover() {
        ImageView[] imageViews = {imageCell00, imageCell01, imageCell10, imageCell11, imageCell20, imageCell21, imageCell30, imageCell31};
        String path = "/View/detailList.fxml";
        Stage popUpStage = new Stage();
        popUpStage.initModality(Modality.NONE);
        popUpStage.setResizable(false);
        popUpStage.initStyle(StageStyle.UNDECORATED);
        for (ImageView cell : imageViews) {
            if (cell.isVisible()) {
                cell.setOnMouseEntered(event -> {
                    FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(path));
                    try {
                        Parent root = loader.load();
                        DetailListController detailList = loader.getController();
                        detailList.viewDetailCardFrom(getIdFromHover(event));
                        if (popUpStage.getOwner() == null) popUpStage.initOwner(closeBtn.getScene().getWindow());
                        popUpStage.setScene(new Scene(root));

                        popUpStage.setX(SceneController.getCurrentStageOn(event).getX() - popUpStage.getWidth());
                        popUpStage.setY(SceneController.getCurrentStageOn(event).getY());
                        popUpStage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                cell.setOnMouseExited(event -> {
                    popUpStage.close();
                });
            }
        }
    }

    private void initSetInvisibleImageView() {
        ImageView[] imageViews = {imageCell00, imageCell01, imageCell10, imageCell11, imageCell20, imageCell21, imageCell30, imageCell31};
        for (ImageView cellImage : imageViews) {
            cellImage.setVisible(false);
            cellImage.setCursor(Cursor.HAND);
        }
    }

    private void initHandleAddIcon() {
        FontAwesomeIconView[] iconViews = {addIcon00, addIcon01, addIcon10, addIcon11, addIcon20, addIcon21, addIcon30, addIcon31};
        for (FontAwesomeIconView icon : iconViews) {
            icon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String id = icon.getId();
                    String selectedCell = "CELL" + id.substring(id.length() - 2);

                    String AddFoodPage = "/View/addFood.fxml";
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(AddFoodPage));
                    try {
                        Parent root = (Parent) loader.load();
                        AddFoodController addFoodController = loader.getController();

                        addFoodController.setSelectedCell(selectedCell);
                        Stage stage = SceneController.getCurrentStageOn(event);
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        System.err.println("Can't load AddFood page");
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void initFoodImage() {
        refrigerator.loadFoodData();
        for (Map.Entry<String, List<Food>> entry : refrigerator.getEntrySet()) {
            String cell = entry.getKey();
            List<Food> foods = entry.getValue();
            if (foods.isEmpty()) {
                continue;
            }
            Food food = foods.get(0);
            switch (cell) {
                case "CELL00":
                    imageCell00.setImage(new Image(food.getImagePath()));
                    imageCell00.setVisible(true);
                    addIcon00.setVisible(false);
                    break;
                case "CELL01":
                    imageCell01.setImage(new Image(food.getImagePath()));
                    imageCell01.setVisible(true);
                    addIcon01.setVisible(false);
                    break;
                case "CELL10":
                    imageCell10.setImage(new Image(food.getImagePath()));
                    imageCell10.setVisible(true);
                    addIcon10.setVisible(false);
                    break;
                case "CELL11":
                    imageCell11.setImage(new Image(food.getImagePath()));
                    imageCell11.setVisible(true);
                    addIcon11.setVisible(false);
                    break;
                case "CELL20":
                    imageCell20.setImage(new Image(food.getImagePath()));
                    imageCell20.setVisible(true);
                    addIcon20.setVisible(false);
                    break;
                case "CELL21":
                    imageCell21.setImage(new Image(food.getImagePath()));
                    imageCell21.setVisible(true);
                    addIcon21.setVisible(false);
                    break;
                case "CELL30":
                    imageCell30.setImage(new Image(food.getImagePath()));
                    imageCell30.setVisible(true);
                    addIcon30.setVisible(false);
                    break;
                case "CELL31":
                    imageCell31.setImage(new Image(food.getImagePath()));
                    imageCell31.setVisible(true);
                    addIcon31.setVisible(false);
                    break;
            }
        }
    }

    private void handleClickOnImageView(){
        ImageView[] imageViews = {imageCell00, imageCell01, imageCell10, imageCell11, imageCell20, imageCell21, imageCell30, imageCell31};
        for (ImageView view : imageViews) {
            String path = "/View/detailList.fxml";
            view.setOnMouseClicked(event -> {
                FXMLLoader loader = new FXMLLoader(SceneController.class.getResource(path));
                try {
                    Parent root = loader.load();
                    DetailListController detailList = loader.getController();
                    detailList.editDetailCardFrom(getIdFromHover(event));

                    Stage stage = SceneController.getCurrentStageOn(event);
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public String getIdFromHover(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        String id = imageView.getId();
        return "CELL" + id.substring(id.length() - 2);
    }
}
