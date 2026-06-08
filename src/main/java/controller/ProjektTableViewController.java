package controller;

import db_zeug.ProjektDao;
import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjektTableViewController implements Initializable {

    private ObservableList<Projekt> masterData = FXCollections.observableArrayList();
    private FilteredList<Projekt> filteredData;
    @FXML
    private TableColumn<Projekt, String> abschlussColumn;

    @FXML
    private TableColumn<Projekt, String> beginnColumn;

    @FXML
    private TableColumn<Projekt, String> bezeichnungColumn;

    @FXML
    private Button projektBearbeitenButton;

    @FXML
    private TableColumn<Projekt, Integer> projektIdColumn;

    @FXML
    private Button projektLoeschenButton;

    @FXML
    private Button projektNeuButton;

    @FXML
    private TextField projektSucheTextField;

    @FXML
    private TableView<Projekt> projektTableView;

    @FXML
    private TableColumn<Projekt, String> projektleitungColumn;

    @FXML
    void deleteProjektButton(ActionEvent event) {

    }

    @FXML
    void editProjektButton(ActionEvent event) {

        Projekt ausgewaehltesProjekt = projektTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehltesProjekt == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
            return;
        }

        AnchorPane hauptContentPane = (AnchorPane) projektTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            new ViewLoader().ladeProjektDetails(ausgewaehltesProjekt, hauptContentPane);

        }
    }
    @FXML
    public void tabellenClick(javafx.scene.input.MouseEvent event) {
        // 1. Prüfen, ob es ein Doppelklick (2 Klicks) war
        if (event.getClickCount() == 2) {

            // 2. Das ausgewählte Objekt direkt aus der Tabelle abfragen
            Projekt ausgewaehltesProjekt = projektTableView.getSelectionModel().getSelectedItem();

            // 3. Sicherheitscheck: Wurde wirklich eine Zeile getroffen?
            // (Falls der Nutzer in den leeren Bereich unter den Zeilen doppelt klickt, ist es null)
            AnchorPane hauptContentPane = (AnchorPane) projektTableView.getScene().lookup("#contentPane");

            if (hauptContentPane != null) {
                // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
                new ViewLoader().ladeProjektDetails(ausgewaehltesProjekt, hauptContentPane);
            }
        }
    }

    @FXML
    void neuProjektButton(ActionEvent event) {
        AnchorPane hauptContentPane = (AnchorPane) projektTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            new ViewLoader().ladeProjektDetails(new Projekt(), hauptContentPane);
        }
    }

    @FXML
    void projektSuche(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        masterData.setAll(new ProjektDao().readAll());
        filteredData = new FilteredList<>(masterData, p -> true);
        projektSucheTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(projekt -> {
                // Wenn das Suchfeld leer ist, alle anzeigen
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                String projektbz = projekt.getBezeichnung() != null ? projekt.getBezeichnung().toLowerCase() : "";
                String vorname = projekt.getProjektleitung().getVorname() != null ? projekt.getProjektleitung().getVorname().toLowerCase() : "";
                String nachname = projekt.getProjektleitung().getNachname() != null ? projekt.getProjektleitung().getNachname().toLowerCase() : "";

                // Filter-Logik (Vorname ODER Nachname)
                return vorname.contains(lowerCaseFilter) || nachname.contains(lowerCaseFilter) || projektbz.contains(lowerCaseFilter);
            });
        });



        //System.out.println("Mitarbeiterdaten gelesen");
        //System.out.println(mitarbeiterliste);
        projektIdColumn.setCellValueFactory(new PropertyValueFactory<>("projektId"));
        bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        beginnColumn.setCellValueFactory(new PropertyValueFactory<>("beginn"));
        abschlussColumn.setCellValueFactory(new PropertyValueFactory<>("abschluss"));
        projektleitungColumn.setCellValueFactory(new PropertyValueFactory<>("projektleitung"));


        projektTableView.setItems(filteredData);
    }

    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }



}

