package fachklassen;

public class User {
    private int userId;
    private String eMail;
    private String userName;
    private Mitarbeiter mitarbeiter;

    public User(int userId, String eMail, String userName, Mitarbeiter mitarbeiter) {
        this.userId = userId;
        this.eMail = eMail;
        this.userName = userName;
        this.mitarbeiter = mitarbeiter;
    }

    public User(String eMail, String userName, Mitarbeiter mitarbeiter) {
        this.eMail = eMail;
        this.userName = userName;
        this.mitarbeiter = mitarbeiter;
    }

    public int getUserId() {
        return userId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Mitarbeiter getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(Mitarbeiter mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }

}

