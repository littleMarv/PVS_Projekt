package db_zeug;

import fachklassen.Mitarbeiter;
import fachklassen.User;

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
}
