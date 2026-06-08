package controller;

import db_zeug.SqlMacher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;
import java.util.Map;

public class DashboardController {

    @FXML
    private Label mitarbeiterAnzahlLabel;

    @FXML
    private Label projekteAnzahlLabel;

    @FXML
    private Label ticketsAnzahlLabel;

    @FXML
    private Label orteAnzahlLabel;

    // Beim Laden des Dashboards werden die Kennzahlen aus der Datenbank gelesen.
    @FXML
    public void initialize() {
        mitarbeiterAnzahlLabel.setText(String.valueOf(zaehleDatensaetze("mitarbeiter")));
        projekteAnzahlLabel.setText(String.valueOf(zaehleDatensaetze("projekte")));
        ticketsAnzahlLabel.setText(String.valueOf(zaehleDatensaetze("ticket")));
        orteAnzahlLabel.setText(String.valueOf(zaehleDatensaetze("orte")));
    }

    // Gibt die Anzahl der Datensätze einer Tabelle zurück.
    private int zaehleDatensaetze(String tabellenName) {
        List<Map<String, Object>> ergebnis = SqlMacher.such("SELECT COUNT(*) AS anzahl FROM ?", tabellenName);

        if (ergebnis.isEmpty() || ergebnis.getFirst().get("anzahl") == null) {
            return 0;
        }

        Number anzahl = (Number) ergebnis.getFirst().get("anzahl");
        return anzahl.intValue();
    }
}