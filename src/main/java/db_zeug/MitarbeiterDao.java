package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.Ort;
import fachklassen.Ressort;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MitarbeiterDao {

    public static Mitarbeiter[] readAll(){
        String sql = "SELECT m.*, o.plz, o.name AS ortsname, r.name AS ressortname " +
                "FROM mitarbeiter m " +
                "LEFT JOIN ort o ON m.ort_id = o.id " +
                "LEFT JOIN ressort r ON m.ressort_id = r.id;";
        List<Map<String, Object>> daten = SqlMacher.such(sql);
        List<Mitarbeiter> rueckgabe = new ArrayList<>();
        for (Map<String, Object> zeile : daten) {
            Ort ort = new Ort((Integer)zeile.get("ort_id"),(String)zeile.get("ortsname"),(String)zeile.get("plz"));
            Ressort ressort = new Ressort((Integer)zeile.get("ressort_id"),(String)zeile.get("bezeichnung"));
            Mitarbeiter m = new Mitarbeiter((Integer)zeile.get("id"),
                    (String)zeile.get("personalnummer"),(String)zeile.get("vorname"),
                    (String) zeile.get("nachname"), (String) zeile.get("strasse"),
                    (String) zeile.get("hausnummer"), ort, ressort,
                    (Date) zeile.get("geburtsdatum"));
            rueckgabe.add(m);

            }
        return rueckgabe.toArray(new Mitarbeiter[0]);
    }

    public static Mitarbeiter readOne(String persnr){
        String sql = "SELECT m.*, o.plz, o.name AS ortsname, r.name AS ressortname " +
                "FROM mitarbeiter m " +
                "LEFT JOIN ort o ON m.ort_id = o.id " +
                "LEFT JOIN ressort r ON m.ressort_id = r.id where personalnummer = ?;";
        List<Map<String, Object>> daten = SqlMacher.such(sql,persnr);
        Mitarbeiter rueckgabe = null;
        for (Map<String, Object> zeile : daten) {
            Ort ort = new Ort((Integer)zeile.get("ort_id"),(String)zeile.get("ortsname"),(String)zeile.get("plz"));
            Ressort ressort = new Ressort((Integer)zeile.get("ressort_id"),(String)zeile.get("bezeichnung"));
            rueckgabe = new Mitarbeiter((Integer)zeile.get("id"),
                    (String)zeile.get("personalnummer"),(String)zeile.get("vorname"),
                    (String) zeile.get("nachname"), (String) zeile.get("strasse"),
                    (String) zeile.get("hausnummer"), ort, ressort,
                    (Date) zeile.get("geburtsdatum"));

        }
        return rueckgabe;

    }

    public static Mitarbeiter[] fuzzyRead(String fuzz){
        String f = "%" + fuzz + "%";
        String sql = "SELECT m.*, o.plz, o.name AS ortsname, r.name AS ressortname " +
                "FROM mitarbeiter m " +
                "LEFT JOIN ort o ON m.ort_id = o.id " +
                "LEFT JOIN ressort r ON m.ressort_id = r.id WHERE personalnummer = ? OR" +
                "vorname LIKE ? OR " +
                "nachname LIKE ? OR " +
                "strasse LIKE ? OR " +
                "hausnummer LIKE ? OR" +
                "ortsname LIKE ? OR" +
                "plz LIKE ? OR" +
                "bezeichnung LIKE ?;";

        List<Map<String, Object>> daten = SqlMacher.such(sql,f,f,f,f,f,f,f,f);
        List<Mitarbeiter> rueckgabe = new ArrayList<>();
        for (Map<String, Object> zeile : daten) {
            Ort ort = new Ort((Integer)zeile.get("ort_id"),(String)zeile.get("ortsname"),(String)zeile.get("plz"));
            Ressort ressort = new Ressort((Integer)zeile.get("ressort_id"),(String)zeile.get("bezeichnung"));
            Mitarbeiter m = new Mitarbeiter((Integer)zeile.get("id"),
                    (String)zeile.get("personalnummer"),(String)zeile.get("vorname"),
                    (String) zeile.get("nachname"), (String) zeile.get("strasse"),
                    (String) zeile.get("hausnummer"), ort, ressort,
                    (Date) zeile.get("geburtsdatum"));
            rueckgabe.add(m);

        }
        return rueckgabe.toArray(new Mitarbeiter[0]);
    }

    public static void deleteOne(String persnr){
        String sql = "DELETE * FROM mitarbeit where Pers_nr = ?";
        SqlMacher.mach(sql,persnr);
    }

    public static void updateOne(Mitarbeiter mitarbeiter){
        String sql = "UPDATE mitarbeiter SET vorname = ?, nachname = ?, strasse = ?, hausnummer = ?, ort_id = ?, ressort_id = ?, geburtsdatum = ? WHERE personalnummer = ?";
        SqlMacher.mach(sql,
                mitarbeiter.getVorname(),
                mitarbeiter.getNachname(),
                mitarbeiter.getStrasse(),
                mitarbeiter.getHausNr(),
                mitarbeiter.getOrt().getId(), // Falls Ort ein ID-Getter hat
                mitarbeiter.getRessort().getId(), // Falls Ressort ein ID-Getter hat
                mitarbeiter.getGebDatum(),
                mitarbeiter.getPersNr());
    }

    public static void create(Mitarbeiter mitarbeiter){
        String sql = "INSERT INTO mitarbeiter (personalnummer, vorname, nachname, strasse, hausnummer, ort_id, ressort_id, geburtsdatum) VALUES (?,?,?,?,?,?,?,?)";
        SqlMacher.mach(sql,
                mitarbeiter.getPersNr(),
                mitarbeiter.getVorname(),
                mitarbeiter.getNachname(),
                mitarbeiter.getStrasse(),
                mitarbeiter.getHausNr(),
                mitarbeiter.getOrt().getId(),
                mitarbeiter.getRessort().getId(),
                mitarbeiter.getGebDatum());
    }

    public static void saveOne(Mitarbeiter m) {
        // 1. Prüfen, ob die Personalnummer schon in der DB existiert
        Mitarbeiter existiertBereits = readOne(m.getPersNr());

        if (existiertBereits == null) {
            // Nix gefunden -> Neuer Mitarbeiter!
            create(m);
        } else {
            updateOne(m);
        }
    }
}
