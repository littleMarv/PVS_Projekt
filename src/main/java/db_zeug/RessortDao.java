package db_zeug;

import fachklassen.Ressort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RessortDao {

    // Erstellt ein neues Ressort in der Datenbank
    public boolean create(Ressort ressort) {
        String sql = "INSERT INTO ressorts (bezeichnung) VALUES (?)";
        int zeilen = SqlMacher.mach(sql, ressort.getBezeichnung());
        return zeilen > 0;
    }

    // Liest ein einzelnes Ressort anhand seiner ID aus
    public Ressort readOne(int ressortId) {
        String sql = "SELECT id, bezeichnung FROM ressorts WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, ressortId);

        if (ergebnis.isEmpty()) {
            return null;
        }

        return mapToRessort(ergebnis.get(0));
    }

    // Gibt alle Ressorts aus der Datenbank zurück
    public List<Ressort> readAll() {
        String sql = "SELECT id, bezeichnung FROM ressorts";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        List<Ressort> ressorts = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            ressorts.add(mapToRessort(zeile));
        }
        return ressorts;
    }

    // Sucht Ressorts nach ihrer Bezeichnung
    public List<Ressort> fuzzyRead(String suchtext) {
        String sql = "SELECT id, bezeichnung FROM ressorts WHERE bezeichnung LIKE ?";
        String suche = "%" + suchtext + "%";

        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, suche);
        List<Ressort> ressorts = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            ressorts.add(mapToRessort(zeile));
        }

        return ressorts;
    }

    // Aktualisiert die Bezeichnung eines bestehenden Ressorts
    public boolean update(Ressort ressort) {
        String sql = "UPDATE ressorts SET bezeichnung = ? WHERE id = ?";
        int zeilen = SqlMacher.mach(sql, ressort.getBezeichnung(), ressort.getRessortId());
        return zeilen > 0;
    }

    // Löscht ein Ressort anhand seiner ID
    public boolean delete(int ressortId) {
        String sql = "DELETE FROM ressorts WHERE id = ?";
        int zeilen = SqlMacher.mach(sql, ressortId);
        return zeilen > 0;
    }

    // Speichert das Ressort (entscheidet über die ID, ob create oder update)
    public boolean save(Ressort ressort) {
        if (readOne(ressort.getRessortId()) != null) {
            return update(ressort);
        } else {
            return create(ressort);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Ressort-Objekt um
    private Ressort mapToRessort(Map<String, Object> zeile) {
        int id = (Integer) zeile.get("id");
        String bezeichnung = (String) zeile.get("bezeichnung");

        // Verwendet den Konstruktor der Ressort-Klasse (int, String)
        return new Ressort(id, bezeichnung);
    }
}
