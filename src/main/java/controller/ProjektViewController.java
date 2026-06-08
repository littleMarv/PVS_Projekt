package controller;

import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import org.controlsfx.control.SearchableComboBox;

import java.util.List;

public class ProjektViewController {

    private Projekt projekt;
    private List<Mitarbeiter> mitarbeiters;

    @FXML
    private DatePicker abschlussDatePicker;

    @FXML
    private Label aktuelleLeitungLabel;

    @FXML
    private DatePicker beginnDatePicker;

    @FXML
    private Button besetzungBearbeitenButton;

    @FXML
    private TableColumn<?, ?> besetzungBisColumn;

    @FXML
    private Button besetzungEntfernenButton;

    @FXML
    private Button besetzungHinzufuegenButton;

    @FXML
    private TableColumn<?, ?> besetzungMitarbeiterColumn;

    @FXML
    private SearchableComboBox<?> besetzungMitarbeiterComboBox;

    @FXML
    private TableColumn<?, ?> besetzungRolleColumn;

    @FXML
    private TableColumn<?, ?> besetzungVonColumn;

    @FXML
    private TextField bezeichnungTextField;

    @FXML
    private DatePicker bisDatumDatePicker;

    @FXML
    private Button projektAbbrechenButton;

    @FXML
    private TableView<?> projektBesetzungTableView;

    @FXML
    private Button projektSpeichernButton;

    @FXML
    private TextField rolleImProjektTextField;

    @FXML
    private DatePicker vonDatumDatePicker;

    @FXML
    void AbbrechenProjekt() {
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
    void ProjektAddMitarbeiterBtn(ActionEvent event) {

    }

    @FXML
    void SpeicherProjekt() {


        AbbrechenProjekt();
    }

    @FXML
    void besetzungEntfernen(ActionEvent event) {

    }

}
