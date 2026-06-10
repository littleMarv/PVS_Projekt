package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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

    // Lädt direkt beim Start die Dashboard-View in die Mitte.
    @FXML
    public void initialize() {
        dashboardAnzeigen();
    }

    // Zeigt die Startseite in der Mitte an.
    @FXML
    public void dashboardAnzeigen() {
        markiereAktivenButton(dashboard);
        ladeViewInDieMitte("dashboard_view");
    }

    // Zeigt die Mitarbeiterliste an.
    @FXML
    public void mitarbeiterAnzeigen() {
        markiereAktivenButton(mitarbeiter);
        ladeViewInDieMitte("mitarbeiter_table_view");
    }

    // Zeigt die Projektliste an.
    @FXML
    public void projekteAnzeigen() {
        markiereAktivenButton(projekte);
        ladeViewInDieMitte("projekt_table_view");
    }

    // Zeigt die Ticketliste an.
    @FXML
    public void ticketsAnzeigen() {
        markiereAktivenButton(tickets);
        ladeViewInDieMitte("ticket_table_view");
    }

    // Zeigt die Ortsliste an.
    @FXML
    public void orteAnzeigen() {
        markiereAktivenButton(orte);
        ladeViewInDieMitte("ort_table_view");
    }

    // Zeigt die Ressortliste an.
    @FXML
    public void ressortsAnzeigen() {
        markiereAktivenButton(ressorts);
        ladeViewInDieMitte("ressort_table_view");
    }

    // Zeigt die Vertragstypenliste an.
    @FXML
    public void vertragstypenAnzeigen() {
        markiereAktivenButton(vertragstypen);
        ladeViewInDieMitte("vertragstyp_table_view");
    }

    // Zeigt die Benutzerliste an.
    @FXML
    public void benutzerAnzeigen() {
        markiereAktivenButton(benutzer);
        ladeViewInDieMitte("benutzer_table_view");
    }

    // Schließt die Anwendung.
    @FXML
    public void beenden() {
        Platform.exit();
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
    // Diese Hilfsmethode lädt eine FXML-Datei und setzt sie in den Center-Bereich.
    private void ladeViewInDieMitte(String dateiname) {
        ViewLoader loader = new ViewLoader();
        Pane view = loader.loadView(dateiname);

        if (view == null) {
            return;
        }
        view.setMaxWidth(Double.MAX_VALUE);
        view.setMaxHeight(Double.MAX_VALUE);

        contentPane.getChildren().setAll(view);

        // Die geladene View soll den ganzen verfügbaren Bereich in der Mitte nutzen.
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }
}
