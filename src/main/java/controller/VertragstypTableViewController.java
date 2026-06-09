package controller;

import db_zeug.VertragDao;
import fachklassen.Vertrag;
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

public class VertragstypTableViewController implements Initializable {

    // Liste, die JavaFX in der Tabelle anzeigen kann
    private ObservableList<Vertrag> vertragstypListe = FXCollections.observableArrayList();

    // Suchfeld aus der FXML-Datei
    @FXML
    private TextField vertragstypSucheTextField;

    // Tabelle aus der FXML-Datei
    @FXML
    private TableView<Vertrag> vertragstypTableView;

    // Spalten aus der FXML-Datei
    @FXML
    private TableColumn<Vertrag, Integer> vertragstypIdColumn;

    @FXML
    private TableColumn<Vertrag, String> vertragstypBezeichnungColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Verbindet die Spalten mit den Getter-Methoden aus der Vertrag-Klasse
        vertragstypIdColumn.setCellValueFactory(new PropertyValueFactory<>("vertragId"));
        vertragstypBezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));

        // Lädt die Vertragstypen aus der Datenbank in die Tabelle
        ladeVertragstypen();
    }

    private void ladeVertragstypen() {
        // Holt alle Vertragstypen über den DAO aus der Datenbank
        vertragstypListe.setAll(new VertragDao().readAll());

        // Übergibt die geladene Liste an die Tabelle
        vertragstypTableView.setItems(vertragstypListe);
    }

    @FXML
    void vertragstypSuche(KeyEvent event) {
        // Text aus dem Suchfeld lesen
        String typed = vertragstypSucheTextField.getText();

        // Tabelle mit den passenden Suchergebnissen neu füllen
        vertragstypListe.setAll(new VertragDao().fuzzyRead(typed));
    }

    @FXML
    void vertragstypNeuOeffnen() {
        // Öffnet die vorhandene Einzelmaske zum Anlegen eines Vertragstyps
        Pane vertragstypView = new ViewLoader().loadView("vertragstyp_view");
        setzeViewInDieMitte(vertragstypView);
    }

    @FXML
    void vertragstypBearbeitenOeffnen() {
        // Holt den Vertragstyp, der in der Tabelle ausgewählt wurde
        Vertrag ausgewaehlterVertragstyp = vertragstypTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehlterVertragstyp == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kein Vertragstyp ausgewählt");
            alert.setHeaderText(null);
            alert.setContentText("Bitte zuerst einen Vertragstyp aus der Tabelle auswählen.");
            alert.showAndWait();
            return;
        }

        ladeVertragstypZumBearbeiten(ausgewaehlterVertragstyp);
    }

    private void ladeVertragstypZumBearbeiten(Vertrag vertragstyp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/vertragstyp_view.fxml"));
            Pane vertragstypView = loader.load();

            VertragstypViewController vertragstypViewController = loader.getController();
            vertragstypViewController.setAktuellerVertragstyp(vertragstyp);

            setzeViewInDieMitte(vertragstypView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void vertragstypLoeschen() {
        // Fragt nach, bevor später wirklich gelöscht wird.
        Alert bestaetigung = new Alert(Alert.AlertType.CONFIRMATION);
        bestaetigung.setTitle("Vertragstyp löschen");
        bestaetigung.setHeaderText(null);
        bestaetigung.setContentText("Soll der ausgewählte Vertragstyp wirklich gelöscht werden?");

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");
        bestaetigung.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (bestaetigung.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
            // Platzhalter: Das echte Löschen wird später mit der Datenbank verbunden.
            Alert platzhalter = new Alert(Alert.AlertType.INFORMATION);
            platzhalter.setTitle("Löschen noch nicht verbunden");
            platzhalter.setHeaderText(null);
            platzhalter.setContentText("DB fehelt");
            platzhalter.showAndWait();
        }
    }

    private void setzeViewInDieMitte(Pane view) {
        AnchorPane hauptContentPane = (AnchorPane) vertragstypTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null && view != null) {
            hauptContentPane.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
        }
    }
}
