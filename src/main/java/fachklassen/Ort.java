package fachklassen;

public class Ort {
    private int ortId;
    private String ortsname;
    private String plz;

    public Ort(int ortId, String ortsname, String plz) {
        this.ortId = ortId;
        this.ortsname = ortsname;
        this.plz = plz;
    }

    public Ort(String ortsname, String plz) {
        this.ortsname = ortsname;
        this.plz = plz;
    }

    public int getOrtId() {
        return ortId;
    }

    public void setOrtId(int ortId) {
        this.ortId = ortId;
    }

    public String getOrtsname() {
        return ortsname;
    }

    public void setOrtsname(String ortsname) {
        this.ortsname = ortsname;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }
}
