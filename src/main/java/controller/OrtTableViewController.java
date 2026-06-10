package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.OrtDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ort;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    private TableColumn<Ort, Integer> ortMitarbeiterAnzahlColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Verbindet die Spalten mit den Getter-Methoden aus der Ort-Klasse
        ortIdColumn.setCellValueFactory(new PropertyValueFactory<>("ortId"));
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
        ortsnameColumn.setCellValueFactory(new PropertyValueFactory<>("ortsname"));
        ortMitarbeiterAnzahlColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(holeMitarbeiterAmOrt(cellData.getValue()).size())
        );

        // Ein Doppelklick auf eine Zeile zeigt die Mitarbeiter an, die zu diesem Ort gehören.
        ortTableView.setRowFactory(tableView -> {
            TableRow<Ort> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    zeigeMitarbeiterAmOrt(row.getItem());
                }
            });
            return row;
        });

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

    private List<Mitarbeiter> holeMitarbeiterAmOrt(Ort ort) {
        List<Mitarbeiter> zugeordneteMitarbeiter = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : new MitarbeiterDao().readAll()) {
            if (mitarbeiter.getOrt() != null && mitarbeiter.getOrt().getOrtId() == ort.getOrtId()) {
                zugeordneteMitarbeiter.add(mitarbeiter);
            }
        }

        return zugeordneteMitarbeiter;
    }

    private void zeigeMitarbeiterAmOrt(Ort ort) {
        List<Mitarbeiter> zugeordneteMitarbeiter = holeMitarbeiterAmOrt(ort);
        StringBuilder text = new StringBuilder();

        if (zugeordneteMitarbeiter.isEmpty()) {
            text.append("Diesem Ort sind keine Mitarbeiter zugeordnet.");
        } else {
            for (Mitarbeiter mitarbeiter : zugeordneteMitarbeiter) {
                text.append(mitarbeiter.getPersNr())
                        .append(" - ")
                        .append(mitarbeiter.getVorname())
                        .append(" ")
                        .append(mitarbeiter.getNachname())
                        .append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zugeordnete Mitarbeiter");
        alert.setHeaderText(ort.getPlz() + " " + ort.getOrtsname());
        alert.setContentText(text.toString());
        alert.showAndWait();
    }
}
