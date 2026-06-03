package fachklassen;

import java.util.Objects;

public class Ressort {
    private int ressortId;
    private String bezeichnung;

    public Ressort(int ressortId, String bezeichnung) {
        this.ressortId = ressortId;
        this.bezeichnung = bezeichnung;
    }

    public Ressort(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getRessortId() {
        return ressortId;
    }

    public void setRessortId(int resortId) {
        this.ressortId = resortId;
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
        Ressort ressort = (Ressort) o;
        return ressortId == ressort.ressortId && Objects.equals(bezeichnung, ressort.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ressortId, bezeichnung);
    }
}
