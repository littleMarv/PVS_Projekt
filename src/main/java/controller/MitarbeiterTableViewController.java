package controller;

import db_zeug.MitarbeiterDao;
import fachklassen.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MitarbeiterTableViewController implements Initializable {

    ObservableList<Mitarbeiter> mitarbeiterliste = FXCollections.observableArrayList();

    @FXML
    private Button mitarbeiterBearbeitenButton;

    @FXML
    private Button mitarbeiterLoeschenButton;

    @FXML
    private Button mitarbeiterNeuButton;

    @FXML
    private TextField mitarbeiterSucheTextField;

    @FXML
    private TableView<Mitarbeiter> mitarbeiterTable;

    @FXML
    private TableColumn<Mitarbeiter, String> nachnameColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> ortColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> personalnummerColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> ressortColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> vertragstypColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> vornameColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> plzColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> gebDatumColumn;

    @FXML
    private TableColumn<Mitarbeiter, String> geschlechtColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Tabelle füllen
        mitarbeiterliste.setAll(new MitarbeiterDao().readAll());

        personalnummerColumn.setCellValueFactory(new PropertyValueFactory<>("persNr"));
        nachnameColumn.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        vornameColumn.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        gebDatumColumn.setCellValueFactory(new PropertyValueFactory<>("geburtsdatumstr"));
        geschlechtColumn.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        ressortColumn.setCellValueFactory(new PropertyValueFactory<>("ressortbz"));
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ortsname"));
        vertragstypColumn.setCellValueFactory(new PropertyValueFactory<>("vertragbz"));

        mitarbeiterTable.setItems(mitarbeiterliste);
    }

    @FXML
    void mitarbeiterSuche(KeyEvent event) {
        String typed = mitarbeiterSucheTextField.getText();
        System.out.println(typed);
        mitarbeiterliste.setAll(new MitarbeiterDao().fuzzyRead(typed));
    }

    // Öffnet die Eingabemaske für einen neuen Mitarbeiter.
    @FXML
    void mitarbeiterNeuOeffnen() {
        ladeMitarbeiterView(null);
    }

    // Öffnet die Eingabemaske für den ausgewählten Mitarbeiter.
    @FXML
    void mitarbeiterBearbeitenOeffnen() {
        Mitarbeiter ausgewaehlterMitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();

        if (ausgewaehlterMitarbeiter == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
            return;
        }

        ladeMitarbeiterView(ausgewaehlterMitarbeiter);
    }

    // Fragt vor dem Löschen nach, damit kein Datensatz aus Versehen entfernt wird.
    @FXML
    void mitarbeiterLoeschen() {
        Mitarbeiter ausgewaehlterMitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();

        if (ausgewaehlterMitarbeiter == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
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
            zeigeHinweis("Platzhalter: Das Löschen wird später mit der Datenbank verbunden.");
        }
    }

    // Lädt die Mitarbeiter-Maske und übergibt beim Bearbeiten den ausgewählten Mitarbeiter.
    private void ladeMitarbeiterView(Mitarbeiter mitarbeiter) {
        AnchorPane contentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");

        if (contentPane == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/mitarbeiter_view.fxml"));
            Pane view = loader.load();
            MitarbeiterViewController controller = loader.getController();
            controller.setMitarbeiter(mitarbeiter);

            contentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
        } catch (IOException e) {
            zeigeHinweis("Die Mitarbeiter-Maske konnte nicht geladen werden.");
            e.printStackTrace();
        }
    }

    // Kleines Hinweisfenster für einfache Bedienfehler.
    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}