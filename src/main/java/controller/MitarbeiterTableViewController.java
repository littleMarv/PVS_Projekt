package controller;
import db_zeug.MitarbeiterDao;
import fachklassen.Mitarbeiter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

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
}

