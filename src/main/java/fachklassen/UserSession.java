package fachklassen;

import db_zeug.UserDao;

public class UserSession {
    private static UserSession instance;

    private final User user;

    // Privater Konstruktor verhindert unbefugtes Erstellen von außen
    private UserSession(String namemail,String pwh) {
        UserDao userdao = new UserDao();
        this.user = userdao.login(namemail,pwh);
    }

    // Wird einmalig beim erfolgreichen Login aufgerufen
    public static void login(String namemail,String pwh) {
        if (instance == null) {
            instance = new UserSession(namemail,pwh);
        }
    }

    // Ermöglicht das Ausloggen
    public static void logout() {
        instance = null;
    }

    // Globaler Zugriffspunkt für alle Controller
    public static UserSession getInstance() {
        return instance;
    }

    // Getter (KEINE Setter, damit niemand die ID zur Laufzeit ändern kann!)
    public User getUser() { return this.user; }
}
