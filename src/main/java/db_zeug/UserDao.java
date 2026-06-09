package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.User;
import fachklassen.UserRolle;

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
        return new User((Integer)daten.getFirst().get("Id"),(String) daten.getFirst().get("username"),(String)daten.getFirst().get("email"),mitarbeiter);
    }

    public void create(String name, String email, String pwh, int activ, Mitarbeiter m, UserRolle r){
        String sql = "INSERT INTO benutzer (username, email, password_hash, is_active, mitarbeiter_id) VALUES (?,?,?,?,?)";
        SqlMacher.mach(sql,name,email,pwh,activ,m.getMitarbeiterId(),r.getId());
    }

}
