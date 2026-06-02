package fachklassen;

public class Ressort {
    private int Id;
    private String bezeichnung;

    public Ressort(int Id, String bezeichnung) {
        this.Id = Id;
        this.bezeichnung = bezeichnung;
    }

    public Ressort(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return Id;
    }

    public void setId(int resortId) {
        this.Id = resortId;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
}
