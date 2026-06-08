package controller;

import db_zeug.OrtDao;
import fachklassen.Ort;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class OrtViewController {

    // Dieser Ort ist null beim Anlegen und gefüllt beim Bearbeiten.
    private Ort aktuellerOrt;

    // Überschrift der Maske, damit Neu und Bearbeiten unterscheidbar sind
    @FXML
    private Label ortTitelLabel;

    // Eingabefelder aus der Ort-Maske
    @FXML
    private TextField plzTextField;

    @FXML
    private TextField ortsnameTextField;

    public void setAktuellerOrt(Ort ort) {
        // Die Bearbeiten-Maske zeigt den ausgewählten Ort aus der Tabelle an.
        aktuellerOrt = ort;
        ortTitelLabel.setText("Ort bearbeiten");
        plzTextField.setText(ort.getPlz());
        ortsnameTextField.setText(ort.getOrtsname());
    }

    @FXML
    void ortSpeichern() {
        // Liest die Eingaben aus den Textfeldern.
        String plz = plzTextField.getText();
        String ortsname = ortsnameTextField.getText();

        if (aktuellerOrt == null) {
            // Kein Ort wurde übergeben: Es wird ein neuer Ort angelegt.
            Ort neuerOrt = new Ort(ortsname, plz);
            new OrtDao().create(neuerOrt);
        } else {
            // Ein Ort wurde übergeben: Der vorhandene Ort wird aktualisiert.
            aktuellerOrt.setPlz(plz);
            aktuellerOrt.setOrtsname(ortsname);
            new OrtDao().update(aktuellerOrt);
        }

        ortAbbrechen();
    }

    @FXML
    void ortAbbrechen() {
        // Bricht die Eingabe ab und lädt wieder die Ortsliste.
        AnchorPane hauptContentPane = (AnchorPane) plzTextField.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            Pane ortTabelle = new ViewLoader().loadView("ort_table_view");
            hauptContentPane.getChildren().setAll(ortTabelle);

            AnchorPane.setTopAnchor(ortTabelle, 0.0);
            AnchorPane.setRightAnchor(ortTabelle, 0.0);
            AnchorPane.setBottomAnchor(ortTabelle, 0.0);
            AnchorPane.setLeftAnchor(ortTabelle, 0.0);
        }
    }
}
