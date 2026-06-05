package db_zeug;

import fachklassen.Vertrag;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VertragDao {

    // Erstellt ein neues Vertrag in der Datenbank
    public boolean create(Vertrag Vertrag) {
        String sql = "INSERT INTO vertragstypen (bezeichnung) VALUES (?)";
        int zeilen = SqlMacher.mach(sql, Vertrag.getBezeichnung());
        return zeilen > 0;
    }

    // Liest ein einzelnes Vertrag anhand seiner ID aus
    public Vertrag readOne(int VertragId) {
        String sql = "SELECT id, bezeichnung FROM vertragstypen WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, VertragId);

        if (ergebnis.isEmpty()) {
            return null;
        }

        return mapToVertrag(ergebnis.get(0));
    }

    // Gibt alle Vertrags aus der Datenbank zurück
    public List<Vertrag> readAll() {
        String sql = "SELECT id, bezeichnung FROM vertragstypen";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        List<Vertrag> Vertrags = new ArrayList<>();

        for (Map<String, Object> zeile : ergebnis) {
            Vertrags.add(mapToVertrag(zeile));
        }
        return Vertrags;
    }

    // Aktualisiert die Bezeichnung eines bestehenden Vertrags
    public boolean update(Vertrag Vertrag) {
        String sql = "UPDATE vertragstypen SET bezeichnung = ? WHERE id = ?";
        int zeilen = SqlMacher.mach(sql, Vertrag.getBezeichnung(), Vertrag.getVertragId());
        return zeilen > 0;
    }

    // Löscht ein Vertrag anhand seiner ID
    public boolean delete(int VertragId) {
        String sql = "DELETE FROM vertragstypen WHERE id = ?";
        int zeilen = SqlMacher.mach(sql, VertragId);
        return zeilen > 0;
    }

    // Speichert das Vertrag (entscheidet über die ID, ob create oder update)
    public boolean save(Vertrag Vertrag) {
        if (readOne(Vertrag.getVertragId()) != null) {
            return update(Vertrag);
        } else {
            return create(Vertrag);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Vertrag-Objekt um
    private Vertrag mapToVertrag(Map<String, Object> zeile) {
        int id = (Integer) zeile.get("id");
        String bezeichnung = (String) zeile.get("bezeichnung");

        // Verwendet den Konstruktor deiner Vertrag-Klasse (int, String)
        return new Vertrag(id, bezeichnung);
    }
}

