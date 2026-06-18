package controller;

import db_zeug.MitarbeiterDao;
import db_zeug.OrtDao;
import db_zeug.RessortDao;
import db_zeug.VertragDao;
import fachklassen.Mitarbeiter;
import fachklassen.Ort;
import fachklassen.Ressort;
import fachklassen.Vertrag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.ModelService;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MitarbeiterViewController implements Initializable {

    private Mitarbeiter aktuellerMitarbeiter;

    @FXML
    private DatePicker geburtsdatumDatePicker;

    @FXML
    private SearchableComboBox<String> geschlechtComboBox;

    @FXML
    private TextField hausnummerTextField;


    @FXML
    private TextField nachnameTextField;

    @FXML
    private SearchableComboBox<Ort> ortComboBox;

    @FXML
    private TextField personalnummerTextField;

    @FXML
    private SearchableComboBox<Ressort> ressortComboBox;

    @FXML
    private TextField strasseTextField;

    @FXML
    private SearchableComboBox<Vertrag> vertragstypComboBox;

    @FXML
    private TextField vornameTextField;

    @FXML
    private Button mitarbeiterAbbrechenButton;

    @FXML
    private Button mitarbeiterSpeichernButton;

    public Mitarbeiter getAktuellerMitarbeiter() {
        return aktuellerMitarbeiter;
    }

    public void setAktuellerMitarbeiter(Mitarbeiter aktuellerMitarbeiter) {
        this.aktuellerMitarbeiter = aktuellerMitarbeiter;
        ladeMitarbeiter();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> geschlechter = List.of(new String[]{"Frau", "Divers", "Mann"});
        ressortComboBox.getItems().setAll(ModelService.getInstance().getAlleRessorts());
        vertragstypComboBox.getItems().setAll(ModelService.getInstance().getAlleVertraege());
        ortComboBox.getItems().setAll(ModelService.getInstance().getAlleOrte());
        geschlechtComboBox.getItems().setAll(geschlechter);
        aktuellerMitarbeiter = (Mitarbeiter) ModelService.getInstance().getFocusObject();
        ladeMitarbeiter();
    }

    @FXML
    public void setMitarbeiterSpeichernButton() {
        updateTmpMitarbeiter();
        new MitarbeiterDao().saveOne(aktuellerMitarbeiter);
        abrechenButtonClick();
    }

    public void ladeMitarbeiter() {
        personalnummerTextField.setText(aktuellerMitarbeiter.getPersNr());
        geschlechtComboBox.setValue(aktuellerMitarbeiter.getGeschlecht());
        vornameTextField.setText(aktuellerMitarbeiter.getVorname());
        nachnameTextField.setText(aktuellerMitarbeiter.getNachname());
        strasseTextField.setText(aktuellerMitarbeiter.getStrasse());
        hausnummerTextField.setText(aktuellerMitarbeiter.getHausNr());
        if (aktuellerMitarbeiter.getGebDatum() != null) {
            geburtsdatumDatePicker.setValue(aktuellerMitarbeiter.getGebDatum().toLocalDate());
        } else {
            geburtsdatumDatePicker.setValue(null); // Bleibt einfach leer
        }
        ortComboBox.setValue(aktuellerMitarbeiter.getOrt());
        ressortComboBox.setValue(aktuellerMitarbeiter.getRessort());
        vertragstypComboBox.setValue(aktuellerMitarbeiter.getVertrag());

    }

    private void updateTmpMitarbeiter() {
        aktuellerMitarbeiter.setPersNr(personalnummerTextField.getText());
        aktuellerMitarbeiter.setVorname(vornameTextField.getText());
        aktuellerMitarbeiter.setNachname(nachnameTextField.getText());
        aktuellerMitarbeiter.setStrasse(strasseTextField.getText());
        aktuellerMitarbeiter.setHausNr(hausnummerTextField.getText());
        aktuellerMitarbeiter.setOrt(ortComboBox.getValue());
        if (geburtsdatumDatePicker.getValue() != null) {
            aktuellerMitarbeiter.setGebDatum(java.sql.Date.valueOf(geburtsdatumDatePicker.getValue()));
        } else {
            aktuellerMitarbeiter.setGebDatum(null);
        }
        aktuellerMitarbeiter.setGeschlecht(geschlechtComboBox.getValue());
        aktuellerMitarbeiter.setRessort(ressortComboBox.getValue());
        aktuellerMitarbeiter.setVertrag(vertragstypComboBox.getValue());


    }

    @FXML
    public void abrechenButtonClick() {
        try {
            ModelService.getInstance().getVerlauf().removeLast();
            ViewLoader.getViewLoader().loadView(ModelService.getInstance().getVerlauf().getLast());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
