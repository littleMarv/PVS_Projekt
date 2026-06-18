package controller;

import db_zeug.RessortDao;
import fachklassen.Ressort;
import fachklassen.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import model.ModelService;

public class RessortViewController {

    // Dieses Ressort ist null beim Anlegen und gefüllt beim Bearbeiten.
    private Ressort aktuellesRessort = (Ressort) ModelService.getInstance().getFocusObject();;

    // Überschrift der Maske, damit Neu und Bearbeiten unterscheidbar sind
    @FXML
    private Label ressortTitelLabel;

    // Eingabefeld für die Bezeichnung des Ressorts
    @FXML
    private TextField ressortBezeichnungTextField;

    public void setAktuellesRessort(Ressort ressort) {
        // Die Bearbeiten-Maske zeigt das ausgewählte Ressort aus der Tabelle an.
        aktuellesRessort = ressort;
        ressortTitelLabel.setText("Ressort bearbeiten");
        ressortBezeichnungTextField.setText(ressort.getBezeichnung());
    }

    @FXML
    void ressortSpeichern() {
        // Liest die Bezeichnung aus dem Textfeld.
        String bezeichnung = ressortBezeichnungTextField.getText();

        if (aktuellesRessort == null) {
            // Kein Ressort wurde übergeben: Es wird ein neues Ressort angelegt.
            Ressort neuesRessort = new Ressort(bezeichnung);
            new RessortDao().create(neuesRessort);
        } else {
            // Ein Ressort wurde übergeben: Das vorhandene Ressort wird aktualisiert.
            aktuellesRessort.setBezeichnung(bezeichnung);
            new RessortDao().update(aktuellesRessort);
        }

        ressortAbbrechen();
    }

    @FXML
    void ressortAbbrechen() {
        try {
            ModelService.getInstance().getVerlauf().removeLast();
            ViewLoader.getViewLoader().loadView(ModelService.getInstance().getVerlauf().getLast());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
