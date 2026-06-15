package controller;

import db_zeug.VertragDao;
import fachklassen.Vertrag;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class VertragstypViewController {

    // Dieser Vertragstyp ist null beim Anlegen und gefüllt beim Bearbeiten.
    private Vertrag aktuellerVertragstyp;

    // Überschrift der Maske, damit Neu und Bearbeiten unterscheidbar sind
    @FXML
    private Label vertragstypTitelLabel;

    // Eingabefeld für die Bezeichnung des Vertragstyps
    @FXML
    private TextField vertragstypBezeichnungTextField;

    public void setAktuellerVertragstyp(Vertrag vertragstyp) {
        // Die Bearbeiten-Maske zeigt den ausgewählten Vertragstyp aus der Tabelle an.
        aktuellerVertragstyp = vertragstyp;
        vertragstypTitelLabel.setText("Vertragstyp bearbeiten");
        vertragstypBezeichnungTextField.setText(vertragstyp.getBezeichnung());
    }

    @FXML
    void vertragstypSpeichern() {
        // Liest die Bezeichnung aus dem Textfeld.
        String bezeichnung = vertragstypBezeichnungTextField.getText();

        if (aktuellerVertragstyp == null) {
            // Kein Vertragstyp wurde übergeben: Es wird ein neuer Vertragstyp angelegt.
            Vertrag neuerVertragstyp = new Vertrag(bezeichnung);
            new VertragDao().create(neuerVertragstyp);
        } else {
            // Ein Vertragstyp wurde übergeben: Der vorhandene Vertragstyp wird aktualisiert.
            aktuellerVertragstyp.setBezeichnung(bezeichnung);
            new VertragDao().update(aktuellerVertragstyp);
        }

        vertragstypAbbrechen();
    }

    @FXML
    void vertragstypAbbrechen() {
        // Bricht die Eingabe ab und lädt wieder die Vertragstypenliste.
        AnchorPane hauptContentPane = (AnchorPane) vertragstypBezeichnungTextField.getScene().lookup("#contentPane");

        if (hauptContentPane != null) {
            Pane vertragstypTabelle = new ViewLoader().loadView("vertragstyp_table_view",hauptContentPane);
            hauptContentPane.getChildren().setAll(vertragstypTabelle);

            AnchorPane.setTopAnchor(vertragstypTabelle, 0.0);
            AnchorPane.setRightAnchor(vertragstypTabelle, 0.0);
            AnchorPane.setBottomAnchor(vertragstypTabelle, 0.0);
            AnchorPane.setLeftAnchor(vertragstypTabelle, 0.0);
        }
    }
}
