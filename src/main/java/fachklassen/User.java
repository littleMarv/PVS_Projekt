package fachklassen;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class User {
    private int userId;
    private String eMail;
    private String userName;
    private Mitarbeiter mitarbeiter;
    private UserRolle rolle;
    private boolean isActive;

    public User(int userId, String eMail, String userName, Mitarbeiter mitarbeiter, UserRolle rolle, boolean isActive) {
        this.userId = userId;
        this.eMail = eMail;
        this.userName = userName;
        this.mitarbeiter = mitarbeiter;
        this.rolle = rolle;
        this.isActive=isActive;
    }

    public User(String eMail, String userName, Mitarbeiter mitarbeiter, UserRolle rolle, boolean isActive) {
        this.eMail = eMail;
        this.userName = userName;
        this.mitarbeiter = mitarbeiter;
        this.rolle = rolle;
        this.isActive=isActive;
    }

    public User() {

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

    public UserRolle getRolle() {
        return rolle;
    }

    public void setRolle(UserRolle rolle) {
        this.rolle = rolle;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public String getMitarbeiterString(){
        if (mitarbeiter == null) {return "---";}
        return mitarbeiter.getAuswahlString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && isActive == user.isActive && Objects.equals(eMail, user.eMail) && Objects.equals(userName, user.userName) && Objects.equals(mitarbeiter, user.mitarbeiter) && Objects.equals(rolle, user.rolle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, eMail, userName, mitarbeiter, rolle, isActive);
    }

    public static String hashPassword(String klartextPasswort) {
        try {
            // 1. Die eingebaute SHA-256 Instanz holen
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 2. Das Passwort in Bytes umwandeln und hashen
            byte[] encodedhash = digest.digest(klartextPasswort.getBytes(StandardCharsets.UTF_8));

            // 3. Byte-Array in einen lesbaren Hex-String umwandeln
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString(); // Das ist dein fertiger Hash für die DB

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithmus nicht gefunden!", e);
        }
    }
}

