package controller;

import db_zeug.UserDao;
import fachklassen.User;
import fachklassen.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        UserSession.login(namemail.getText(),hashPassword(passwort.getText()));
        if (UserSession.getInstance().getUser()==null){
            UserSession.logout();
        }
        else {
            new ViewLoader().loadMain();
        }
    }

    public static String hashPassword(String klartextPasswort) {
        try {
            // 1. Die eingebaute SHA-256 Instanz holen
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 2. Das Passwort in Bytes umwandeln und hashen
            byte[] encodedhash = digest.digest(klartextPasswort.getBytes(StandardCharsets.UTF_8));

            // 3. Byte-Array in einen lesbaren Hex-String umwandeln
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString(); // Hashwert für die Datenbank

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithmus nicht gefunden!", e);
        }
    }
}
