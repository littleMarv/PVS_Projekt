package db_zeug;

import fachklassen.Ort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OrtDao {

    // Erstellt einen neuen Ort in der Datenbank
    public boolean create(Ort ort) {
        String sql = "INSERT INTO orte (ortsname, plz) VALUES (?, ?)";
        // mach() liefert die Anzahl geänderter Zeilen oder -1 bei Fehlern
        int ergebnis = SqlMacher.mach(sql, ort.getOrtsname(), ort.getPlz());
        return ergebnis > 0;
    }

    // Liest einen einzelnen Ort anhand seiner ID aus
    public Ort readOne(int ortId) {
        String sql = "SELECT Id, ortsname, plz FROM orte WHERE Id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, ortId);

        if (ergebnis.isEmpty()) {
            return null;
        }

        return mapToOrt(ergebnis.get(0));
    }

    // Gibt alle Orte aus der Datenbank zurück
    public List<Ort> readAll() {
        String sql = "SELECT Id, ortsname, plz FROM orte";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        List<Ort> orte = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            orte.add(mapToOrt(zeile));
        }
        return orte;
    }

    // Sucht Orte nach Ortsname oder Postleitzahl
    public List<Ort> fuzzyRead(String suchtext) {
        String sql = "SELECT Id, ortsname, plz FROM orte WHERE ortsname LIKE ? OR plz LIKE ?";
        String suche = "%" + suchtext + "%";

        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, suche, suche);
        List<Ort> orte = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            orte.add(mapToOrt(zeile));
        }

        return orte;
    }

    // Aktualisiert einen bestehenden Ort
    public boolean update(Ort ort) {
        String sql = "UPDATE orte SET ortsname = ?, plz = ? WHERE Id = ?";
        int ergebnis = SqlMacher.mach(sql, ort.getOrtsname(), ort.getPlz(), ort.getOrtId());
        return ergebnis > 0;
    }

    // Löscht einen Ort anhand seiner ID
    public boolean delete(int ortId) {
        String sql = "DELETE FROM orte WHERE Id = ?";
        int ergebnis = SqlMacher.mach(sql, ortId);
        return ergebnis > 0;
    }

    // Entscheidet anhand der Existenz in der DB, ob create oder update gerufen wird
    public boolean save(Ort ort) {
        if (readOne(ort.getOrtId()) != null) {
            return update(ort);
        } else {
            return create(ort);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Ort-Objekt um
    private Ort mapToOrt(Map<String, Object> zeile) {
        int id = (int) zeile.get("Id");
        String name = (String) zeile.get("ortsname");
        String plz = (String) zeile.get("plz");
        return new Ort(id, name, plz);
    }
}

