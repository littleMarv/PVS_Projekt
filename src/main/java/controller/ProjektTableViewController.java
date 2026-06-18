package controller;

import db_zeug.ProjektDao;
import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import fachklassen.ProjektMitarbeiter;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import model.ModelService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjektTableViewController implements Initializable {

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
    private TableColumn<Projekt, Integer> projektMitarbeiterAnzahlColumn;

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
        editProjekt(ausgewaehltesProjekt);


    }
    @FXML
    public void tabellenClick(javafx.scene.input.MouseEvent event) {
        // 1. Prüfen, ob es ein Doppelklick (2 Klicks) war
        if (event.getClickCount() == 2) {

            // 2. Das ausgewählte Objekt direkt aus der Tabelle abfragen
            Projekt ausgewaehltesProjekt = projektTableView.getSelectionModel().getSelectedItem();

            if (ausgewaehltesProjekt != null) {
                editProjekt(ausgewaehltesProjekt);
            }
        }
    }

    private void editProjekt(Projekt p){
        AnchorPane hauptContentPane = (AnchorPane) projektTableView.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            // Jetzt rufen wir den ViewLoader auf und übergeben die gefundene Pane!
            ViewLoader.getViewLoader().loadView("projekt_view",p);

        }
    }

    @FXML
    void neuProjektButton(ActionEvent event) {
        editProjekt(new Projekt());
    }

    @FXML
    void projektSuche(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filteredData = new FilteredList<>(ModelService.getInstance().getAlleProjekte(), p -> true);
        projektSucheTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(projekt -> {
                // Wenn das Suchfeld leer ist, alle anzeigen
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                String projektbz = projekt.getBezeichnung() != null ? projekt.getBezeichnung().toLowerCase() : "";
                String vorname = (projekt.getProjektleitung() != null && projekt.getProjektleitung().getVorname() != null) ? projekt.getProjektleitung().getVorname().toLowerCase() : "";
                String nachname = (projekt.getProjektleitung() != null && projekt.getProjektleitung().getNachname() != null) ? projekt.getProjektleitung().getNachname().toLowerCase() : "";

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
        projektMitarbeiterAnzahlColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(zaehleProjektMitarbeiter(cellData.getValue()))
        );


        projektTableView.setItems(filteredData);
    }

    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private int zaehleProjektMitarbeiter(Projekt projekt) {
        if (projekt.getMitarbeiterListe() == null) {
            return 0;
        }

        return projekt.getMitarbeiterListe().size();
    }

    private void zeigeMitarbeiterDesProjekts(Projekt projekt) {
        List<ProjektMitarbeiter> projektMitarbeiter = projekt.getMitarbeiterListe();
        StringBuilder text = new StringBuilder();

        if (projektMitarbeiter == null || projektMitarbeiter.isEmpty()) {
            text.append("Diesem Projekt sind keine Mitarbeiter zugeordnet.");
        } else {
            for (ProjektMitarbeiter mitarbeiter : projektMitarbeiter) {
                text.append(mitarbeiter.getAuswahlString());

                if (mitarbeiter.getRolle() != null && !mitarbeiter.getRolle().isBlank()) {
                    text.append(" (").append(mitarbeiter.getRolle()).append(")");
                }

                text.append("\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Projektmitarbeiter");
        alert.setHeaderText(projekt.getBezeichnung());
        alert.setContentText(text.toString());
        alert.showAndWait();
    }


}

