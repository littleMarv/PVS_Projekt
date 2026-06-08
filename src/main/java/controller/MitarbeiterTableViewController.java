package controller;
import db_zeug.MitarbeiterDao;
import fachklassen.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
        Mitarbeiter[] a = new MitarbeiterDao().readAll();
        mitarbeiterliste.setAll(new MitarbeiterDao().readAll());
        //System.out.println("Mitarbeiterdaten gelesen");
        //System.out.println(mitarbeiterliste);

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

    @FXML
    public void tabellenClick(javafx.scene.input.MouseEvent event) {
        // 1. Prüfen, ob es ein Doppelklick (2 Klicks) war
        if (event.getClickCount() == 2) {

            // 2. Das ausgewählte Objekt direkt aus der Tabelle abfragen
            Mitarbeiter gewaehlterMitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();

            // 3. Sicherheitscheck: Wurde wirklich eine Zeile getroffen?
            // (Falls der Nutzer in den leeren Bereich unter den Zeilen doppelt klickt, ist es null)
            AnchorPane hauptContentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");

            if (hauptContentPane != null) {
                // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
                new ViewLoader().ladeMitarbeiterDetails(gewaehlterMitarbeiter, hauptContentPane);
            }
        }
    }

    // Öffnet die Eingabemaske für einen neuen Mitarbeiter.
    @FXML
    void mitarbeiterNeuOeffnen() {
        AnchorPane hauptContentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");
        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            new ViewLoader().ladeMitarbeiterDetails(new Mitarbeiter(), hauptContentPane);
        }
    }

    // Öffnet die Eingabemaske für den ausgewählten Mitarbeiter.
    @FXML
    void mitarbeiterBearbeitenOeffnen() {
        Mitarbeiter ausgewaehlterMitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();

        if (ausgewaehlterMitarbeiter == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
            return;
        }

        AnchorPane hauptContentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            new ViewLoader().ladeMitarbeiterDetails(ausgewaehlterMitarbeiter, hauptContentPane);
        }
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
            new MitarbeiterDao().deleteOne(ausgewaehlterMitarbeiter.getMitarbeiterId());
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