package fachklassen;

import java.sql.Date;
import java.util.Objects;

public class Mitarbeiter {
    private int mitarbeiterId;
    private String persNr;
    private String vorname;
    private String nachname;
    private String strasse;
    private String hausNr;
    private Ort ort;
    private Ressort ressort;
    private Date gebDatum;

    public Mitarbeiter(int mitarbeiterId, String persNr, String vorname, String nachname, String strasse, String hausNr, Ort ort, Ressort ressort, Date gebDatum) {
        this.mitarbeiterId = mitarbeiterId;
        this.persNr = persNr;
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.hausNr = hausNr;
        this.ort = ort;
        this.ressort = ressort;
        this.gebDatum = gebDatum;
    }

    public Mitarbeiter(String persNr, String vorname, String nachname, String strasse, String hausNr, Ort ort, Ressort ressort, Date gebDatum) {
        this.persNr = persNr;
        this.vorname = vorname;
        this.nachname = nachname;
        this.strasse = strasse;
        this.hausNr = hausNr;
        this.ort = ort;
        this.ressort = ressort;
        this.gebDatum = gebDatum;
    }

    public int getMitarbeiterId() {
        return mitarbeiterId;
    }

    public String getPersNr() {
        return persNr;
    }

    public void setPersNr(String persNr) {
        this.persNr = persNr;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausNr() {
        return hausNr;
    }

    public void setHausNr(String hausNr) {
        this.hausNr = hausNr;
    }

    public Ort getOrt() {
        return ort;
    }

    public void setOrt(Ort ort) {
        this.ort = ort;
    }

    public Ressort getRessort() {
        return ressort;
    }

    public void setRessort(Ressort ressort) {
        this.ressort = ressort;
    }

    public Date getGebDatum() {
        return gebDatum;
    }

    public void setGebDatum(Date gebDatum) {
        this.gebDatum = gebDatum;
    }

    public String getAuswahlString(){
        return this.persNr +"-"+ this.nachname +"-"+ this.vorname;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mitarbeiter that = (Mitarbeiter) o;
        return mitarbeiterId == that.mitarbeiterId && Objects.equals(persNr, that.persNr) && Objects.equals(vorname, that.vorname) && Objects.equals(nachname, that.nachname) && Objects.equals(strasse, that.strasse) && Objects.equals(hausNr, that.hausNr) && Objects.equals(ort, that.ort) && Objects.equals(ressort, that.ressort) && Objects.equals(gebDatum, that.gebDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mitarbeiterId, persNr, vorname, nachname, strasse, hausNr, ort, ressort, gebDatum);
    }
}


