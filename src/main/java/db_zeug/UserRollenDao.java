package db_zeug;

import fachklassen.Ort;
import fachklassen.User;
import fachklassen.UserRolle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRollenDao {

    public boolean create(UserRolle rolle){
        String sql = "INSERT INTO rollen (bezeichnung) VALUES (?)";
        int ergebnis= SqlMacher.mach(sql,rolle.getBezeichnung());
        return ergebnis>0;

    }

    public List<UserRolle> readAll(){
        String sql = "SELECT * from rollen";
        List<Map<String,Object>> ergebnis = SqlMacher.such(sql);
        List<UserRolle> rueck = new ArrayList<>();
        for (Map<String,Object> zeile: ergebnis){
            rueck.add(new UserRolle((Integer)zeile.get("id"),(String)zeile.get("bezeichnung")));
        }
        return rueck;
    }

    public UserRolle readOneById(Integer rollenId) {
        String sql = "SELECT * from rollen WHERE id= ?";
        List<Map<String,Object>> ergebnis = SqlMacher.such(sql, rollenId);
        List<UserRolle> rueck = new ArrayList<>();
        for (Map<String,Object> zeile: ergebnis){
            rueck.add(new UserRolle((Integer)zeile.get("id"),(String)zeile.get("bezeichnung")));
        }
        return rueck.getFirst();
    }

    public boolean update(UserRolle rolle) {
        String sql = "UPDATE rollen SET bezeichnung = ? WHERE Id = ?";
        int ergebnis = SqlMacher.mach(sql, rolle.getBezeichnung(), rolle.getId());
        return ergebnis > 0;
    }

    // Löscht einen Ort anhand seiner ID
    public boolean delete(int rolleId) {
        String sql = "DELETE FROM rollen WHERE Id = ?";
        int ergebnis = SqlMacher.mach(sql, rolleId);
        return ergebnis > 0;
    }

    // Entscheidet anhand der Existenz in der DB, ob create oder update gerufen wird
    public boolean save(UserRolle rolle) {
        if (readOneById(rolle.getId()) != null) {
            return update(rolle);
        } else {
            return create(rolle);
        }
    }

    // Hilfsmethode: Wandelt eine Tabellenzeile in ein Rollen-Objekt um
    private UserRolle mapToRolle(Map<String, Object> zeile) {
        int id = (int) zeile.get("Id");
        String bezeichnung = (String) zeile.get("bezeichnung");
        return new UserRolle(id, bezeichnung);
    }
}
