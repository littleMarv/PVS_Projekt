package fachklassen;

import java.sql.Date;
import java.util.Objects;

public class Projekt {
    private int projektId;
    private String bezeichnung;
    private Date beginn;
    private Date abschluss;

    public Projekt(String bezeichnung, Date beginn, Date abschluss) {
        this.bezeichnung = bezeichnung;
        this.beginn = beginn;
        this.abschluss = abschluss;
    }

    public Projekt(int projektId, String bezeichnung, Date beginn, Date abschluss) {
        this.projektId = projektId;
        this.bezeichnung = bezeichnung;
        this.beginn = beginn;
        this.abschluss = abschluss;
    }

    public int getProjektId() {
        return projektId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Date getBeginn() {
        return beginn;
    }

    public void setBeginn(Date beginn) {
        this.beginn = beginn;
    }

    public Date getAbschluss() {
        return abschluss;
    }

    public void setAbschluss(Date abschluss) {
        this.abschluss = abschluss;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Projekt projekt = (Projekt) o;
        return projektId == projekt.projektId && Objects.equals(bezeichnung, projekt.bezeichnung) && Objects.equals(beginn, projekt.beginn) && Objects.equals(abschluss, projekt.abschluss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projektId, bezeichnung, beginn, abschluss);
    }
}
