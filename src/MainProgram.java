import Util.FileController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class MainProgram extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/home.fxml"));
        primaryStage.setTitle("Freezer App");
        primaryStage.setScene(new Scene(root, 400, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        final String FS = File.separator;
        String currentDir = System.getProperty("user.dir");
        String mainDir = "FreezerApp";
        try {
            FileController userFile = new FileController(currentDir + FS + mainDir, "userAccount.csv");
            if (!userFile.isExistData()) {
                userFile.appendWithNewLine("admin,test");
                userFile.save();
            }
        } catch (IOException e) {
            System.err.println("Can't create user: admin");
//            e.printStackTrace();
        }
        try {
            FileController refrigeratorFile = new FileController(currentDir + FS + mainDir, "admin_refrigerator.csv");
            if (!refrigeratorFile.isExistData()) {
                refrigeratorFile.append(
                                "CELL10,Pork,Meats,KG,12,10/04/2020,12/03/2020,false,/images/32380-cut-of-meat-icon.png" + "\n" +
                                "CELL10,Pork,Meats,KG,3,01/05/2020,10/03/2020,false,/images/32380-cut-of-meat-icon.png" + "\n" +
                                "CELL10,Pork,Meats,KG,120,17/04/2020,12/03/2020,false,/images/32380-cut-of-meat-icon.png" + "\n" +
                                "CELL10,Pork,Meats,KG,30,16/05/2020,10/03/2020,false,/images/32380-cut-of-meat-icon.png" + "\n" +
                                "CELL10,Pork,Meats,KG,8,20/03/2020,14/03/2020,false,/images/32380-cut-of-meat-icon.png" + "\n" +
                                "CELL00,Ice cream,Frozen Food,pieces,2,12/03/2020,11/03/2020,true,/images/ice-cream.jpg"
                );
                refrigeratorFile.save();
            }
        } catch (IOException e) {
            System.err.println("Can't create data for testing");
        }
        launch(args);
    }
}
