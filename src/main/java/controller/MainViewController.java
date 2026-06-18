package controller;

import fachklassen.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.Arrays;

public class MainViewController {
    // In diesen Bereich wird immer die gerade ausgewählte View geladen.
    @FXML
    private AnchorPane contentPane;

    // Buttons aus der linken Navigation.
    @FXML
    private Button dashboard;
    @FXML
    private Button mitarbeiter;
    @FXML
    private Button projekte;
    @FXML
    private Button tickets;
    @FXML
    private Button orte;
    @FXML
    private Button ressorts;
    @FXML
    private Button vertragstypen;
    @FXML
    private Button benutzer;

    @FXML
    private Label eingelogterUserLabel;

    public Button[] navigationsButtons = {dashboard, mitarbeiter, projekte, tickets, orte, ressorts, vertragstypen, benutzer};
    // Lädt direkt beim Start die Dashboard-View in die Mitte.
    @FXML
    public void initialize() {
        if (UserSession.getInstance()!=null&&UserSession.getInstance().getUser()!= null){
            eingelogterUserLabel.setText(UserSession.getInstance().getUser().getMitarbeiterString());
        }
    }

    public AnchorPane getContentPane() {
        return this.contentPane;
    }
    
    // Zeigt die Startseite in der Mitte an.
    @FXML
    public void dashboardAnzeigen() {
        ViewLoader.getViewLoader().loadView("dashboard_view");
    }

    // Zeigt die Mitarbeiterliste an.
    @FXML
    public void mitarbeiterAnzeigen() {
        ViewLoader.getViewLoader().loadView("mitarbeiter_table_view");
    }

    // Zeigt die Projektliste an.
    @FXML
    public void projekteAnzeigen() {
        ViewLoader.getViewLoader().loadView("projekt_table_view");
    }

    // Zeigt die Ticketliste an.
    @FXML
    public void ticketsAnzeigen() {
        ViewLoader.getViewLoader().loadView("ticket_table_view");
    }

    // Zeigt die Ortsliste an.
    @FXML
    public void orteAnzeigen() {
        ViewLoader.getViewLoader().loadView("ort_table_view");
    }

    // Zeigt die Ressortliste an.
    @FXML
    public void ressortsAnzeigen() {
        ViewLoader.getViewLoader().loadView("ressort_table_view");
    }

    // Zeigt die Vertragstypenliste an.
    @FXML
    public void vertragstypenAnzeigen() {
        ViewLoader.getViewLoader().loadView("vertragstyp_table_view");
    }

    // Zeigt die Benutzerliste an.
    @FXML
    public void benutzerAnzeigen() {
        ViewLoader.getViewLoader().loadView("benutzer_table_view");
    }

    // Schließt die Anwendung.
    @FXML
    public void beenden() {
        Platform.exit();
    }
    public void markCat(String viewname){
        String viewname3 = viewname.substring(0,3);
        Button b = null;
        switch (viewname3){
            case "das" -> b = dashboard;
            case "mit" -> b = mitarbeiter;
            case "pro" -> b = projekte;
            case "tic" -> b = tickets;
            case "ort" -> b = orte;
            case "res" -> b = ressorts;
            case "ver" -> b = vertragstypen;
            case "ben" -> b = benutzer;

            default    -> System.out.println("Unbekannte Ansicht: " + viewname);
        }
        if (b!= null){
            markiereAktivenButton(b);
        }
    }
    // Setzt alle Navigationsbuttons zurück und markiert danach den aktiven Button grün.
    private void markiereAktivenButton(Button aktiverButton) {
        Button[] navigationsButtons = {dashboard, mitarbeiter, projekte, tickets, orte, ressorts, vertragstypen, benutzer};

        for (Button button : navigationsButtons) {
            button.getStyleClass().remove("nav-button-active");
            if (!button.getStyleClass().contains("nav-button")) {
                button.getStyleClass().add("nav-button");
            }
        }

        aktiverButton.getStyleClass().remove("nav-button");
        if (!aktiverButton.getStyleClass().contains("nav-button-active")) {
            aktiverButton.getStyleClass().add("nav-button-active");
        }
    }
}
