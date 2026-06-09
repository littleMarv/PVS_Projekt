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
        Mitarbeiter mitarbeiter = new MitarbeiterDao().readOneById((Integer)daten.getFirst().get("mitabeiterId"));
        UserRolle rolle = new UserRollenDao().readOneById((Integer)daten.getFirst().get("rollen_id"));
        return new User((Integer)daten.getFirst().get("Id"),(String) daten.getFirst().get("email"),(String)daten.getFirst().get("username"),mitarbeiter,rolle, (Boolean) daten.getFirst().get("is_active"));
    }

    public User readOneById(int id){
        String sql= "SELECT email, username, id, rollen_id, mitarbeiter_id, is_active from benutzer WHERE id = ?";
        List<Map<String, Object>> ergebnis = SqlMacher.such(sql, id);
        if (ergebnis.isEmpty()){return null;}
        return userFromMap(ergebnis.getFirst());
    }

    public int readIdByRest(User user){
        String sql ="SELECT id from benutzer WHERE username=? AND email=? AND mitarbeiter_id = ?";
        return (int) SqlMacher.such(sql,user.getUserName(),user.geteMail(),user.getMitarbeiter().getMitarbeiterId()).getFirst().get("id)");
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
        String sql = "INSERT INTO benutzer (username, email, password_hash, is_active, mitarbeiter_id, rollen_id) VALUES (?,?,?,?,?,?)";
        int ergebnis = (int)SqlMacher.machUndHolId(sql,user.getUserName(),user.geteMail(),user.getIsActive(),user.getMitarbeiter().getMitarbeiterId(),user.getRolle().getId());
        return readOneById(ergebnis);
    }

    public boolean deleteById(int id){
        String sql = "DELETE FROM benutzer WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql,id);
        return ergebnis>0;
    }

    public User update(User user){
        String sql = "UPDATE benutzer SET username = ?, email = ?, rollen_id= ?, mitarbeiter_id =?, is_active= ? WHERE id = ?";
        int ergebnis = (int)SqlMacher.machUndHolId(sql,user.getUserName(),user.geteMail(),user.getRolle().getId(),user.getMitarbeiter().getMitarbeiterId(),user.getIsActive(),user.getUserId());
        return readOneById(ergebnis);
    }

    public User updatePw(int id, String pwh){
        String sql = "UPDATE benutzer SET pwh = ? WHERE id = ?";
        int ergebnis = SqlMacher.mach(sql,pwh,id);
        return readOneById(ergebnis);
    }

    public User save(User user){
        User vser = readOneById(user.getUserId());
        if (vser != user){
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
        Mitarbeiter mitarbeiter = new MitarbeiterDao().readOneById((Integer)zeile.get("mitabeiterId"));
        UserRolle rolle = new UserRollenDao().readOneById((Integer)zeile.get("rollen_id"));
        return new User((Integer)zeile.get("Id"),(String) zeile.get("email"),(String)zeile.get("username"),mitarbeiter,rolle, (Boolean) zeile.get("is_active"));
    }
}
