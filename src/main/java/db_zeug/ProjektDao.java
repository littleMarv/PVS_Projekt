package db_zeug;

import fachklassen.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjektDao {

    // Erstellt einen neuen Ort in der Datenbank
    public boolean create(Projekt projekt) {
        String sql = "INSERT INTO projekte (bezeichnung , beginn, abschluss) VALUES (?, ?, ?)";
        // mach() liefert die Anzahl geänderter Zeilen oder -1 bei Fehlern
        int ergebnis = SqlMacher.mach(sql,projekt.getBezeichnung(), projekt.getBeginn(), projekt.getAbschluss());
        return ergebnis>0;
    }

    // Liest einen einzelnen Ort anhand seiner ID aus
    public Projekt readOne(int projektId) {
        String sql = "SELECT id, beginn, abschluss, bezeichnung FROM projekte WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, projektId);
        sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung AS ressortbz, v.bezeichnung AS vertragstypbz, mp.von_datum, mp.bis_datum, mp.rolle_im_projekt " +
                "FROM mitarbeiter_projekte mp " +
                "LEFT JOIN mitarbeiter m ON mp.id_mitarbeiter = m.id " +
                "LEFT JOIN orte o ON m.ort_id = o.id " +
                "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id " +
                "WHERE id_projekt = ?";

        List<Map<String, Object>> mitarbeiterRawList = SqlMacher.such(sql, projektId);
        if (ergebnis.isEmpty()) {
            return null;
        }

        return stuffToProjekt(ergebnis.getFirst(), mitarbeiterRawList);
    }

    // Gibt alle Orte aus der Datenbank zurück
    public List<Projekt> readAll() {
        String sql = "SELECT id, beginn, abschluss, bezeichnung FROM projekte";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        if (ergebnis.isEmpty()) {
            return null;
        }
        List<Projekt> rueck = new ArrayList<>();
        for (Map<String,Object> zeile : ergebnis) {
            sql = "SELECT m.*, o.plz, o.ortsname, r.bezeichnung AS ressortbezeichnung, v.bezeichnung AS vertragbezeichnung, mp.von_datum, mp.bis_datum, mp.rolle_im_projekt " +
                    "FROM mitarbeiter_projekte mp " +
                    "LEFT JOIN mitarbeiter m ON mp.id_mitarbeiter = m.id " +
                    "LEFT JOIN orte o ON m.ort_id = o.id " +
                    "LEFT JOIN ressorts r ON m.ressort_id = r.id " +
                    "LEFT JOIN vertragstypen v ON m.vertragstyp_id = v.id " +
                    "WHERE id_projekt = ?";
            List<Map<String, Object>> mitarbeiterRawList = SqlMacher.such(sql, zeile.get("id"));
            rueck.add(stuffToProjekt(zeile, mitarbeiterRawList));
        }
        return rueck;
    }


    public boolean update(Projekt projekt) {
        int projektId = projekt.getProjektId();
        String sql = "UPDATE projekte SET beginn = ?, abschluss = ?, bezeichnung =? WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, projekt.getBeginn(),projekt.getAbschluss(),projekt.getBezeichnung(),projektId);
        for (ProjektMitarbeiter a: projekt.getMitarbeiterListetoDel()){ //gelöschte raus
            sql="DELETE FROM mitarbeiter_projekte WHERE id = ?";
            ergebnis = SqlMacher.mach(sql,a.getMitarbeiterId());
        }
        for (ProjektMitarbeiter a: projekt.getMitarbeiterListetoAdd()){ //neue rein
            sql= "INSERT INTO mitarbeiter_projekte(id_projekt,id_mitarbeiter,von_datum,bis_datum,rolle_im_projekt) VALUES (?,?,?,?,?)";
            ergebnis = SqlMacher.mach(sql,projekt.getProjektId(),a.getMitarbeiterId(),a.getVonDatum(),a.getBisDatum(),a.getRolle());
        }
        return ergebnis > 0;
    }

    // Löscht einen Ort anhand seiner ID
    public boolean delete(int projektId) {
        String sql = "DELETE FROM projekte WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql, projektId);
        return ergebnis > 0;
    }

    // Entscheidet anhand der Existenz in der DB, ob create oder update gerufen wird
    public boolean save(Projekt projekt) {
        Projekt tucket = readOne(projekt.getProjektId());
        if (tucket == null) {
            return create(projekt);
        } else if (projekt.equals(tucket)) {
            return true;
        } else {
            return update(projekt);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Ort-Objekt um
    private Projekt stuffToProjekt(Map<String, Object> projekt, List<Map<String, Object>> mitarbeiterRawList
    ) {
        List<ProjektMitarbeiter> prMa = new ArrayList<>();
        for (Map<String, Object> zeile : mitarbeiterRawList) {
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
            prMa.add( new ProjektMitarbeiter(
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
                    (String) zeile.get("geschlecht"),
                    (Date) zeile.get("von_datum"),
                    (Date) zeile.get("bis_datum"),
                    (String) zeile.get("rolle_im_projekt")
            ));
        }
        return new Projekt((Integer) projekt.get("id"),(String) projekt.get("bezeichnung"),(Date) projekt.get("beginn"),(Date) projekt.get("abschluss"),prMa);
    }
}



