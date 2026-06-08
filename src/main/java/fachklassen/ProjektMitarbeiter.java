package fachklassen;

import java.sql.Date;
import java.util.Objects;

public class ProjektMitarbeiter extends Mitarbeiter{
    private Date vonDatum;
    private Date bisDatum;
    private String rolle;


    public ProjektMitarbeiter(int mitarbeiterId, String persNr, String vorname, String nachname, String strasse, String hausNr, Ort ort, Ressort ressort, Date gebDatum, Vertrag vertrag, String geschlecht, Date vonDatum, Date bisDatum, String rolle) {
        super(mitarbeiterId, persNr, vorname, nachname, strasse, hausNr, ort, ressort, gebDatum, vertrag, geschlecht);
        this.vonDatum = vonDatum;
        this.bisDatum = bisDatum;
        this.rolle = rolle;
    }

    public ProjektMitarbeiter(String persNr, String vorname, String nachname, String strasse, String hausNr, Ort ort, Ressort ressort, Date gebDatum, Vertrag vertrag, String geschlecht, Date vonDatum, Date bisDatum, String rolle) {
        super(persNr, vorname, nachname, strasse, hausNr, ort, ressort, gebDatum, vertrag, geschlecht);
        this.vonDatum = vonDatum;
        this.bisDatum = bisDatum;
        this.rolle = rolle;
    }

    public ProjektMitarbeiter(Mitarbeiter mitarbeiter,Date von, Date bis, String rolle){
        super(mitarbeiter.getMitarbeiterId(),mitarbeiter.getPersNr(),mitarbeiter.getVorname(),mitarbeiter.getNachname(), mitarbeiter.getStrasse(), mitarbeiter.getHausNr(),
                mitarbeiter.getOrt(),mitarbeiter.getRessort(),mitarbeiter.getGebDatum(),mitarbeiter.getVertrag(),mitarbeiter.getGeschlecht());
        this.vonDatum=von;
        this.bisDatum=bis;
        this.rolle = rolle;
    }

    public Date getVonDatum() {
        return vonDatum;
    }

    public void setVonDatum(Date vonDatum) {
        this.vonDatum = vonDatum;
    }

    public Date getBisDatum() {
        return bisDatum;
    }

    public void setBisDatum(Date bisDatum) {
        this.bisDatum = bisDatum;
    }

    public String getRolle() {
        return this.rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProjektMitarbeiter that = (ProjektMitarbeiter) o;
        return Objects.equals(vonDatum, that.vonDatum) && Objects.equals(bisDatum, that.bisDatum) && Objects.equals(this.rolle, that.rolle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vonDatum, bisDatum, rolle);
    }
}
