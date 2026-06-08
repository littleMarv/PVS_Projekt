package controller;

import db_zeug.OrtDao;
import fachklassen.Ort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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

public class OrtTableViewController implements Initializable {

    // Liste, die JavaFX in der Tabelle anzeigen kann
    private ObservableList<Ort> ortListe = FXCollections.observableArrayList();

    // Suchfeld aus der FXML-Datei
    @FXML
    private TextField ortSucheTextField;

    // Tabelle aus der FXML-Datei
    @FXML
    private TableView<Ort> ortTableView;

    // Spalten aus der FXML-Datei
    @FXML
    private TableColumn<Ort, Integer> ortIdColumn;

    @FXML
    private TableColumn<Ort, String> plzColumn;

    @FXML
    private TableColumn<Ort, String> ortsnameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Verbindet die Spalten mit den Getter-Methoden aus der Ort-Klasse
        ortIdColumn.setCellValueFactory(new PropertyValueFactory<>("ortId"));
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
        ortsnameColumn.setCellValueFactory(new PropertyValueFactory<>("ortsname"));

        // Lädt die Orte aus der Datenbank in die Tabelle
        ladeOrte();
    }

    private void ladeOrte() {
        // Holt alle Orte über den DAO aus der Datenbank
        ortListe.setAll(new OrtDao().readAll());

        // Übergibt die geladene Liste an die Tabelle
        ortTableView.setItems(ortListe);
    }

    @FXML
    void ortSuche(KeyEvent event) {
        // Text aus dem Suchfeld lesen
        String typed = ortSucheTextField.getText();

        // Tabelle mit den passenden Suchergebnissen neu füllen
        ortListe.setAll(new OrtDao().fuzzyRead(typed));
    }

    @FXML
    void ortNeuOeffnen() {
        // Sucht den mittleren Inhaltsbereich aus der Hauptansicht
        AnchorPane hauptContentPane = (AnchorPane) ortTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Lädt die vorhandene Einzelmaske zum Anlegen eines Ortes
            Pane ortView = new ViewLoader().loadView("ort_view");
            hauptContentPane.getChildren().setAll(ortView);

            AnchorPane.setTopAnchor(ortView, 0.0);
            AnchorPane.setRightAnchor(ortView, 0.0);
            AnchorPane.setBottomAnchor(ortView, 0.0);
            AnchorPane.setLeftAnchor(ortView, 0.0);
        }
    }

    @FXML
    void ortBearbeitenOeffnen() {
        // Holt den Ort, der in der Tabelle ausgewählt wurde
        Ort ausgewaehlterOrt = ortTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehlterOrt == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kein Ort ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte zuerst einen Ort aus der Tabelle auswählen.");
            alert.showAndWait();
            return;
        }

        // Öffnet die Ort-Maske und trägt die Daten des ausgewählten Ortes ein
        ladeOrtZumBearbeiten(ausgewaehlterOrt);
    }

    private void ladeOrtZumBearbeiten(Ort ort) {
        try {
            AnchorPane hauptContentPane = (AnchorPane) ortTableView.getScene().lookup("#contentPane");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/ort_view.fxml"));
            Pane ortView = loader.load();

            OrtViewController ortViewController = loader.getController();
            ortViewController.setAktuellerOrt(ort);

            hauptContentPane.getChildren().setAll(ortView);

            AnchorPane.setTopAnchor(ortView, 0.0);
            AnchorPane.setRightAnchor(ortView, 0.0);
            AnchorPane.setBottomAnchor(ortView, 0.0);
            AnchorPane.setLeftAnchor(ortView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ortLoeschen() {
        // Fragt nach, bevor später wirklich gelöscht wird.
        Alert bestaetigung = new Alert(Alert.AlertType.CONFIRMATION);
        bestaetigung.setTitle("Ort löschen");
        bestaetigung.setHeaderText(null);
        bestaetigung.setContentText("Soll der ausgewählte Ort wirklich gelöscht werden?");

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");
        bestaetigung.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (bestaetigung.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
            // Platzhalter: Das echte Löschen wird später mit der Datenbank verbunden.
            Alert platzhalter = new Alert(Alert.AlertType.INFORMATION);
            platzhalter.setTitle("Löschen noch nicht verbunden");
            platzhalter.setHeaderText(null);
            platzhalter.setContentText("DB fehlt.");
            platzhalter.showAndWait();
        }
    }
}
