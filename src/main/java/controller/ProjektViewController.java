package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.ProjektDao;
import db_zeug.RessortDao;
import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import fachklassen.ProjektMitarbeiter;
import fachklassen.Ressort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProjektViewController implements Initializable {

    ObservableList<ProjektMitarbeiter> pmitarbeiters = FXCollections.observableArrayList();
    private Projekt projekt;

    @FXML
    private DatePicker abschlussDatePicker;

    @FXML
    private Label aktuelleLeitungLabel;

    @FXML
    private DatePicker beginnDatePicker;

    @FXML
    private Button besetzungBearbeitenButton;

    @FXML
    private TableColumn<ProjektMitarbeiter, String> besetzungBisColumn;

    @FXML
    private Button besetzungEntfernenButton;

    @FXML
    private Button besetzungHinzufuegenButton;

    @FXML
    private TableColumn<ProjektMitarbeiter, String> besetzungMitarbeiterColumn;

    @FXML
    private SearchableComboBox<Mitarbeiter> besetzungMitarbeiterComboBox;

    @FXML
    private TableColumn<ProjektMitarbeiter, String> besetzungRolleColumn;

    @FXML
    private TableColumn<ProjektMitarbeiter, String> besetzungVonColumn;

    @FXML
    private TextField bezeichnungTextField;

    @FXML
    private DatePicker bisDatumDatePicker;

    @FXML
    private Button projektAbbrechenButton;

    @FXML
    private TableView<ProjektMitarbeiter> projektBesetzungTableView;

    @FXML
    private Button projektSpeichernButton;

    @FXML
    private TextField rolleImProjektTextField;

    @FXML
    private DatePicker vonDatumDatePicker;

    @FXML
    void abbrechenProjekt() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pvs_projekt/projekt_table_view.fxml"));
            Pane detailView = loader.load();


            // Wichtig für das Layout (Wachstum erlauben)
            detailView.setMaxWidth(Double.MAX_VALUE);
            detailView.setMaxHeight(Double.MAX_VALUE);
            AnchorPane pane = (AnchorPane) projektAbbrechenButton.getScene().lookup("#contentPane");
            // In die übergebene Pane setzen und verankern
            pane.getChildren().setAll(detailView);
            AnchorPane.setTopAnchor(detailView, 0.0);
            AnchorPane.setRightAnchor(detailView, 0.0);
            AnchorPane.setBottomAnchor(detailView, 0.0);
            AnchorPane.setLeftAnchor(detailView, 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void projektleitungButton(){
        rolleImProjektTextField.setText("Projektleitung");
    }

    @FXML
    void projektAddMitarbeiterBtn() {
        ProjektMitarbeiter tmp = new ProjektMitarbeiter(besetzungMitarbeiterComboBox.getValue(), Date.valueOf(vonDatumDatePicker.getValue()), Date.valueOf(bisDatumDatePicker.getValue()), rolleImProjektTextField.getText());
        if (projekt.add(tmp)) {
            pmitarbeiters.add(tmp);
        }
        else {
            zeigeHinweis("Projektleitung darf sich zeitlich nicht überschneiden.");
        }

    }

    @FXML
    void speicherProjekt() {
        new ProjektDao().save(projekt);
        abbrechenProjekt();
    }

    @FXML
    void besetzungEntfernen(ActionEvent event) {
        ProjektMitarbeiter ausgewaehlterMitarbeiter = projektBesetzungTableView.getSelectionModel().getSelectedItem();

        if (ausgewaehlterMitarbeiter == null) {
            zeigeHinweis("Bitte zuerst einen Mitarbeiter in der Tabelle auswählen.");
            return;
        }
        if (Objects.equals(ausgewaehlterMitarbeiter.getRolle(), "Projektleitung")) {
            zeigeHinweis("Projektleitung darf nicht gelöscht werden");
            return;
        }

        ButtonType bestaetigenButton = new ButtonType("Bestätigen");
        ButtonType abbrechenButton = new ButtonType("Abbrechen");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Entfernen bestätigen");
        alert.setHeaderText(null);
        alert.setContentText("Soll " + ausgewaehlterMitarbeiter.getAuswahlString() + " aus dem Projekt entfernt werden?");
        alert.getButtonTypes().setAll(bestaetigenButton, abbrechenButton);

        if (alert.showAndWait().orElse(abbrechenButton) == bestaetigenButton) {
            projekt.getMitarbeiterListetoDel().add(ausgewaehlterMitarbeiter);
        }
        pmitarbeiters.remove(ausgewaehlterMitarbeiter);
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
        ladeProjekt();
    }

    private void ladeProjekt() {
        pmitarbeiters.setAll(projekt.getMitarbeiterListe()!=null?projekt.getMitarbeiterListe(): new ArrayList<>());
        besetzungVonColumn.setCellValueFactory(new PropertyValueFactory<>("vonDatum"));
        besetzungBisColumn.setCellValueFactory(new PropertyValueFactory<>("bisDatum"));
        besetzungMitarbeiterColumn.setCellValueFactory(new PropertyValueFactory<>("auswahlString"));
        besetzungRolleColumn.setCellValueFactory(new PropertyValueFactory<>("rolle"));
        projektBesetzungTableView.setItems(pmitarbeiters);
        bezeichnungTextField.setText(projekt.getBezeichnung());

        List<Mitarbeiter> alleMitarbeiter = List.of(new MitarbeiterDao().readAll());
        besetzungMitarbeiterComboBox.getItems().setAll(alleMitarbeiter);
        if (projekt != null) {
            aktuelleLeitungLabel.setText(projekt.getProjektleitung()!=null?projekt.getProjektleitung().getAuswahlString():"---");
            beginnDatePicker.setValue(projekt.getBeginn().toLocalDate());
            abschlussDatePicker.setValue(projekt.getAbschluss().toLocalDate());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        besetzungMitarbeiterComboBox.setConverter(new StringConverter<Mitarbeiter>() {
            @Override public String toString(Mitarbeiter m) { return m != null ? m.getAuswahlString() : ""; }
            @Override public Mitarbeiter fromString(String s) { return null; }
        });
    }

    private void zeigeHinweis(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hinweis");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
