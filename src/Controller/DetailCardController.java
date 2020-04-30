package Controller;

import Model.Food;
import Util.SceneController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DetailCardController implements Initializable {

    @FXML
    private AnchorPane rootCardPane;
    @FXML
    private GridPane addBg;
    @FXML
    private ImageView foodImage;
    @FXML
    private Text foodName, numAmount, foodUnit, numStore, storeDay, expireDay, numExpire, expText;
    @FXML
    private Label storeDate, expireDate;
    @FXML
    private Button minBtn, minus, plus, maxBtn, getFoodBtn;
    @FXML
    private TextField numGetFood;
    @FXML
    private FontAwesomeIconView addBtn;

    private Food food;
    private RefrigeratorController refrigetor;
    private String selectedCell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refrigetor = new RefrigeratorController(UserAccountController.getActiveUser());
        refrigetor.loadFoodData();
        minBtn.setOnAction(event -> handleMinBtn());
        maxBtn.setOnAction(event -> handleMaxBtn());
        minus.setOnAction(event -> handleMinusBtn());
        plus.setOnAction(event -> handlePlusBtn());
        getFoodBtn.setOnAction(event -> {
            if (validationNumGetFood()) {
                handleGetFoodBtn(event);
            }
        });
        addBtn.setOnMouseClicked(event -> {
            try {
                handleAddBtn(event);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't load Add page");
            }
        });
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void loadViewDetail() {
        foodImage.setImage(new Image(food.getImagePath()));
        foodName.setText(food.getName());
        numAmount.setText(String.valueOf(food.getAmount()));
        foodUnit.setText(food.getUnit());

        numStore.setText(String.valueOf(food.getDayInRefrigerator()));
        storeDay.setText(food.getDayInRefrigerator() <= 1 ? "day" : "days");
        storeDate.setText("(Stored Date : " + food.getStoredDate() + ")");

        if (food.getShelfLife() == 0) {
            numExpire.setText("");
            expireDay.setText("Today");
            expireDate.setText("(Expired Date : " + food.getExpireDate() + ")");
            expText.setText("Expired");
            expText.setStyle("-fx-fill: red");
            numExpire.setStyle("-fx-fill: red");
            expireDay.setStyle("-fx-fill: red");
            expireDate.setStyle("-fx-text-fill: red");
        } else if (food.getShelfLife() < 1) {
            numExpire.setText(String.valueOf(Math.abs(food.getShelfLife())));
            expireDay.setText(Math.abs(food.getShelfLife()) <= 1 ? "day" : "days");
            expireDate.setText("(Expired Date : " + food.getExpireDate() + ")");
            expText.setText("Expired   ");
            expText.setStyle("-fx-fill: red");
            numExpire.setStyle("-fx-fill: red");
            expireDay.setStyle("-fx-fill: red");
            expireDate.setStyle("-fx-text-fill: red");
        } else {
            numExpire.setText(String.valueOf(food.getShelfLife()));
            expireDay.setText(food.getShelfLife() <= 1 ? "day" : "days");
            expireDate.setText("(Expired Date : " + food.getExpireDate() + ")");
            expText.setText("Expires in");
            expText.setStyle("-fx-fill: black");
            numExpire.setStyle("-fx-text-fill: black");
            expireDay.setStyle("-fx-text-fill: black");
            expireDate.setStyle("-fx-text-fill: black");
        }
        handleVisibleButton(false);
    }

    public void loadEditDetailCard() {
        loadViewDetail();
        handleVisibleButton(true);

    }

    public void loadAddCard() {
        for (Node element : rootCardPane.getChildren()) {
            element.setVisible(false);
        }
        addBg.setVisible(true);
    }

    private void handleVisibleButton(boolean visible) {
        Button[] buttons = {minBtn, minus, plus, maxBtn, getFoodBtn};
        for (Button btn : buttons) {
            btn.setVisible(visible);
            numGetFood.setVisible(visible);
        }
    }

    public void setDisableAll(boolean status) {
        for (Node element : rootCardPane.getChildren()) {
            element.setDisable(status);
        }
    }

    private void handleMinBtn() {
        numGetFood.setText("1");
    }

    private void handleMaxBtn() {
        numGetFood.setText(String.valueOf(food.getAmount()));
    }

    private void handleMinusBtn() {
        int tmp = Integer.parseInt(numGetFood.getText());
        tmp -= 1;
        if (tmp < 1) {
            numGetFood.setText("1");
        } else {
            numGetFood.setText(String.valueOf(tmp));
        }
    }

    private void handlePlusBtn() {
        int tmp = Integer.parseInt(numGetFood.getText());
        tmp += 1;
        if (tmp > food.getAmount()) {
            numGetFood.setText(String.valueOf(food.getAmount()));
        } else {
            numGetFood.setText(String.valueOf(tmp));
        }
    }

    private void handleGetFoodBtn(ActionEvent event) {
        int amount = Integer.parseInt(numGetFood.getText());
        List<Food> tray = refrigetor.getFoodListFrom(this.selectedCell);
        Alert confirmGetFood = new Alert(AlertType.CONFIRMATION);
        String headerText = "\"" + food.getName() + "\" (" + expText.getText().toLowerCase().trim() + " " + numExpire.getText() + " " + expireDay.getText() + ")";
        confirmGetFood.setTitle("Freezer App : Get Food Out");
        ImageView alertImg = foodImage;
        alertImg.setFitHeight(60);
        confirmGetFood.setGraphic(alertImg);


        if (amount == food.getAmount()) {
            confirmGetFood.setHeaderText("You're getting all of " + headerText + " out.");
            confirmGetFood.setContentText("Are you sure?");
            Optional<ButtonType> answer = confirmGetFood.showAndWait();
            if (answer.get() == ButtonType.OK) {
                refrigetor.getFoodOut(this.selectedCell);
                refrigetor.deleteFoodData(this.selectedCell, food);
                try {
                    SceneController.changeSceneTo("/View/open-refrigerator.fxml", SceneController.getCurrentStageOn(event));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Can't load open page");
                }
            }
        } else {
            String oldStr = this.selectedCell + "," + food.toString();
            confirmGetFood.setHeaderText("You're getting " + numGetFood.getText() + " " + foodUnit.getText() + " of " + headerText + " out.");
            confirmGetFood.setContentText("Are you sure?");
            int newAmount = food.getAmount() - amount;
            Optional<ButtonType> answer = confirmGetFood.showAndWait();
            if (answer.get() == ButtonType.OK) {
                food.setAmount(newAmount);
                String newStr = this.selectedCell + "," + food.toString();
                refrigetor.updateFoodData(oldStr, newStr);
                numAmount.setText(String.valueOf(food.getAmount()));
                numGetFood.setText("1");
            }
        }

    }

    private boolean validationNumGetFood() {
        Alert alert = new Alert(AlertType.ERROR);
        try {
            int num = Integer.parseInt(numGetFood.getText());
            if (num < 1) {
                alert.setTitle("Error: Input is less than 1");
                alert.setHeaderText("Input should be 1 or more.");
                alert.showAndWait();
                numGetFood.setText("1");
                return false;
            } else if (num > food.getAmount()) {
                alert.setTitle("Error: Input is more than food amount");
                alert.setHeaderText("Input should be less than food amount");
                alert.showAndWait();
                numGetFood.setText(String.valueOf(food.getAmount()));
                return false;
            }
        } catch (NumberFormatException e) {
            alert.setTitle("Error: Input is not a number");
            alert.setHeaderText("Your input is not a number.");
            alert.showAndWait();
            numGetFood.setText("1");
            return false;
        }
        return true;
    }

    public void setSelectedCell(String selectedCell) {
        this.selectedCell = selectedCell;
    }

    public void handleAddBtn(Event event) throws IOException {
        String path = "/View/addFood.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent root = loader.load();

        AddFoodController addFoodController = loader.getController();
        addFoodController.setSelectedCell(selectedCell);
        addFoodController.setInitForm(food);

        Stage stage = SceneController.getCurrentStageOn(event);
        stage.setScene(new Scene(root));
        stage.show();
    }


}

