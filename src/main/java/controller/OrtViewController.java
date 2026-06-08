package controller;

import fachklassen.Ort;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class OrtViewController {

    // Überschrift der Maske, damit Neu und Bearbeiten unterscheidbar sind
    @FXML
    private Label ortTitelLabel;

    // Eingabefelder aus der Ort-Maske
    @FXML
    private TextField plzTextField;

    @FXML
    private TextField ortsnameTextField;

    public void setAktuellerOrt(Ort ort) {
        // Die Bearbeiten-Maske zeigt den ausgewählten Ort aus der Tabelle an
        ortTitelLabel.setText("Ort bearbeiten");
        plzTextField.setText(ort.getPlz());
        ortsnameTextField.setText(ort.getOrtsname());
    }
}
