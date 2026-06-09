package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.UserRollenDao;
import fachklassen.Mitarbeiter;
import fachklassen.UserRolle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BenutzerView implements Initializable {

    @FXML
    private CheckBox aktivCheckBox;

    @FXML
    private Button benutzerAbbrechenButton;

    @FXML
    private Button benutzerSpeichernButton;

    @FXML
    private TextField benutzernameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private SearchableComboBox<Mitarbeiter> mitarbeiterComboBox;

    @FXML
    private PasswordField passwortField;

    @FXML
    private SearchableComboBox<UserRolle> rolleComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Mitarbeiter> alleMitarbeiter = List.of(new MitarbeiterDao().readAll());
        mitarbeiterComboBox.getItems().setAll(alleMitarbeiter);
        List<UserRolle> alleRollen = new UserRollenDao().readAll();
        rolleComboBox.getItems().setAll(alleRollen);
    }
}
