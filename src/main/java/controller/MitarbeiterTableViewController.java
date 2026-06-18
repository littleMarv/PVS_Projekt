package controller;
import db_zeug.MitarbeiterDao;
import fachklassen.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.ModelService;
import model.SuchHelper;

import java.net.URL;
import java.util.ResourceBundle;


public class MitarbeiterTableViewController implements Initializable {

    FilteredList<Mitarbeiter> filteredData;
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
        personalnummerColumn.setCellValueFactory(new PropertyValueFactory<>("persNr"));
        nachnameColumn.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        vornameColumn.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        gebDatumColumn.setCellValueFactory(new PropertyValueFactory<>("geburtsdatumstr"));
        geschlechtColumn.setCellValueFactory(new PropertyValueFactory<>("geschlecht"));
        ressortColumn.setCellValueFactory(new PropertyValueFactory<>("ressortbz"));
        plzColumn.setCellValueFactory(new PropertyValueFactory<>("plz"));
        ortColumn.setCellValueFactory(new PropertyValueFactory<>("ortsname"));
        vertragstypColumn.setCellValueFactory(new PropertyValueFactory<>("vertragbz"));
        SuchHelper.verknuepfe(mitarbeiterSucheTextField,mitarbeiterTable,ModelService.getInstance().getAlleMitarbeiter());
    }

    @FXML
    void mitarbeiterSuche(KeyEvent event) {
    //    String typed = mitarbeiterSucheTextField.getText();
    //    System.out.println(typed);
    //    filteredData.setAll(new MitarbeiterDao().fuzzyRead(typed));
    }



    // Öffnet die Eingabemaske für einen neuen Mitarbeiter.
    @FXML
    void mitarbeiterNeuOeffnen() {
    //TODO: neues Laden:    ViewLoader.getViewLoader().loadView("mitarbeiter_view", ausgewaehlterMitarbeiter);
        AnchorPane hauptContentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");
        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            ViewLoader.getViewLoader().loadView("mitarbeiter_view", new Mitarbeiter());
        }
    }

    // Öffnet die Eingabemaske für den ausgewählten Mitarbeiter.
    @FXML
    public void tabellenClick(javafx.scene.input.MouseEvent event) {
        // 1. Prüfen, ob es ein Doppelklick (2 Klicks) war
        if (event.getClickCount() == 2) {
            mitarbeiterBearbeitenOeffnen();
        }
    }

    @FXML
    void mitarbeiterBearbeitenOeffnen() {
        Mitarbeiter ausgewaehlterMitarbeiter = mitarbeiterTable.getSelectionModel().getSelectedItem();

        if (ausgewaehlterMitarbeiter == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
            return;
        }
        //TODO: neues Laden:    ViewLoader.getViewLoader().loadView("mitarbeiter_view", ausgewaehlterMitarbeiter);

        AnchorPane hauptContentPane = (AnchorPane) mitarbeiterTable.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            ViewLoader.getViewLoader().loadView("mitarbeiter_view",ausgewaehlterMitarbeiter);
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
        filteredData.setAll(new MitarbeiterDao().readAll());
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