package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.UserDao;
import db_zeug.UserRollenDao;
import fachklassen.Mitarbeiter;
import fachklassen.User;
import fachklassen.UserRolle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BenutzerViewController implements Initializable {

    private User angezeigterUser;

    @FXML
    private Label benutzerTitelLabel;

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
    private CheckBox overridePwCheckbox;

    @FXML
    private SearchableComboBox<UserRolle> rolleComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Mitarbeiter> alleMitarbeiter = List.of(new MitarbeiterDao().readAll());
        mitarbeiterComboBox.getItems().setAll(alleMitarbeiter);
        List<UserRolle> alleRollen = new UserRollenDao().readAll();
        rolleComboBox.getItems().setAll(alleRollen);

        mitarbeiterComboBox.setConverter(new StringConverter<Mitarbeiter>() {
            @Override public String toString(Mitarbeiter m) { return m != null ? m.getAuswahlString() : ""; }
            @Override public Mitarbeiter fromString(String s) { return null; }
        });
        rolleComboBox.setConverter(new StringConverter<UserRolle>() {
            @Override public String toString(UserRolle r) { return r != null ? r.getBezeichnung() : ""; }
            @Override public UserRolle fromString(String s) { return null; }
        });
    }

    @FXML
    void abbrechenButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/benutzer_table_view.fxml"));
            Pane detailView = loader.load();


            // Wichtig für das Layout (Wachstum erlauben)
            detailView.setMaxWidth(Double.MAX_VALUE);
            detailView.setMaxHeight(Double.MAX_VALUE);
            AnchorPane pane = (AnchorPane) benutzerAbbrechenButton.getScene().lookup("#contentPane");
            // In die übergebene Pane setzen und verankern
            pane.getChildren().setAll(detailView);
            AnchorPane.setTopAnchor(detailView, 0.0);
            AnchorPane.setRightAnchor(detailView, 0.0);
            AnchorPane.setBottomAnchor(detailView, 0.0);
            AnchorPane.setLeftAnchor(detailView, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void speichernButton() {
        String email = emailTextField.getText();
        String benutzername = benutzernameTextField.getText();

        if (email == null || email.isBlank() || benutzername == null || benutzername.isBlank() ||
                rolleComboBox.getValue() == null || mitarbeiterComboBox.getValue() == null) {
            zeigeHinweis("Bitte Benutzername, E-Mail, Rolle und Mitarbeiter ausfüllen.");
            return;
        }

        if ((angezeigterUser == null || angezeigterUser.getUserId() == 0) && passwortField.getText().isBlank()) {
            zeigeHinweis("Bitte ein Passwort für den neuen Benutzer eingeben.");
            return;
        }

        User user = new User(email, benutzername, mitarbeiterComboBox.getValue(), rolleComboBox.getValue(), aktivCheckBox.isSelected());
        UserDao dao = new UserDao();

        if (angezeigterUser == null || angezeigterUser.getUserId() == 0) {
            // Neue Benutzer brauchen direkt beim Anlegen ein Passwort.
            angezeigterUser = dao.create(user, User.hashPassword(passwortField.getText()));
        } else {
            angezeigterUser.seteMail(user.getEMail());
            angezeigterUser.setUserName(user.getUserName());
            angezeigterUser.setMitarbeiter(user.getMitarbeiter());
            angezeigterUser.setRolle(user.getRolle());
            angezeigterUser.setIsActive(user.getIsActive());
            dao.update(angezeigterUser);

            if (overridePwCheckbox.isSelected()) {
                dao.updatePw(angezeigterUser.getUserId(), User.hashPassword(passwortField.getText()));
            }
        }

        abbrechenButton();
    }

    public void setAktuellerUser(User user) {
        this.angezeigterUser=user;

        if (angezeigterUser.getUserId() == 0) {
            benutzerTitelLabel.setText("Benutzer anlegen");
            passwortField.clear();
            overridePwCheckbox.setSelected(true);
        } else {
            benutzerTitelLabel.setText("Benutzer bearbeiten");
            overridePwCheckbox.setSelected(false);
        }

        emailTextField.setText(angezeigterUser.getEMail());
        benutzernameTextField.setText(angezeigterUser.getUserName());
        mitarbeiterComboBox.setValue(angezeigterUser.getMitarbeiter());
        rolleComboBox.setValue(angezeigterUser.getRolle());
        aktivCheckBox.setSelected(angezeigterUser.getIsActive());
    }

    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
