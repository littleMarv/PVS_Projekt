package db_zeug;

import fachklassen.Mitarbeiter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MitarbeiterDao {

    public static Mitarbeiter[] readAll(){
        String sql = "Select * from mitarbeiter;";
        List<Map<String, Object>> daten = SqlMacher.such(sql);
        List<Mitarbeiter> rueckgabe = new ArrayList<>();
        for (Map<String, Object> zeile : daten) {
            Mitarbeiter m = new Mitarbeiter((Integer)zeile.get("Pers_nr"),(String)zeile.get("vorname"),
                    (String)zeile.get("nachname"),(String)zeile.get("geschlecht"));
            rueckgabe.add(m);

            }
        return rueckgabe.toArray(new Mitarbeiter[0]);
    }

    public static Mitarbeiter readOne(int persnr){
        String sql = "Select * from mitarbeiter where Pers_nr = ?";
        List<Map<String, Object>> daten = SqlMacher.such(sql,persnr);
        Mitarbeiter rueckgabe = null;
        for (Map<String, Object> zeile : daten) {
            rueckgabe = new Mitarbeiter((Integer)zeile.get("Pers_nr"),(String)zeile.get("vorname"),
                    (String)zeile.get("nachname"),(String)zeile.get("geschlecht"));
        }
        return rueckgabe;

    }

    public static void deleteOne(int persnr){
        String sql = "DELETE * FROM mitarbeit where Pers_nr = ?";
        SqlMacher.mach(sql,persnr);
    }

    public static void updateOne(Mitarbeiter mitarbeiter){
        String sql = "UPDATE mitarbeiter SET vorname = ?, nachname = ?, geschlecht = ? WHERE Pers_nr = ?";
        SqlMacher.mach(sql,mitarbeiter.getVorname(),mitarbeiter.getNachname(),mitarbeiter.getGeschlecht(),sql,mitarbeiter.getPersNr());
    }

    public static void create(Mitarbeiter mitarbeiter){
        String sql = "Insert into mitarbeiter (Pers_nr, vorname, nachname, geschlecht)  Values (?,?,?,?)";
        SqlMacher.mach(sql,mitarbeiter.getPersNr(),mitarbeiter.getVorname(),mitarbeiter.getNachname(),mitarbeiter.getGeschlecht());
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
