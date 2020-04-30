package Controller;

import Model.Food;
import Util.SceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AddFoodController implements Initializable {
    @FXML
    private TextField foodName, foodUnit, foodAmount;
    @FXML
    private DatePicker expDate;
    @FXML
    private MenuButton headFoodMenu;
    @FXML
    private MenuItem beverageType, dairyType, dessertType, eggType, freshType, frozenType, meatType, veggieType, otherType;
    @FXML
    private CheckBox frozenCheck;
    @FXML
    private Button uploadImageBtn, submitBtn, backBtn;
    @FXML
    private ImageView showSelectedImage;
    @FXML
    private Text errFoodName, errFoodUnit, errExpDate, errFoodAmount, errFoodType, errFrozenCheck;

    private String defaultText;
    private DateTimeFormatter dateFormat;
    private File imgFile;
    private String selectedCell;
    private List<String> freezerCell;
    private List<String> normalCell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        defaultText = headFoodMenu.getText();
        dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        freezerCell = new ArrayList<>(Arrays.asList("CELL00", "CELL01"));
        normalCell = new ArrayList<>(Arrays.asList("CELL10", "CELL11", "CELL20", "CELL21", "CELL30", "CELL31"));

        Text[] errors = {errFoodName, errFoodUnit, errExpDate, errFoodAmount, errFoodType, errFrozenCheck};
        for (Text err : errors) {
            err.setVisible(false);
        }
        frozenCheck.setOnMouseClicked(event -> {
            if (frozenCheck.isSelected() && normalCell.contains(selectedCell)){
                errFrozenCheck.setText("Warning!!! You're put FROZEN food into chilling cell");
                errFrozenCheck.setVisible(true);
            } else {
                errFrozenCheck.setVisible(false);
            }
        });

        setupDropDownMenu();
        expDate.setOnShown(event -> {
            expDate.getEditor().setStyle("-fx-text-fill: black");
            errExpDate.setVisible(false);
        });

        submitBtn.setOnAction((e) -> {
            if (isValidFoodName() && isValidFoodType() && isValidFoodUnit() && isValidFoodAmount() && isValidExpDate()) {
                String imgPath = copyImage(imgFile);
                Food food = new Food(
                        foodName.getText(),
                        headFoodMenu.getText(),
                        foodUnit.getText(),
                        Integer.parseInt(foodAmount.getText()),
                        LocalDate.parse(expDate.getValue().format(dateFormat), dateFormat),
                        frozenCheck.isSelected()
                );
                food.setImagePath(imgPath);
                RefrigeratorController refrigerator = new RefrigeratorController(UserAccountController.getActiveUser());
                refrigerator.addFoodTo(selectedCell, food);
                refrigerator.saveFoodData(selectedCell, food);
                // will add alert box, show its success
                try {
                    SceneController.changeSceneTo("/View/open-refrigerator.fxml", SceneController.getCurrentStageOn(e));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        uploadImageBtn.setOnAction(e -> {
            handleUploadImageBtn(e);
            if (imgFile != null) {
                try {
                    showSelectedImage.setImage(new Image(imgFile.toURI().toURL().toExternalForm()));
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        backBtn.setOnAction(event -> {
            try {
                SceneController.changeSceneTo("/View/open-refrigerator.fxml", SceneController.getCurrentStageOn(event));
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't load open-refrigerator page");
            }
        });

    }

    public void setSelectedCell(String cell){
        this.selectedCell = cell;
    }

    public void setInitForm(Food food){
        foodName.setText(food.getName());
        headFoodMenu.setText(food.getType());
        headFoodMenu.setDisable(true);
        foodUnit.setText(food.getUnit());
        frozenCheck.setSelected(food.isFrozen());
        frozenCheck.setDisable(true);
        showSelectedImage.setImage(new Image(food.getImagePath()));
    }

    private boolean isValidFoodName() {
        if (foodName.getText().isEmpty()) {
            errFoodName.setVisible(true);
            errFoodName.setText("Please enter name of food");
            return false;
        } else {
            errFoodName.setVisible(false);
            return true;
        }
    }

    private boolean isValidFoodType() {
        if (headFoodMenu.getText().equals(defaultText)) {
            errFoodType.setVisible(true);
            errFoodType.setText("Please select food type");
            return false;
        }
        else if (headFoodMenu.getText().equals(frozenType.getText()) && normalCell.contains(selectedCell)){
            errFoodType.setVisible(true);
            errFoodType.setText("Can't put frozen food to chilling cell");
            return false;
        }
        else {
            errFoodType.setVisible(false);
            return true;
        }
    }

    private boolean isValidFoodUnit() {
        if (foodUnit.getText().isEmpty()) {
            errFoodUnit.setVisible(true);
            errFoodUnit.setText("Please enter unit of food");
            return false;
        } else {
            errFoodUnit.setVisible(false);
            return true;
        }
    }

    private boolean isValidFoodAmount() {
        if (foodAmount.getText().isEmpty()) {
            errFoodAmount.setVisible(true);
            errFoodAmount.setText("Please enter amount of food");
            return false;
        }
        try {
            int amount = Integer.parseInt(foodAmount.getText());
            if (amount <= 0) {
                errFoodAmount.setVisible(true);
                errFoodAmount.setText("Amount of food should be 1 or more");
                return false;
            } else {
                errFoodAmount.setVisible(false);
                return true;
            }
        } catch (NumberFormatException e) {
            errFoodAmount.setVisible(true);
            errFoodAmount.setText("Amount of food should be number");
            return false;
        }
    }

    private boolean isValidExpDate() {
        if (expDate.getEditor().getText().isEmpty()) {
            errExpDate.setVisible(true);
            errExpDate.setText("Please enter expire date");
            return false;
        }
        LocalDate inputDate = LocalDate.parse(expDate.getValue().format(dateFormat), dateFormat);
        if (inputDate.isBefore(LocalDate.now())) {
            errExpDate.setVisible(true);
            errExpDate.setText("Don't put expired food to refrigerator");
            expDate.getEditor().setText(LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("d/M/yyyy")));
            expDate.getEditor().setStyle("-fx-text-fill: red");
            return false;
        } else {
            errExpDate.setVisible(false);
            return true;
        }
    }

    private void handleUploadImageBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select your image");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg"));
        imgFile = fileChooser.showOpenDialog(SceneController.getCurrentStageOn(event));

    }

    private String copyImage(File file){
        String FS = System.getProperty("file.separator");
        String currentDir = System.getProperty("user.dir");
        if (file != null) {
            File destDir = new File(currentDir + FS + "FreezerApp" + FS + "images");
            if (!destDir.exists()) destDir.mkdirs();

            String[] fileSplit = file.getName().split("\\.");
            String filename = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy")) +
                    "_" + System.currentTimeMillis() + "." + fileSplit[fileSplit.length -1];
            Path target = FileSystems.getDefault().getPath(destDir.getAbsolutePath()+FS+filename);
            try {
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.err.println("Can't copy image file");
                e.printStackTrace();
            }
            return target.toUri().toString();
        }
        return "/images/image-not-found.png";
    }

    private void setupDropDownMenu() {
        MenuItem[] menuItems = {beverageType, dairyType, dessertType, eggType, freshType, frozenType, meatType, veggieType, otherType};
        for (MenuItem item : menuItems) {
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    headFoodMenu.setText(item.getText());
                    if (headFoodMenu.getText().equals(frozenType.getText())){
                        frozenCheck.setDisable(true);
                        frozenCheck.setSelected(true);
                    }
                    else {
                        frozenCheck.setDisable(false);
                        frozenCheck.setSelected(false);
                    }
                }
            });
        }
    }

}
