package fachklassen;

public class Vertrag {
    private int vertragId;
    private String bezeichnung;

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
}
