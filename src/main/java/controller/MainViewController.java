package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainViewController {

    // In diesen Bereich wird immer die gerade ausgewählte View geladen.
    @FXML
    private AnchorPane contentPane;

    // Lädt die Startseite in den mittleren Bereich.
    @FXML
    public void dashboardAnzeigen() {
        contentPane.getChildren().clear();
    }

    // Zeigt die Mitarbeiterliste an.
    @FXML
    public void mitarbeiterAnzeigen() {
        ladeViewInDieMitte("mitarbeiter_table_view");
    }

    // Zeigt die Projektliste an.
    @FXML
    public void projekteAnzeigen() {
        ladeViewInDieMitte("projekt_table_view");
    }

    // Zeigt die Ticketliste an.
    @FXML
    public void ticketsAnzeigen() {
        ladeViewInDieMitte("ticket_table_view");
    }

    // Zeigt die Ortsliste an.
    @FXML
    public void orteAnzeigen() {
        ladeViewInDieMitte("ort_table_view");
    }

    // Zeigt die Ressortliste an.
    @FXML
    public void ressortsAnzeigen() {
        ladeViewInDieMitte("ressort_table_view");
    }

    // Zeigt die Vertragstypenliste an.
    @FXML
    public void vertragstypenAnzeigen() {
        ladeViewInDieMitte("vertragstyp_table_view");
    }

    // Zeigt die Projektbesetzung an.
    @FXML
    public void besetzungAnzeigen() {
        ladeViewInDieMitte("projekt_besetzung_view");
    }

    // Schließt die Anwendung.
    @FXML
    public void beenden() {
        Platform.exit();
    }

    // Diese Hilfsmethode lädt eine FXML-Datei und setzt sie in den Center-Bereich.
    private void ladeViewInDieMitte(String dateiname) {
        ViewLoader loader = new ViewLoader();
        Pane view = loader.loadView(dateiname);

        if (view == null) {
            return;
        }

        contentPane.getChildren().setAll(view);

        // Die geladene View soll den ganzen verfügbaren Bereich in der Mitte nutzen.
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
    }
}