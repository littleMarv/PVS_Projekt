package controller;

import fachklassen.Mitarbeiter;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.SearchableComboBox;

public class MitarbeiterViewController {

    @FXML
    private TextField personalnummerTextField;

    @FXML
    private TextField vornameTextField;

    @FXML
    private TextField nachnameTextField;

    @FXML
    private TextField strasseTextField;

    @FXML
    private TextField hausnummerTextField;

    @FXML
    private DatePicker geburtsdatumDatePicker;

    @FXML
    private SearchableComboBox<String> geschlechtComboBox;

    @FXML
    private SearchableComboBox<String> ortComboBox;

    @FXML
    private SearchableComboBox<String> ressortComboBox;

    @FXML
    private SearchableComboBox<String> vertragstypComboBox;

    // Füllt die Maske mit den Daten aus der ausgewählten Tabellenzeile.
    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        if (mitarbeiter == null) {
            return;
        }

        personalnummerTextField.setText(mitarbeiter.getPersNr());
        vornameTextField.setText(mitarbeiter.getVorname());
        nachnameTextField.setText(mitarbeiter.getNachname());
        strasseTextField.setText(mitarbeiter.getStrasse());
        hausnummerTextField.setText(mitarbeiter.getHausNr());
        geschlechtComboBox.setValue(mitarbeiter.getGeschlecht());
        ortComboBox.setValue(mitarbeiter.getOrtsname());
        ressortComboBox.setValue(mitarbeiter.getRessortbz());
        vertragstypComboBox.setValue(mitarbeiter.getVertragbz());

        if (mitarbeiter.getGebDatum() != null) {
            geburtsdatumDatePicker.setValue(mitarbeiter.getGebDatum().toLocalDate());
        }
    }
}