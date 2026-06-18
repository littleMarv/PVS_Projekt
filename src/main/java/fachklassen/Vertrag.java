package fachklassen;

import java.util.Objects;

public class Vertrag {
    private int vertragId;
    private String bezeichnung;

    public Vertrag(){}
    public Vertrag(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Vertrag(int vertragId, String bezeichnung) {
        this.vertragId = vertragId;
        this.bezeichnung = bezeichnung;
    }

    public int getVertragId() {
        return vertragId;
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
        Vertrag vertrag = (Vertrag) o;
        return vertragId == vertrag.vertragId && Objects.equals(bezeichnung, vertrag.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragId, bezeichnung);
    }

    @Override
    public String toString() {
        return bezeichnung;
    }
}
