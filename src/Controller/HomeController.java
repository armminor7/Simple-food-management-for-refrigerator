package Controller;

import Model.UserAccount;
import Util.SceneController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomeController implements Initializable {

    private UserAccountController userAccountController;

    @FXML
    private ImageView bgCloseRefrige;
    @FXML
    private Button creator;
    @FXML
    private Button manual;
    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;
    @FXML
    private Button register;
    @FXML
    private Label errMsg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bgCloseRefrige.setImage(new Image("images/refrigerator-close.png"));
        errMsg.setVisible(false);


        login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleLoginButton(event);
            }
        });

        register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    SceneController.createModalWindow("Register New Account", "/View/register-form.fxml", event);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Can't open Register Form");
                }
            }
        });

        userName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                errMsg.setVisible(false);
            }
        });
        password.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                errMsg.setVisible(false);
            }
        });

    }

    private void handleLoginButton(ActionEvent event) {
        String userTxt = userName.getText();
        String pwd = password.getText();
        try {
            userAccountController = new UserAccountController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (userAccountController.isExistUser(userTxt) && userAccountController.isMatchPassword(pwd)) {
            userAccountController.setActiveUser(userTxt);
            try {
                SceneController.changeSceneTo("/View/open-refrigerator.fxml", SceneController.getCurrentStageOn(event));
            } catch (IOException ex) {
                System.err.println("Can't load next page");
                ex.printStackTrace();
            }
        } else {
            errMsg.setVisible(true);
            errMsg.setText("ID or Password is incorrect");
            userName.setText("");
            password.setText("");
        }

    }

}
