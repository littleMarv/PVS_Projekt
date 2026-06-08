package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.ProjektDao;
import fachklassen.Mitarbeiter;
import fachklassen.Projekt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjektTableViewController implements Initializable {

    ObservableList<Projekt> projektliste = FXCollections.observableArrayList();
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

    }

    @FXML
    void neuProjektButton(ActionEvent event) {

    }

    @FXML
    void projektSuche(KeyEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projektliste.setAll(new ProjektDao().readAll());
        //System.out.println("Mitarbeiterdaten gelesen");
        //System.out.println(mitarbeiterliste);
        projektIdColumn.setCellValueFactory(new PropertyValueFactory<>("projektId"));
        bezeichnungColumn.setCellValueFactory(new PropertyValueFactory<>("bezeichnung"));
        beginnColumn.setCellValueFactory(new PropertyValueFactory<>("beginn"));
        abschlussColumn.setCellValueFactory(new PropertyValueFactory<>("abschluss"));
        projektleitungColumn.setCellValueFactory(new PropertyValueFactory<>("projektleitung"));


        projektTableView.setItems(projektliste);
    }
}

