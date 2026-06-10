package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.User;
import fachklassen.UserRolle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {

    public User login(String namemail, String pwh){
        String sql = "SELECT * FROM benutzer WHERE (username = ? OR email = ?) " +
                "  AND password_hash = ? " +
                "  AND is_active = 1;";
        List<Map<String, Object>> daten = SqlMacher.such(sql,namemail,namemail,pwh);
        if (daten.isEmpty()) {
            return null;
        }
        Mitarbeiter mitarbeiter = null;
        if (daten.getFirst().get("mitarbeiter_id") != null) {
            mitarbeiter = new MitarbeiterDao().readOneById((Integer) daten.getFirst().get("mitarbeiter_id"));
        }
        UserRolle rolle = new UserRollenDao().readOneById((Integer)daten.getFirst().get("rollen_id"));
        return new User((Integer)daten.getFirst().get("id"),(String) daten.getFirst().get("email"),(String)daten.getFirst().get("username"),mitarbeiter,rolle, istAktiv(daten.getFirst().get("is_active")));
    }

    public User readOneById(int id){
        String sql= "SELECT email, username, id, rollen_id, mitarbeiter_id, is_active from benutzer WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, id);
        if (ergebnis.isEmpty()){return null;}
        return userFromMap(ergebnis.getFirst());
    }

    public int readIdByRest(User user){
        String sql ="SELECT id from benutzer WHERE username=? AND email=? AND mitarbeiter_id = ?";
        return (int) SqlMacher.such(sql,user.getUserName(),user.getEMail(),user.getMitarbeiter().getMitarbeiterId()).getFirst().get("id");
    }

    public List<User> readAll(){
        String sql= "SELECT email, username, id, rollen_id, mitarbeiter_id, is_active from benutzer";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql);
        if (ergebnis.isEmpty()){return null;}
        List<User> rueck= new ArrayList<>();
        for (Map<String,Object> zeile: ergebnis){
            rueck.add(userFromMap(zeile));
        }
        return rueck;
    }

    public User create(User user){
        return create(user, "");
    }

    public User create(User user, String passwortHash){
        String sql = "INSERT INTO benutzer (username, email, password_hash, is_active, mitarbeiter_id, rollen_id) VALUES (?,?,?,?,?,?)";
        long neueId = SqlMacher.machUndHolId(sql,user.getUserName(),user.getEMail(),passwortHash,user.getIsActive(),user.getMitarbeiter().getMitarbeiterId(),user.getRolle().getId());
        return readOneById((int) neueId);
    }

    public boolean deleteById(int id){
        String sql = "DELETE FROM benutzer WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql,id);
        return ergebnis>0;
    }

    public User update(User user){
        String sql = "UPDATE benutzer SET username = ?, email = ?, rollen_id= ?, mitarbeiter_id =?, is_active= ? WHERE id = ?";
        SqlMacher.mach(sql,user.getUserName(),user.getEMail(),user.getRolle().getId(),user.getMitarbeiter().getMitarbeiterId(),user.getIsActive(),user.getUserId());
        return readOneById(user.getUserId());
    }

    public User updatePw(int id, String pwh){
        String sql = "UPDATE benutzer SET password_hash = ? WHERE id = ?";
        SqlMacher.mach(sql,pwh,id);
        return readOneById(id);
    }

    public User save(User user){
        if (user == null){
            return null;
        }
        User vser = readOneById(user.getUserId());
        if (!user.equals(vser)){
            if(vser == null){
                return create(user);
            }
            else {
                return update(user);
            }
        }
        return vser;
    }

    private User userFromMap(Map<String,Object> zeile){
        if (zeile.isEmpty()) {
            return null;
        }
        Mitarbeiter mitarbeiter = null;
        if (zeile.get("mitarbeiter_id") != null) {
            mitarbeiter = new MitarbeiterDao().readOneById((Integer) zeile.get("mitarbeiter_id"));
        }
        UserRolle rolle = new UserRollenDao().readOneById((Integer)zeile.get("rollen_id"));
        return new User((Integer)zeile.get("id"),(String) zeile.get("email"),(String)zeile.get("username"),mitarbeiter,rolle, istAktiv(zeile.get("is_active")));
    }

    private boolean istAktiv(Object wert) {
        if (wert instanceof Boolean) {
            return (Boolean) wert;
        }

        if (wert instanceof Number) {
            return ((Number) wert).intValue() == 1;
        }

        return false;
    }
}
