package Controller;

import Model.UserAccount;
import Util.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    @FXML
    private TextField userName;

    @FXML
    private PasswordField password;

    @FXML
    private Label errMsg;

    @FXML
    private Button submit;

    private UserAccountController accountController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errMsg.setVisible(false);
        try {
            accountController = new UserAccountController();
        } catch (IOException e) {
//            e.printStackTrace();
            System.err.println("Can't load UserAccount file");
        }

        submit.setOnAction(this::handleSubmitButton);

    }

    private void handleSubmitButton(ActionEvent event){
        String user = userName.getText();
        String pwd = password.getText();

        if (accountController.isExistUser(user)){
            errMsg.setVisible(true);
            errMsg.setText("Username is exist!!");
        } else {
            errMsg.setVisible(false);
            try {
                accountController.createNewUser(new UserAccount(user, pwd));
            } catch (IOException e) {
//                e.printStackTrace();
                System.err.println("Can't add new user to file");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Register Successful!!");
            alert.setHeaderText(null);
            alert.setContentText("Welcome to FreezeApp. Use your account to login.");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() == ButtonType.OK){
                Stage stage = (Stage) userName.getScene().getWindow();
                stage.close();
            }

        }

    }
}
