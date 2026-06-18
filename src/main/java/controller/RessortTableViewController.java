package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.RessortDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ressort;
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

public class RessortTableViewController implements Initializable {

    // Liste, die JavaFX in der Tabelle anzeigen kann
    private ObservableList<Ressort> ressortListe = FXCollections.observableArrayList();
    private AnchorPane hauptcontenpane;
    // Suchfeld aus der FXML-Datei
    @FXML
    private TextField ressortSucheTextField;

    // Tabelle aus der FXML-Datei
    @FXML
    private TableView<Ressort> ressortTableView;

    // Spalten aus der FXML-Datei
    @FXML
    private TableColumn<Ressort, Integer> ressortIdColumn;

    @FXML
    private TableColumn<Ressort, String> ressortBezeichnungColumn;

    @FXML
    private TableColumn<Ressort, Integer> ressortMitarbeiterAnzahlColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Verbindet die Spalten mit den Getter-Methoden aus der Ressort-Klasse
        ressortIdColumn.setCellValueFactory(new PropertyValueFactory<>("ressortId"));
        ressortBezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        ressortMitarbeiterAnzahlColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(holeMitarbeiterImRessort(cellData.getValue()).size())
        );

        // Ein Doppelklick auf eine Zeile zeigt die Mitarbeiter an, die zu diesem Ressort gehören.
        ressortTableView.setRowFactory(tableView -> {
            TableRow<Ressort> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    zeigeMitarbeiterImRessort(row.getItem());
                }
            });
            return row;
        });
        hauptcontenpane = (AnchorPane) ressortTableView.getScene().lookup("#contentPane");
        // Lädt die Ressorts aus der Datenbank in die Tabelle
        ladeRessorts();
    }

    private void ladeRessorts() {
        // Holt alle Ressorts über den DAO aus der Datenbank
        ressortListe.setAll(new RessortDao().readAll());

        // Übergibt die geladene Liste an die Tabelle
        ressortTableView.setItems(ressortListe);
    }

    @FXML
    void ressortSuche(KeyEvent event) {
        // Text aus dem Suchfeld lesen
        String typed = ressortSucheTextField.getText();

        // Tabelle mit den passenden Suchergebnissen neu füllen
        ressortListe.setAll(new RessortDao().fuzzyRead(typed));
    }

    @FXML
    void ressortNeuOeffnen() {
        // Öffnet die vorhandene Einzelmaske zum Anlegen eines Ressorts
        Pane ressortView = ViewLoader.getViewLoader().loadView("ressort_view", hauptcontenpane);
        setzeViewInDieMitte(ressortView);
    }

    @FXML
    void ressortBearbeitenOeffnen() {
        // Holt das Ressort, das in der Tabelle ausgewählt wurde
        Ressort ausgewaehltesRessort = ressortTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehltesRessort == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kein Ressort ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte zuerst ein Ressort aus der Tabelle auswählen.");
            alert.showAndWait();
            return;
        }

        ladeRessortZumBearbeiten(ausgewaehltesRessort);
    }

    private void ladeRessortZumBearbeiten(Ressort ressort) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/ressort_view.fxml"));
            Pane ressortView = loader.load();

            RessortViewController ressortViewController = loader.getController();
            ressortViewController.setAktuellesRessort(ressort);

            setzeViewInDieMitte(ressortView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ressortLoeschen() {
        // Fragt nach, bevor später wirklich gelöscht wird.
        Alert bestaetigung = new Alert(Alert.AlertType.CONFIRMATION);
        bestaetigung.setTitle("Ressort löschen");
        bestaetigung.setHeaderText(null);
        bestaetigung.setContentText("Soll das ausgewählte Ressort wirklich gelöscht werden?");

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");
        bestaetigung.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (bestaetigung.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
            // Platzhalter: Das echte Löschen wird später mit der Datenbank verbunden.
            Alert platzhalter = new Alert(Alert.AlertType.INFORMATION);
            platzhalter.setTitle("Löschen noch nicht verbunden");
            platzhalter.setHeaderText(null);
            platzhalter.setContentText("Db fehlt.");
            platzhalter.showAndWait();
        }
    }

    private void setzeViewInDieMitte(Pane view) {
        AnchorPane hauptContentPane = (AnchorPane) ressortTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null && view != null) {
            hauptContentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
        }
    }

    private List<Mitarbeiter> holeMitarbeiterImRessort(Ressort ressort) {
        List<Mitarbeiter> zugeordneteMitarbeiter = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : new MitarbeiterDao().readAll()) {
            if (mitarbeiter.getRessort() != null && mitarbeiter.getRessort().getRessortId() == ressort.getRessortId()) {
                zugeordneteMitarbeiter.add(mitarbeiter);
            }
        }

        return zugeordneteMitarbeiter;
    }

    private void zeigeMitarbeiterImRessort(Ressort ressort) {
        List<Mitarbeiter> zugeordneteMitarbeiter = holeMitarbeiterImRessort(ressort);
        StringBuilder text = new StringBuilder();

        if (zugeordneteMitarbeiter.isEmpty()) {
            text.append("Diesem Ressort sind keine Mitarbeiter zugeordnet.");
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
        alert.setHeaderText(ressort.getBezeichnung());
        alert.setContentText(text.toString());
        alert.showAndWait();
    }
}
