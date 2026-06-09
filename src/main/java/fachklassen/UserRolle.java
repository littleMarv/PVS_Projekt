package fachklassen;

import java.util.Objects;

public class UserRolle {
    private int id;
    private String bezeichnung;

    public UserRolle(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public UserRolle(int id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRolle userRolle = (UserRolle) o;
        return id == userRolle.id && Objects.equals(bezeichnung, userRolle.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bezeichnung);
    }
}
