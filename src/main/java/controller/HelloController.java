package controller;

import javafx.fxml.FXML;
import db_zeug.MitarbeiterDao;
import fachklassen.Mitarbeiter;

public class HelloController {

    // Wird aufgerufen, wenn auf den Mitarbeiter-Button geklickt wird
    @FXML
    public void mitarbeiterAnzeigen() {

        // DAO erzeugen
        MitarbeiterDao dao = new MitarbeiterDao();

        // Mitarbeiter aus der Datenbank laden
        Mitarbeiter[] mitarbeiterListe = dao.readAll();

        // Mitarbeiter in der Konsole ausgeben
        for (Mitarbeiter mitarbeiter : mitarbeiterListe) {

            System.out.println(
                    mitarbeiter.getPersNr()
                            + " | "
                            + mitarbeiter.getVorname()
                            + " "
                            + mitarbeiter.getNachname()
            );
        }
    }
}

