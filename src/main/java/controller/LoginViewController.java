package controller;

import db_zeug.UserDao;
import fachklassen.User;
import fachklassen.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import launcher.HelloApplication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginViewController {

    @FXML
    private Button login;

    @FXML
    private Label meldungLabel;

    @FXML
    private TextField namemail;

    @FXML
    private PasswordField passwort;

    @FXML
    void loginButtonClick(ActionEvent event) {
        UserSession.login(namemail.getText(),User.hashPassword(passwort.getText()));
        if (UserSession.getInstance().getUser()==null){
            UserSession.logout();
        }
        else {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewLoader.getViewLoader().loadMain(stage);
        }
    }


}
