package fachklassen;

public class Ort {
    private int Id;
    private String ortsname;
    private String plz;

    public Ort(int ortId, String ortsname, String plz) {
        this.Id = ortId;
        this.ortsname = ortsname;
        this.plz = plz;
    }

    public Ort(String ortsname, String plz) {
        this.ortsname = ortsname;
        this.plz = plz;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
