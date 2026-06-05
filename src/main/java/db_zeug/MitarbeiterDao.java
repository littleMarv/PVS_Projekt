package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.Ort;
import fachklassen.Ressort;
import fachklassen.Vertrag;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MitarbeiterDao {

    public Mitarbeiter[] readAll() {
        String sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung as ressortbezeichnung, v.bezeichnung as vertragbezeichnung " +
                "FROM mitarbeiter m " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id";

        List<Map<String, Object>> daten = SqlMacher.such(sql);
        List<Mitarbeiter> rueckgabe = new ArrayList<>();

        for (Map<String, Object> zeile : daten) {
            rueckgabe.add(mapToMitarbeiter(zeile));
        }
        return rueckgabe.toArray(new Mitarbeiter[0]);
    }

    public Mitarbeiter readOne(String persnr) {
        String sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung as ressortbezeichnung, v.bezeichnung as vertragbezeichnung " +
                "FROM mitarbeiter m " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id WHERE m.personalnummer = ?;";

        List<Map<String, Object>> daten = SqlMacher.such(sql, persnr);
        if (daten.isEmpty()) {
            return null;
        }
        return mapToMitarbeiter(daten.get(0));
    }

    public Mitarbeiter readOneById(int id) {
        String sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung as ressortbezeichnung, v.bezeichnung as vertragbezeichnung " +
                "FROM mitarbeiter m " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id WHERE m.id = ?;";

        List<Map<String, Object>> daten = SqlMacher.such(sql, id);
        if (daten.isEmpty()) {
            return null;
        }
        return mapToMitarbeiter(daten.get(0));
    }

    public Mitarbeiter[] fuzzyRead(String fuzz) {
        String f = "%" + fuzz + "%";
        String sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung as ressortbezeichnung, v.bezeichnung as vertragbezeichnung " +
                "FROM mitarbeiter m " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id " +
                "WHERE m.personalnummer LIKE ? OR " +
                "m.vorname LIKE ? OR " +
                "m.nachname LIKE ? OR " +
                "m.strasse LIKE ? OR " +
                "m.hausnummer LIKE ? OR " +
                "o.name LIKE ? OR " +
                "o.plz LIKE ? OR " +
                "r.bezeichnung LIKE ? OR" +
                "v.bezeichnung LIKE ?";

        List<Map<String, Object>> daten = SqlMacher.such(sql, f, f, f, f, f, f, f, f, f);
        List<Mitarbeiter> rueckgabe = new ArrayList<>();

        for (Map<String, Object> zeile : daten) {
            rueckgabe.add(mapToMitarbeiter(zeile));
        }
        return rueckgabe.toArray(new Mitarbeiter[0]);
    }

    public boolean deleteOne(String persnr) {
        String sql = "DELETE FROM mitarbeiter WHERE personalnummer = ?";
        int zeilen = SqlMacher.mach(sql, persnr);
        return zeilen > 0;
    }

    public boolean updateOne(Mitarbeiter mitarbeiter) {
        String sql = "UPDATE mitarbeiter SET vorname = ?, nachname = ?, strasse = ?, hausnummer = ?, ort_id = ?, ressort_id = ?, geburtsdatum = ? WHERE personalnummer = ?";
        int zeilen = SqlMacher.mach(sql,
                mitarbeiter.getVorname(),
                mitarbeiter.getNachname(),
                mitarbeiter.getStrasse(),
                mitarbeiter.getHausNr(),
                mitarbeiter.getOrt().getOrtId(),
                mitarbeiter.getRessort().getRessortId(),
                mitarbeiter.getGebDatum(),
                mitarbeiter.getPersNr());
        return zeilen > 0;
    }

    public boolean create(Mitarbeiter mitarbeiter) {
        String sql = "INSERT INTO mitarbeiter (personalnummer, vorname, nachname, strasse, hausnummer, ort_id, ressort_id, geburtsdatum) VALUES (?,?,?,?,?,?,?,?)";
        int zeilen = SqlMacher.mach(sql,
                mitarbeiter.getPersNr(),
                mitarbeiter.getVorname(),
                mitarbeiter.getNachname(),
                mitarbeiter.getStrasse(),
                mitarbeiter.getHausNr(),
                mitarbeiter.getOrt().getOrtId(),
                mitarbeiter.getRessort().getRessortId(),
                mitarbeiter.getGebDatum());
        return zeilen > 0;
    }

    public boolean saveOne(Mitarbeiter m) {
        Mitarbeiter existiertBereits = readOne(m.getPersNr());
        if (existiertBereits == null) {
            return create(m);
        } else {
            return updateOne(m);
        }
    }

    protected Mitarbeiter mapToMitarbeiter(Map<String, Object> zeile) {
        Ort ort = null;
        Ressort ressort = null;
        Vertrag vertrag = null;
        if (zeile.get("ort_id") != null) {
            ort = new Ort(
                    (Integer) zeile.get("ort_id"),
                    (String) zeile.get("ortsname"),
                    (String) zeile.get("plz")
            );
        }
        // Greift nun korrekt auf "ressortbezeichnung" aus dem SQL-Alias zu
        if (zeile.get("ressort_id") != null) {
            ressort = new Ressort(
                    (Integer) zeile.get("ressort_id"),
                    (String) zeile.get("ressortbezeichnung")
            );
        }
        if (zeile.get("vertragstyp_id") != null) {
            vertrag = new Vertrag(
                    (Integer) zeile.get("vertragstyp_id"),
                    (String) zeile.get("vertragbezeichnung")
            );
        }
        return new Mitarbeiter(
                (Integer) zeile.get("id"),
                (String) zeile.get("personalnummer"),
                (String) zeile.get("vorname"),
                (String) zeile.get("nachname"),
                (String) zeile.get("strasse"),
                (String) zeile.get("hausnummer"),
                ort,
                ressort,
                (Date) zeile.get("geburtsdatum"),
                vertrag,
                (String) zeile.get("geschlecht")
        );
    }
}

