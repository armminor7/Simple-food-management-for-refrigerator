package Controller;

import Model.UserAccount;
import Util.FileController;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserAccountController {
    private final String FS = File.separator;
    private String currentDir = System.getProperty("user.dir");

    private FileController userAccountFile;
    private List<UserAccount> userAccountList;
    private static String activeUser;

    public UserAccountController() throws IOException {
        userAccountList = new ArrayList<>();
        userAccountFile = new FileController(currentDir + FS + "FreezerApp", "userAccount.csv");
        loadUserAcount();
    }

    public void loadUserAcount(){
        if (userAccountFile.isExistData()) {
            String[] line = userAccountFile.getContent().split(System.lineSeparator());
            for (String data : line) {
                String[] temp = data.split(",");
                UserAccount user = new UserAccount(temp[0], temp[1]);
                userAccountList.add(user);
            }
        }
    }

    public void createNewUser(UserAccount newUser) throws IOException {
        userAccountList.add(newUser);

        String userInfo = newUser.getUserName() + "," + newUser.getPassword();
        userAccountFile.appendWithNewLine(userInfo);
        userAccountFile.save();
    }

    public boolean isExistUser(String userName) {
        for (UserAccount user : userAccountList) {
            if (user.getUserName().equalsIgnoreCase(userName)) return true;
        }
        return false;
    }

    public boolean isMatchPassword(String password) {
        for (UserAccount user : userAccountList) {
            if (user.getPassword().equals(password)) return true;
        }
        return false;
    }

    public static String getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(String activeUser) {
        this.activeUser = activeUser;
    }
}
