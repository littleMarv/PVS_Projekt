package db_zeug;

import fachklassen.UserRolle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRollenDao {

    public void create(UserRolle rolle){
        String sql = "INSERT INTO rollen (bezeichnung) VALUES (?)";
        SqlMacher.mach(sql,rolle.getBezeichnung());

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
}
