package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.ProjektDao;
import db_zeug.UserDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ressort;
import fachklassen.User;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BenutzerTableViewController implements Initializable {
    ObservableList<User> masterData;
    FilteredList<User> filteredData;

    @FXML
    private TableColumn<?, ?> aktivColumn;

    @FXML
    private Button benutzerBearbeitenButton;

    @FXML
    private TableColumn<?, ?> benutzerIdColumn;

    @FXML
    private Button benutzerLoeschenButton;

    @FXML
    private Button benutzerNeuButton;

    @FXML
    private TextField benutzerSucheTextField;

    @FXML
    private TableView<User> benutzerTableView;

    @FXML
    private TableColumn<User, String> benutzernameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> mitarbeiterColumn;

    @FXML
    private TableColumn<User, String> rolleColumn;

    @FXML
    void delUlerButton(ActionEvent event) {
        User ausgewaehlterUser = benutzerTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehlterUser == null) {
            zeigeHinweis("Bitte zuerst einen Benutzer in der Tabelle auswählen.");
            return;
        }

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Löschen bestätigen");
        alert.setHeaderText(null);
        alert.setContentText("Möchten Sie diesen Datensatz wirklich löschen?");
        alert.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (alert.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
            new UserDao().deleteById(ausgewaehlterUser.getUserId());
        }
        masterData.setAll(new UserDao().readAll());
    }

    @FXML
    void editUserButton() {
        // Holt das Ressort, das in der Tabelle ausgewählt wurde
        User ausgewaehltesRessort = benutzerTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehltesRessort == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kein Ressort ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte zuerst ein Ressort aus der Tabelle auswählen.");
            alert.showAndWait();
            return;
        }

        ladeUserZumBearbeiten(ausgewaehltesRessort);
    }


    @FXML
    void newUserButton(ActionEvent event) {
        ladeUserZumBearbeiten(new User());
    }

    @FXML
    void searchTyped(KeyEvent event) {

    }

    @FXML
    void userSearchField(ActionEvent event) {

    }

    @FXML
    void userTable(ActionEvent event) {

    }

    @FXML
    void userTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            editUserButton();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        masterData.setAll(new UserDao().readAll());
        filteredData = new FilteredList<>(masterData, p -> true);
        benutzerSucheTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                // Wenn das Suchfeld leer ist, alle anzeigen
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                String email = user.geteMail() != null ? user.geteMail().toLowerCase() : "";
                String uname = (user.getUserName() != null) ? user.getUserName().toLowerCase() : "";
                String mname = (user.getMitarbeiter() != null && user.getMitarbeiter().getAuswahlString() != null) ? user.getMitarbeiter().getAuswahlString().toLowerCase() : "";

                // Filter-Logik (Vorname ODER Nachname)
                return uname.contains(lowerCaseFilter) || mname.contains(lowerCaseFilter) || email.contains(lowerCaseFilter);
            });
        });

        benutzerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        benutzernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mitarbeiterColumn.setCellValueFactory(new PropertyValueFactory<>("mitarbeiterString"));


        benutzerTableView.setItems(filteredData);
    }

    private void ladeUserZumBearbeiten(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/benutzer_view.fxml"));
            Pane ressortView = loader.load();

            BenutzerViewController userViewController = loader.getController();
            userViewController.setAktuellerUser(user);

            setzeViewInDieMitte(ressortView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setzeViewInDieMitte(Pane view) {
        AnchorPane hauptContentPane = (AnchorPane) benutzerTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null && view != null) {
            hauptContentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
        }
    }
    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}

